from .constants import BLD_ENERGY_WELL, BLD_GOLD_MINE, BLD_FORTRESS
from .constants import BUILDING_COST, BUILDING_UPGRADE_COST, HOME_COST, HOME_UPGRADE_COST, DESTROY_BONUS

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

    @property
    def is_empty(self):
        return self.name == 'empty'

    @property
    def is_home(self):
        return self.name == 'home'

    @property
    def max_level(self):
        if self.upgrade_cost:
            return len(self.upgrade_cost) + 1
        else:
            return 0

    @property
    def can_upgrade(self):
        return not self.is_empty and self.level < self.max_level

    @property
    def upgrade_gold(self):
        if self.can_upgrade:
            return self.upgrade_cost[self.level - 1][0]
        return None

    @property
    def upgrade_energy(self):
        if self.can_upgrade:
            return self.upgrade_cost[self.level - 1][1]
        return None

    @property
    def destroy_gold(self):
        if self.name == 'gold_mine':
            return DESTROY_BONUS[self.level - 1]
        return 0

    @property
    def destroy_forcefield(self):
        if self.name == 'energy_well':
            return DESTROY_BONUS[self.level - 1]
        return 0

    def info(self):
        return self.name

class Empty(BaseBuilding):
    name = 'empty'

class Home(BaseBuilding):
    name = 'home'
    cost = HOME_COST
    upgrade_cost = HOME_UPGRADE_COST

class EnergyWell(BaseBuilding):
    name = "energy_well"
    cost = BUILDING_COST
    upgrade_cost = BUILDING_UPGRADE_COST

class GoldMine(BaseBuilding):
    name = "gold_mine"
    cost = BUILDING_COST
    upgrade_cost = BUILDING_UPGRADE_COST

class Fortress(BaseBuilding):
    name = "fortress"
    cost = BUILDING_COST
    upgrade_cost = BUILDING_UPGRADE_COST

def get_building_class(building):
    '''
        return a class based on the string
    '''
    if building == BLD_ENERGY_WELL:
        return EnergyWell
    elif building == BLD_GOLD_MINE:
        return GoldMine
    elif building == BLD_FORTRESS:
        return Fortress
    else:
        return None

def letter_to_build_class(s):
    for cls in [Home, EnergyWell, GoldMine, Fortress]:
        if cls.name[0] == s:
            return cls
    return Empty

def str_to_build_class(s):
    for cls in [Empty, Home, EnergyWell, GoldMine, Fortress]:
        if cls.name == s:
            return cls
    return Empty
