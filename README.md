# Minecraft Capture-The-Flag Plugin

Minecraft Plugin that used Spigot's API to create a Capture-The-Flag tournament on Minecraft. Built for a series of tournaments between University of Windsor's CS department.

## Objective

The game begins with a preparation period (usually 1.5 hours) where teams will gather resources, fortify their bases and prepare for war. PvP is disabled, and teams should respectively leave eachother alone to prepare.

Teams must place their flag (banner) down inside their base. This is the flag that you must defend (and enemies should capture). This will also act as your "home" which you can teleport to using the */teams home* command.

A scoreboard is displayed on the right side of each players screen.

The battle begins once the preparation period ends. PvP is enabled.

Teams can score points by picking up an enemy flag and returning it to their home base. After a flag has been captured, it will be returned to it's home base and that base will be protected by a defined cooldown time (e.g. 5 minutes) to allow the team to recover and re-fortify their base.

If a flag carrier is killed, the flag is dropped on his position. If the flag is not re-claimed within 30 seconds, it is returned back to it's base.

Players can use compasses that will point to the nearest enemy base.

The team with the most points (captured flags) in the end wins.


## Requirements

Compatible with Minecraft 1.14.4 and Spigot's 1.14.4 versions. Untested with other versions.

## Commands

Attributes inside parentheses are command arguments, i.e. (teamName)

### Admin Commands:
/teams home - *teleport to your flag*  
/teams create (teamName) (teamColor) - *create new a team with the specified name/color*  
/teams delete (teamName) - *delete a team completely (removes members as well)*  
/teams list - *lists all of the current teams*  
/teams add/remove (player) (team) - *add/remove user to/from specified team*  
/teams setFlag (teamName) - *set the location of the specified team's flag*  
/teams getFlag (teamName) - *get the location of the specified team's flag*  
/teams setScore (teamName) (score) - *set specified team's score*  
/teams get (player) - *get the specified user's team name*  
/teams info (teamName) - *list info about the specified team*  
/teams pvp true/false - *enable or disable pvp manually*

### Normal Commands:
/teams help - *list help menu for plugin*  
/teams home - *teleport to your flag*  
/teams list - *lists all of the current teams*  
/teams setFlag - *set the location of your team's flag*  
/teams getFlag - *get the location of your team's flag*  
/teams get - *get your team's name*  
/teams info (teamName) - *list info about your team*  

