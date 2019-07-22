# Overview

This is the API document for ColorFightII

# Colorfight

The ```Colorfight``` object holds all the useful information for the game. 

> ```game = Colorfight()``` will create the instance. You should always have
  this object for the game.

> After instantiate the object, you can access the attributes like 
  ```game.uid```, and use the methods like ```game.update_turn()```

## attributes of ```Colorfight```

* ```uid``` 

    > Your user id when you join the game. ```0``` before you join the game.

* ```turn```

    > Current turn number, starts with ```0```.

* ```max_turn```

    > Turn number that this game will last.

* ```round_time```

    > The time in seconds the player have to take actions between each round.

* ```me```

    > ```User``` object for you. Invalid before join the game.

    > This is a useful object. You could do ```me = game.me``` after you do
      ```game.update_turn()``` so that you can access the information of your
      bot.

* ```users```

    > A map of ```User``` objects. **key** is ```uid``` and **value** is the 
      ```User``` object.

    > ```game.users[1]``` will give you the ```User``` object of user with uid
      ```1```.

* ```error```

    > A map of error ```string```. **key** is ```uid``` and **value** is a list
      of error strings for this user from last round

    > Your action could fail due to many reasons. This is the error messages
      from last round. You can do ```print(game.error[game.me.uid])``` to 
      print out all your error messages to debug you bot.

* ```game_map```

    > ```GameMap``` object for current game map.

    > This is an important object that saves all the information of the game
      map, including all the cell info. You can save a shortcut just like 
      ```me```, with ```game_map = game.game_map```

## methods of ```Colorfight```

* ```.get_gameroom_list()``` 

    > return a list of data of gameroom on the server

    > You can use this in the script to choose gameroom ```rooms = game.get_gameroom_list()```
      ```rank_room = [room for room in rooms if room["rank"]]```

* ```.connect(room)``` 

    > Establish connection to the server. ```room``` is the name of the game room
      you want to join.

    > You should always connect first. ```game.connect(room = "public")``` is the default way to connect to the default
    public room

* ```.update_turn()```

    > Wait until next turn, and update the game info. This method will change
      the data of ```Colorfight``` object.
     
    > You should use ```game.update_turn()``` after you send the command list 
      to get the latest info from the game.

* ```.register(username, password, join_key = "")```

    > Register to the game with ```username``` and ```password```. Duplicate 
      ```username``` is not allowed. If you already joined the game and somehow
      disconnected. Using the same ```username``` and ```password``` will allow
      you to continue with previous account. ```join_key``` is required if the
      gameroom is locked, otherwise you do not need to specify the argument. 
      Returns ```True``` if success.

    > This should happen after you connect to the server and before you start
      you game loop.

* ```.attack(position, energy)```

    > Returns a ```string``` for attacking a certain position. ```position``` 
      should be a ```Position``` object or a tuple, ```energy``` should be an 
      integer.

    > ** This string is a command that you should put in your command list
      and send with ```send_cmd``` **
    
    > example: ```game.attack((2, 2), 250)``` will spend 250 energy to attack
      (2, 2). It's equivalent to ```game.attack(Position(2, 2), 250)```.

* ```.build(position, building)```

    > Returns a ```string``` for build a building on a certain position. 
      ```position``` should be a ```Position``` object and building should be
      a character(Global const variable) representing the building type.

    > ** This string is a command that you should put in your command list
      and send with ```send_cmd``` **

    > example: ```game.build((5, 8), BLD_GOLD_MINE)``` will build a gold mine
      on (5, 8) under the condition that you already occupied (5, 8) and you
      have enough gold to build the gold mine. 

* ```.upgrade(position)```

    > Returns a ```string``` for upgrade the build on ```position```. 
      ```position``` should be a ```Position``` object.

    > ** This string is a command that you should put in your command list
      and send with ```send_cmd``` **

    > example: ```game.upgrade((5, 8))``` will upgrade the building on 
      ```(5, 8)``` under the condition that you have a building on 
      ```(5, 8)``` and it can be upgraded. 

* ```.send_cmd(cmd_list)```

    > Sends the command list to the server. If you send multiple command lists
      to the server in a single round, the latter one will overlap the former
      ones. Only the last command list the server receives is valid. In theory,
      you should only send one command list every round. 

    > Returns a ```dict``` from the server of whether the command is taken.
      ```{"success": <boolean>, "err_msg": <str>}```. ```err_msg``` only exists
      if ```success``` is ```False```.

    > All the commands(attack, build, upgrade) above will only return a 
      ```string``` representing the command. So * nothing will happen * if you
      do not send the commands to the server. 
      You should keep a ```cmd_list``` and do something like
      ```cmd_list.append(game.attack((1, 1), 250))``` and 
      ```cmd_list.append(game.build((2, 5), BLD_ENERGY_WELL))```
      Then do a ```game.send_cmd(cmd_list)```

