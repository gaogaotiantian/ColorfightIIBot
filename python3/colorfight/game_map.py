from .position import Position
from .building import Empty, Home, EnergyWell, GoldMine, letter_to_build_class
from .constants import GAME_MAX_LEVEL
import random

class MapCell:
    def __init__(self, position):
        self.position = position
        self.building = Empty()
        self.gold = 0
        self.energy = 0
        self.owner = 0
        self.natural_cost = 0
        self.natural_gold = 0
        self.natural_energy = 0
        self.force_field  = 0

    @property
    def is_empty(self):
        return self.building.is_empty

    @property
    def is_home(self):
        return self.building.is_home

    def _update_info(self, info):
        for field in info:
            if field == 'position':
                self.position = Position(info[field][0], info[field][1])
            elif field == 'building':
                bld_cls = letter_to_build_class(info[field][0])
                self.building = bld_cls()
                self.building.level = info[field][1]
            else:
                setattr(self, field, info[field])

class GameMap:
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self._cells = self._generate_cells(width, height)
    
    def __getitem__(self, location):
        if isinstance(location, Position):
            return self._cells[location.y][location.x]
        elif isinstance(location, tuple):
            return self._cells[location[1]][location[0]]

    def __contains__(self, item):
        if isinstance(item, Position):
            return 0 <= item.x < self.width and 0 <= item.y < self.height
        elif isinstance(item, tuple):
            return 0 <= item[0] < self.width and 0 <= item[1] < self.height
        else:
            return False

    def _update_info(self, info):
        def unpack_cell(headers, cell):
            unpacked_cell = {}
            for idx, header in enumerate(headers):
                unpacked_cell[header] = cell[idx]
            return unpacked_cell

        for row in info['data']:
            for cell in row:
                cell = unpack_cell(info['headers'], cell)
                x = cell['position'][0]
                y = cell['position'][1]
                self._cells[y][x]._update_info(cell)

    def get_cells(self):
        return [self._cells[y][x] for y in range(GAME_HEIGHT) for x in range(GAME_WIDTH)]

    def _generate_cells(self, width, height):
        cells = [[None for _ in range(width)] for _ in range(height)]
        for x in range(width):
            for y in range(height):
                cells[y][x] = MapCell(Position(x, y))
        return cells


