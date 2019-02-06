from colorfight import Colorfight
import time
import random
from colorfight.constants import BLD_GOLD_MINE, BLD_ENERGY_WELL

game = Colorfight()
game.connect(url = 'https://colorfightii.herokuapp.com')
if game.register('ExampleAI', str(int(time.time()))):
    while True:
        cmd_list = []
        my_attack_list = []
        game.update_turn()
        if game.me == None:
            continue
        for cell in game.me.cells.values():
            for pos in cell.position.get_surrounding_cardinals():
                c = game.game_map[pos]
                if c.attack_cost < game.me.energy and c.owner != game.uid \
                        and c.position not in my_attack_list:
                    print(c.position, c.owner, game.uid)
                    print(c.attack_cost)
                    cmd_list.append(game.attack(pos, c.attack_cost))
                    game.me.energy -= c.attack_cost
                    my_attack_list.append(c.position)
        game.send_cmd(cmd_list)
