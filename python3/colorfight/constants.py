GAME_WIDTH = 30
GAME_HEIGHT = 30
GAME_MAX_TURN = 500

def update_globals(info):
    global GAME_WIDTH
    global GAME_HEIGHT
    global GAME_MAX_TURN
    GAME_WIDTH = info['width']
    GAME_HEIGHT = info['height']
    GAME_MAX_TURN = info['max_turn']

CMD_ATTACK = 'a'
CMD_BUILD  = 'b'

BLD_GOLD_MINE = 'g'
BLD_ENERGY_WELL = 'e'
