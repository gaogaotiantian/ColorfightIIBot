from colorfight import Colorfight

game = Colorfight()
game.connect()
game.register('a', 'a')
print(game.uid)
while True:
    game.update_turn()
    print(game.turn)
