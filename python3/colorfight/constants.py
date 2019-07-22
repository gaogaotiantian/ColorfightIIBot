GAME_VERSION = 2

GAME_WIDTH = 30
GAME_HEIGHT = 30
GAME_MAX_TURN = 500
GAME_MAX_LEVEL = 4

def update_globals(info):
    global GAME_WIDTH
    global GAME_HEIGHT
    global GAME_MAX_TURN
    GAME_WIDTH = info['width']
    GAME_HEIGHT = info['height']
    GAME_MAX_TURN = info['max_turn']

CMD_ATTACK  = 'a'
CMD_BUILD   = 'b'
CMD_UPGRADE = 'u'

BLD_HOME        = 'h'
BLD_GOLD_MINE   = 'g'
BLD_ENERGY_WELL = 'e'
BLD_FORTRESS    = 'f'

BUILDING_COST         = (200, 0)
BUILDING_UPGRADE_COST = [(400, 0), (600, 0)]
HOME_COST             = (1000, 0)
HOME_UPGRADE_COST     = [(1000, 1000), (2000, 2000)]
DESTROY_BONUS         = [100, 300, 600]
