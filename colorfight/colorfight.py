import time
import queue

from .game_map import GameMap
from .user import User
from .position import Position
from .network import Network

class Colorfight:
    def __init__(self):
        self.uid = 0
        self.turn = 0
        self.max_turn = 0
        self.me = None
        self.users = {}
        self.error = {}
        self.game_map = None
        self.info_queue = None
        self.action_queue = None
        self.action_resp_queue = None

    def connect(self, url = 'ws://localhost:5000'):
        self.info_queue = queue.Queue()
        self.action_queue = queue.Queue()
        self.action_resp_queue = queue.Queue()
        self.nw = Network(self.info_queue, self.action_queue, self.action_resp_queue, url)
        self.nw.setDaemon(True)
        self.nw.start()

    def _update(self, info):
        self.turn = info['turn']
        self.error = info['error']
        self._update_info(info['info'])
        self.game_map = GameMap(self.width, self.height)
        self.game_map._update_info(info['game_map'])
        for uid in info['users']:
            user = User()
            user._update_info(info['users'][uid])
            user.cells = {}
            for pos_lst in info['users'][uid]['cells']:
                pos = Position(pos_lst[0], pos_lst[1])
                user.cells[pos] = self.game_map[pos]
            self.users[uid] = user
        if self.uid in self.users:
            self.me = self.users[self.uid]

    def _update_info(self, info):
        for field in info:
            setattr(self, field, info[field])

    def update_turn(self):
        while self.info_queue.empty():
            # Wait until new info
            pass
        while not self.info_queue.empty():
            info = self.info_queue.get()
        self._update(info)

    def register(self, username, password):
        self.action_queue.put({'action':'register', 'username':username, 'password':password})
        time.sleep(0.1)
        try:
            result = self.action_resp_queue.get(timeout = 2)
            self.uid = result['uid']
        except Exception as e:
            raise Exception("Failed to register to the game!")

            

        


