from .constants import BLD_ENERGY_WELL, BLD_GOLD_MINE

class BaseBuilding:
    cost = 0
    upgrade_cost = []
    def __init__(self):
        self.level = 1

    def get_energy_source(self, cell):
        return cell.energy

    def get_gold_source(self, cell):
        return cell.gold

    def get_attack_cost(self, cell):
        return cell.attack_cost 

    def is_empty(self):
        return self.name == 'empty'

    def info(self):
        return self.name

class Empty(BaseBuilding):
    name = 'empty'

class Home(BaseBuilding):
    name = 'home'
    cost = (1000, 0)
    upgrade_cost = [(1000, 1000), (2000, 2000), (4000, 4000)]
    def get_energy_source(self, cell):
        return 10 * self.level

    def get_gold_source(self, cell):
        return 10 * self.level

    def get_attack_cost(self, cell):
        return 1000 * self.level

class EnergyWell(BaseBuilding):
    name = "energy_well"
    cost = (100, 0)

    def get_gold_source(self, cell):
        return cell.energy * (1 + self.level)

class GoldMine(BaseBuilding):
    name = "gold_mine"
    cost = (100, 0)

    def get_gold_source(self, cell):
        return cell.gold * (1 + self.level)

def get_building_class(building):
    '''
        return a class based on the string
    '''
    if building == BLD_ENERGY_WELL:
        return EnergyWell
    elif building == BLD_GOLD_MINE:
        return GoldMine
    else:
        return None

def str_to_build_class(s):
    for cls in [Empty, Home, EnergyWell, GoldMine]:
        if cls.name == s:
            return cls
    return Empty
