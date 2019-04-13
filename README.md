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
      ```User``` object.

* ```error```

    > A map of error ```string```. **key** is ```uid``` and **value** is a list
      of error strings for this user from last round

* ```game_map```

    > ```GameMap``` object for current game map.

## methods

* ```.connect(url)``` 

    > Establish connection to the server. ```url``` is the url of the game room
      you want to join.

* ```.update_turn()```

    > Wait until next turn, and update the game info.
      You should use this command after you send the command list to get the 
      latest info from the game.

* ```.register(username, password)```

    > Register to the game with ```username``` and ```password```. Duplicate 
      ```username``` is not allowed. If you already joined the game and somehow
      disconnected. Using the same ```username``` and ```password``` will allow
      you to continue with previous account.

* ```.attack(position, energy)```

    > Returns a ```string``` for attacking a certain position. ```position``` 
      should be a ```Position``` object and energy should be an integer.

      ** This string is a command that you should put in your command list
      and send with ```send_cmd``` **

* ```.build(position, building)```

    > Returns a ```string``` for build a building on a certain position. 
      ```position``` should be a ```Position``` object and building should be
      a character(Global const variable) representing the building type.

      ** This string is a command that you should put in your command list
      and send with ```send_cmd``` **

* ```.upgrade(position)```

    > Returns a ```string``` for upgrade the build on ```position```. 
      ```position``` should be a ```Position``` object.

      ** This string is a command that you should put in your command list
      and send with ```send_cmd``` **

* ```.send_cmd(cmd_list)```

    > Sends the command list to the server. If you send multiple command lists
      to the server in a single round, the latter one will overlap the former
      ones. Only the last command list the server receives is valid. In theory,
      you should only send one command list every round. 

# Building

## attributes

* ```cost```

    > Cost of the building

* ```name```

    > Name of the building in ```string```

* ```level```

    > Level of the building

# GameMap

## attributes

* ```width```

    > Width of the game map

* ```height```

    > Height of the game map

## methods

* ```game_map[position]```

    > Get the ```MapCell``` object on ```position```. ```position``` should be
      a ```Position``` Object or a ```tuple``` ```(x, y)```.

# MapCell

## attributes

* ```position```

    > ```Position``` object representing the position of the cell.

* ```building```

    > An object for the current building. ```Empty``` if nothing on the cell.
      You should check the ```name``` of the ```building``` to determine what
      kind of building it is. The possible names are ```empty```, ```home```, 
      ```energy_well``` and ```gold_mine```.

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
