

# Strafes Plugin
This Strafes plugin was made in order to offer the same expereicence as the one on Hive. This plugin mainly serves the purpose to help map markers in their endeavours of making further DeathRun maps, in order to test them.

# Commands
/leap - Gives the item for leaping to a player (_strafes.leap_)

/strafes - Gives the items for strafing to a player (_strafes.strafes_)

/strafes help - Sends help message (_strafes.help_)

/strafes velocity - Opens the container for velocity editing (_strafes.velocity_)

/strafes velocity strafe <horizontal> <vertical> - Sets the values for strafe velocities (_strafes.velocity_)

/strafes velocity leap <horizontal> <vertical> - Sets the values for leap velocities (_strafes.velocity_)

/strafes cooldowns/cds/cooldown - Opens the container for cooldown editing (_strafes.cooldowns_)

# Setting up powerups
In order to setup a powerup you need to place a sign under the block that has 3 lines (the powerup name, level and duration in seconds). Currently there are 2 powerups registered and working- speed and jump. 

### How do I setup a speed powerup?
Place a sign under a block. On the first line write down speed (since that is what the powerup is called), on the second write the level (the level the potion effect should be) and on the third write down the duration in seconds (2 for 2 seconds).

Jump powerups are setup exactly the same, but with "jump" instead of "speed" on the first line.


# Building and contributing
### Building
The project uses Maven, with no local dependencies. Therefore building it is as simple as cloning the project and running 
```mvn clean verify``` 
inside the directory. Results of the compilation can be found in `/target/`

### API
If you want to use the API feel free to take a look at the [Powerup Demo](https://github.com/Gargant0373/PowerupDemo) I wrote. 

You can use the code as a dependency from JitPack:
[![](https://jitpack.io/v/Gargant0373/REStrafes.svg)](https://jitpack.io/#Gargant0373/REStrafes)

### Contributing
If you want to contribute to the project, all you have to do is to fork the project, make the wanted changes, and create a branch with a description of your feature.

# Bugs and problems
In order to report a bug, head over to the [issues](https://github.com/Gargant0373/REStrafes/issues) section and file a report with as much detail as possible! Remember, we need as much information as possible to be able to fix the issue.


# Frequently asked questions
## I can't see the messages file inside my `/plugins/` folder?
We use M-Lib for sending messages, and all the messages for the plugins live inside the `M-Lib/messages.yml` directory.

## How can I get in touch?
Via discord! You can contact me easily on discord at Gargant#7711!
