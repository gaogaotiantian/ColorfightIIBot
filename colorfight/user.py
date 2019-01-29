class User:
    def __init__(self):
        self.uid = 0
        self.energy   = 0
        self.gold     = 0
        self.energy_source = 0
        self.gold_source = 0
        self.dead = False
        self.cells = {}

    def _update_info(self, info):
        for field in info:
            if field != 'cells':
                setattr(self, field, info[field])

    def info(self):
        return {"uid":self.uid, \
                "username": self.username, \
                "energy": self.energy, \
                "gold": self.gold, \
                "dead": self.dead, \
                "energy_source": self.energy_source, \
                "gold_source": self.gold_source, \
                "cells": [cell.position.info() for cell in self.cells.values()]}
