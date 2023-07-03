# SvrUtil
Bunch of useful, and occasionally weird specific tweaks and commands for Fabric-compatible server.

## Config file
This mod will work out of the box with all the commands ready to be used.  
There are a config file to modify some vanilla behavior, in that case please see [here](CONFIG.md).

## Commands
Note: These are just the default command configuration, you may optionally disable, change the required permission level or remap a command name in the [Command Config].  

| Command                                                                                     | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | Privilege              |
|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------|
| /afk (reason)                                                                               | Mark yourself as AFK/No longer AFK, optionally with a reason.<br>Note that this only sends a chat message in chat and affects the custom /msg command in this mod.                                                                                                                                                                                                                                                                                                                                                                                                                                                       | Op Level 0<br>(Anyone) |
| /barrierblock                                                                               | Gives you 1 barrier block.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /cmdblock                                                                                   | Gives you 1 command block.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /lightblock (Light Level 0-15)                                                              | Gives you 1 light block, optionally with a specified light level.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        | Op Level 2             |
| /clienttime <reset/day/noon/night/midnight/custom time>                                     | Set the world time only visible for you.<br>This works by repeatedly sending a packet to fake<br>your client on what time the world actually is.<br><br>`/clienttime reset` to follow the server time again.                                                                                                                                                                                                                                                                                                                                                                                                             | Op Level 0<br>(Anyone) |
| /clientweather <clear/rain/thunder/reset>                                                   | Set the weather only visible for you.<br>**You will have to run it again if the weather<br>on the actual world changes**<br><br>`/clientweather reset` to follow the server weather again.                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 0<br>(Anyone) |
| /fancykick <Minecraft Text>                                                                 | Kick someone with Minecraft Raw JSON Text.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /silentkick <Player>                                                                        | Kick a player with a convincing message.<br>By default it is `Internal Exception: java.lang.StackOverflowError`, you may change it in the config file.                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Op Level 2             |
| /feed (Target player)                                                                       | Fill the hunger and saturation to full for you, or the player you specified.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Op Level 2             |
| /fly (Target Player)                                                                        | Toggle flight for you, or the player you specified.<br><br>Note: This works even in Adventure/Survival mode.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             | Op Level 2             |
| /gma                                                                                        | Change your gamemode to Adventure Mode.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | Op Level 2             |
| /gms                                                                                        | Change your gamemode to Survival Mode.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Op Level 2             |
| /gmc                                                                                        | Change your gamemode to Creative Mode.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Op Level 2             |
| /gmsp                                                                                       | Change your gamemode to Spectator Mode.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | Op Level 2             |
| /heal (Target Player)                                                                       | Sets the health & hunger to full for you,<br>or the player you specified.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                | Op Level 2             |
| /svrutil<br>/svrutil reload                                                                 | Main SvrUtil command.`/svrutil` to see the version and homepage.<br>`/svrutil reload` to reload the config                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /spd (speed)                                                                                | Set the player's walking and flying speed factor.<br>If "speed" argument is not provided, it will reset to the vanilla default speed.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    | Op Level 2             |
| /msg <Target Player> <Message>                                                              | This sends a private message to the target player,<br>along with a "ding" sound.<br><br>Note: This will deregister the default vanilla `/msg` command.                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Op Level 0<br>(Anyone) |
| /r <Message>                                                                                | Reply message command.<br>This works the same way for `/msg`, except it automatically sends it<br>to the last player you messaged.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | Op Level 0<br>(Anyone) |
| /opLevel <Player>                                                                           | This returns the OP Level of the specified player.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       | Op Level 2             |
| /silentTp <Player>                                                                          | Teleport to player without sending a public system message                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /overworld                                                                                  | Teleport you to Overworld.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               | Op Level 2             |
| /nether                                                                                     | Teleport you to The Nether.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | Op Level 2             |
| /theend                                                                                     | Teleport you to The End.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 | Op Level 2             |
| /suicide                                                                                    | This kills yourself in-game, along with a public message being sent.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | Op Level 0<br>(Anyone) |
| /spawn                                                                                      | Teleport you to the world spawn.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | Op Level 0<br>(Anyone) |
| /spectate \<Entity to be spectated><br>/spectate \<Target Player> \<Entity to be spectated> | This command locks the player's camera to a specified entity,<br>similar to how you click an entity in spectator mode.<br>This can be used to, for example, force the player to watch a<br>set of animation following an invisible entity.<br><br>Note (Important!):<br>\- This works in any gamemode, even outside of spectator.<br><br>\- The player **cannot** unlock the camera unless they re-login,<br>or use the `/unspectate` command<br><br>\- It only moves the player's camera, the player position remains<br>unchanged<br><br>\- The client cannot load new chunk as the player position is not<br>changed. | Op Level 2             |
| /unspectate<br>/unspectate <Target Player>                                                  | This is the opposite of the /spectate command,<br>unlocking the player's camera.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | Op Level 2             |
| /where <Target Player>                                                                      | Tells the XYZ Coordinates of the player                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  | Op Level 2             |

## Setup
You may run the `downloadOptimizedMod` gradle task if you are not using a NASA computer.

## Bugs/Suggestion
This is originated from another server-specific mod, as such things will be a bit rough around the edges and are not as polished.  
If you have any bugs or suggestions (Even just QOL one), you may open an GitHub issue [here](https://github.com/Kenny-Hui/SvrUtil/issues). Please keep things civil :)

## License
This project is licensed under the MIT License.