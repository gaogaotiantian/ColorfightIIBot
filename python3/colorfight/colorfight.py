import time
import queue

from .game_map import GameMap
from .user import User
from .position import Position
from .network import Network
from .constants import update_globals, CMD_ATTACK, CMD_BUILD, CMD_UPGRADE, GAME_VERSION

class Colorfight:
    def __init__(self):
        self.uid = 0
        self.turn = 0
        self.max_turn = 0
        self.round_time = 0
        self.me = None
        self.users = {}
        self.error = {}
        self.game_map = None
        self.info_queue = None
        self.action_queue = None
        self.action_resp_queue = None

    def connect(self, room = 'public', url = None):
        self.info_queue = queue.Queue()
        self.action_queue = queue.Queue()
        self.action_resp_queue = queue.Queue()
        if url == None:
            url = 'https://www.colorfightai.com/gameroom/' + room
        self.nw = Network(self.info_queue, self.action_queue, self.action_resp_queue, url)
        self.nw.setDaemon(True)
        self.nw.start()

    def _update(self, info):
        self.turn = info['turn']
        self.error = info['error']
        self._update_info(info['info'])
        self.game_map = GameMap(self.width, self.height)
        self.game_map._update_info(info['game_map'])
        self.users = {}
        for uid in info['users']:
            user = User()
            user._update_info(info['users'][uid])
            user.cells = {}
            for pos_lst in info['users'][uid]['cells']:
                pos = Position(pos_lst[0], pos_lst[1])
                user.cells[pos] = self.game_map[pos]
            self.users[int(uid)] = user
        if self.uid in self.users:
            self.me = self.users[self.uid]
        else:
            self.me = None

    def _update_info(self, info):
        for field in info:
            setattr(self, field, info[field])
        update_globals(info)

    def update_turn(self):
        info = self.info_queue.get()
        while True:
            while not self.info_queue.empty():
                info = self.info_queue.get()
            if info["turn"] != self.turn:
                if info['info']['game_version'] != GAME_VERSION:
                    print("Please update your bot. You can do git pull or download from the website.")
                break
                
        self._update(info)

    def register(self, username, password, join_key = ''):
        self.action_queue.put({'action': 'register', 
                'username': username, 
                'password': password,
                'join_key': join_key
        })
        time.sleep(0.01)
        try:
            result = self.action_resp_queue.get(timeout = 2)
            if "err_msg" in result:
                print(result["err_msg"])
                return False
            else:
                self.uid = int(result['uid'])
                return True
        except Exception as e:
            raise Exception("Failed to register to the game!")

    def attack(self, position, energy):
        '''
            /param position: a Position object for the attacked position
            /param energy: the energy the user uses

            /return: a string representing a command
        '''
        return "{} {} {} {}".format(CMD_ATTACK, position.x, position.y, energy)

    def build(self, position, building):
        '''
            /param position: a Position object for the build position
            /param building: a letter representing the building

            /return: a string representing a command
        '''
        return "{} {} {} {}".format(CMD_BUILD, position.x, position.y, building)
            
    def upgrade(self, position):
        '''
            /param position: a Position object to upgrade

            /return: a string representing a command
        '''
        return "{} {} {}".format(CMD_UPGRADE, position.x, position.y)

    def send_cmd(self, cmd_list):
        msg = {"action": "command", "cmd_list": cmd_list}
        self.action_queue.put(msg)
        result = self.action_resp_queue.get()
        return result


