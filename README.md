# Overview

This is the API document for ColorFightII

# Colorfight

The ```Colorfight``` object holds all the useful information for the game. 

## attributes

* uid 
    > Your user id when you join the game. ```0``` before you join the game.

* turn
    > Current turn number, starts with ```0```.

* max_turn
    > Turn number that this game will last.

* me
    > ```User``` object for you. Invalid before join the game.