# User

## attributes of ```User```

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

* ```tech_level```

    > The tech_level of the user. This limits the highest level of building
      the user can upgrade to.

* ```tax_amount```
    
    > The amount of energy and gold the user will be taxed.

* ```cells```

    > A map of ```MapCell``` objects. **key** is ```Position``` object and 
      **value** is ```MapCell``` object.

# GameMap

## attributes of ```GameMap```

* ```width```

    > Width of the game map

* ```height```

    > Height of the game map

## methods of ```GameMap```

* ```game_map[position]``` (```game_map.get_cell(position)``` in Java)

    > Get the ```MapCell``` object on ```position```. ```position``` should be
      a ```Position``` object or a ```tuple``` ```(x, y)```.

    > example: ```game_map[(1, 2)]``` will get you the ```MapCell``` object of
      ```(1, 2)```, which is equivalent to ```game_map[Position(1, 2)]```

# MapCell

This is the object representing a cell. ```cell = game.game_map[Position(3, 4)]``` 
stores a ```MapCell``` object of ```(3, 4)``` in ```cell```

## attributes of ```MapCell```

* ```position```

    > ```Position``` object representing the position of the cell.

* ```building```

    > An object for the current building. ```Empty``` if nothing on the cell.
      You should check the ```name``` of the ```building``` to determine what
      kind of building it is. The possible names are ```empty```, ```home```, 
      ```energy_well``` and ```gold_mine```.
    
    > ```cell.building.name == "home"``` will return a boolean whether the cell
      has a home on it

* ```gold```

    > The amount of gold the cell produces for each round.

* ```energy```

    > The amount of energy the cell produces for each round.

* ```owner```

    > ```uid``` of the owner of this cell.

* ```attack_cost```

    > The minimum energy you should use to attack this cell

* ```natural_cost```

* ```natural_gold```

* ```natural_energy```

* ```force_field```

* ```is_empty```
    
    > Whether the building is empty

* ```is_home```
    
    > Whether the building is home 

# Building

## attributes of ```Building```

* ```cost```

    > Cost of the building

* ```name```

    > Name of the building in ```string```

* ```level```

    > Level of the building

* ```is_empty```
    
    > Whether the building is empty

* ```is_home```
    
    > Whether the building is home 

* ```max_level```

    > Maximum level this building can reach

* ```can_upgrade```

    > If the building can be upgraded

* ```upgrade_gold```

    > Amount of gold to upgrade the building. Invalid if cannot upgrade

* ```upgrade_energy```

    > Amount of energy to upgrade the building. Invalid if cannot upgrade

* ```destroy_gold```

    > Amount of gold for the attacker if the building is destroyed. 

* ```destroy_forcefield```

    > Amount of forcefield the cell will have if the building is destroyed

# Position

## attributes of ```Position```

* ```x```

* ```y```

## methods of ```Position```

* ```.is_valid()```

    > If the position is valid on the map

* ```.directional_offset(direction)```

    > Returns a ```Position``` object to the direction. ```direction``` should 
      be a ```Direction``` object.

    > example: if ```pos = Position(2, 3)```,  
      ```pos.directional_offset(Direction.North)``` will return 
      ```Position(2, 2)```

* ```.get_surrounding_cardinals()```

    > Returns a list of the ```Position``` objects that are right adjacent to
      this position.

    > example: if ```pos = Position(2, 3)```. 
      ```pos.get_surrounding_cardinals()``` will return
      ```[Position(2, 2), Position(2, 4), Position(1, 3), Position(3, 3)]```.
      This function is helpful to find the attackable cells.

# Direction

## attributes of ```Direction```

* ```North```

    > (0, -1)

* ```South```

    > (0, 1)

* ```West```

    > (-1, 0)

* ```East```

    > (1, 0)

## methods of ```Direction```

* ```get_all_cardinals()```

    > Returns a list of all directions. 


# constants

* ```BLD_HOME```
    
    > Build character for home

* ```BLD_GOLD_MINE```

    > Build character for gold mine

* ```BLD_ENERGY_WELL```

    > Build character for energy well

* ```BLD_FORTRESS```

    > Build character for fortress
