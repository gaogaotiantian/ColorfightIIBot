# Overview

This is the API document for ColorFightII

# Colorfight

The ```Colorfight``` object holds all the useful information for the game. 

## attributes

* ```uid``` 

    > Your user id when you join the game. ```0``` before you join the game.

* ```turn```

    > Current turn number, starts with ```0```.

* ```max_turn```

    > Turn number that this game will last.

* ```round_time```

    > The time the player have to take actions between each round.

* ```me```

    > ```User``` object for you. Invalid before join the game.

* ```users```

    > A map of ```User``` objects. **key** is ```uid``` and **value** is the 
      ```User object.

* ```error```

    > A map of error ```string```. **key** is ```uid``` and **value** is a list
      of error strings for this user from last round

* ```game_map```

    > ```GameMap``` object for current game map.

## methods

* ```.connect(url)``` 

    > Establish connection to the server.

* ```.update_turn()```

    > Wait until next turn, and update the game info.

* ```.register(username, password)```

    > Register to the game with ```username``` and ```password```

* ```.attack(position, energy)```

    > Returns a ```string``` for attacking a certain position. ```position``` 
      should be a ```Position``` object and energy should be an integer.

* ```.build(position, building)```

    > Returns a ```string``` for build a building on a certain position. 
      ```position``` should be a ```Position``` object and building should be
      a character(Global const variable) representing the building type.

* ```.send_cmd(cmd_list)```

    > Sends the command list to the server.

# Building

## attributes

* ```cost```

    > Cost of the building

* ```name```

    > Name of the building in ```string```

# GameMap

## attributes

* ```width```

    > Width of the game map

* ```height```

    > Height of the game map

## methods

* ```game_map[position]```

    > Get the ```MapCell``` object on ```position```. ```position``` should be
      a ```Position``` Object.

# MapCell

## attributes

* ```position```

    > ```Position``` object representing the position of the cell.

* ```building```

    > An object for the current building. ```Empty``` if nothing on the cell.

* ```gold```

    > The amount of gold the cell produces for each round.

* ```energy```

    > The amount of energy the cell produces for each round.

* ```owner```

    > ```uid``` of the owner of this cell.

* ```natural_cost```

* ```natural_gold```

* ```natural_energy```

* ```force_field```

# Position

## attributes

* ```x```

* ```y```

## methods

* ```.is_valid()```

    > If the position is valid on the map

* ```.directional_offset(direction)```

    > Returns a ```Position``` object to the direction. ```direction``` should 
      be a ```Direction``` object.

* ```.get_surrounding_cardinals()```

    > Returns a list of the ```Position``` objects that are right adjacent to
      this position.

# Direction

## attributes

* ```North```

    > (0, -1)

* ```South```

    > (0, 1)

* ```West```

    > (-1, 0)

* ```East```

    > (1, 0)

## methods

* ```get_all_cardinals()```

    > Returns a list of all directions. 

# User

## attributes

* ```uid```

    > User id 

* ```energy```

    > Current energy the user has.

* ```gold```

    > Current gold the user has.

* ```energy_source```

    > The amount of energy the user can produce each round

* ```gold_source```

    > The amount of gold the user can produce each round

* ```cells```

    > A map of ```MapCell``` objects. **key** is ```Position``` object and 
      **value** is ```MapCell``` object.

# constants

* ```BLD_GOLD_MINE```

    > Build character for gold mine

* ```BLD_ENERGY_WELL```

    > Build character for energy well
