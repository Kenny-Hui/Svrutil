# Config
All config file are stored in the JSON format, and a default config file will be automatically generated if missing.
## Feature Config
The config file are located in `Your Fabric Instance/config/svrutil/feature.json`.

### join_message
This section describes the list of join message entry to show when player joined the server, described below.

| Key     | Description                                 |
|---------|---------------------------------------------|
| entries | An array of Join Message Object, see below. |

#### Join Message Object
| Key        | Description                                                                                                                                       |
|------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| title      | The title that are displayed to the player. ([Simplified Text Format](https://placeholders.pb4.eu/user/text-format/))                             |
| subtitle   | The subtitle that are displayed to the player. ([Simplified Text Format](https://placeholders.pb4.eu/user/text-format/))                          |
| message    | The chat message that are displayed to the player. ([Simplified Text Format](https://placeholders.pb4.eu/user/text-format/))                      |
| delayTick  | How long to wait (in Minecraft tick) before showing the welcome message to the player.                                                            |
| permLevels | An array of numbers representing the op level of the player.<br>Player that are inside the OP Level range will have the welcome message be shown. |

### hunger
This section enforces the hunger range for all players, at a scale from 0 to 20.

| Key      | Description                                       |
|----------|---------------------------------------------------|
| minLevel | The minimum hunger level the player must stay at. |
| maxLevel | The maximum hunger level the player must stay at. |

### vanilla_mechanics
This section contains configurations that overrides Vanilla Minecraft mechanics.

| Key                         | Description                                                                                         |
|-----------------------------|-----------------------------------------------------------------------------------------------------|
| immutableItemFrame          | Whether all item frame in the world should be non-destructible by nature except by players.         |
| minItemFrameInteractOpLevel | The minimum OP Level required to be able to interact with item frame.                               |
| fallingBlockDelay           | How much tick it should take for falling block (i.e. Sand & Gravel) to start falling after placing. |

### fancy_msg
If enabled, this enables a fancier msg of vanilla's `/msg`, `/w` and `/tell` command.

| Key         | Description                                                                                                   |
|-------------|---------------------------------------------------------------------------------------------------------------|
| soundEffect | A string containing the sound ID you want to play to the receiver when message, null to disable sound effect. |

## Command Config
The config file are located in `Your Fabric Instance/config/svrutil/commands.json`.
This config is used to override existing SvrUtil command. For example whether the command should be enabled, the required OP level to run it, and more.
Inside of this file, there should be one property called `overrides`, inside it is a key-value pair of the default SvrUtil command name, and a Command Object indicating the option you want to override for that command.

### Command Object
| Key                  | Description                                                                                                                                                                                  |
|----------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| commandName          | A string representing the command name that should be registered to Minecraft, you may use this to avoid command name conflict by changing this and remap it to another name of your liking. |
| enabled              | If false, this command would not be registered in Minecraft at all, essentially disabling it.                                                                                                |
| permLevel            | A number (integer) representing the minimum OP Level required to execute this command                                                                                                        |
| chainedServerCommand | An array of String representing what command should be executed by the server (console) after the command has finished executing.                                                            |
| chainedPlayerCommand | An array of String representing what command should be executed by the player after the command has finished executing.                                                                      |

### Command Config Example
```
{
  "overrides": {
    "fly": {
      "commandName": "togglefly",
      "chainedPlayerCommand": ["say I have executed the /fly command"],
      "chainedServerCommand": ["tellraw @a \"{playerName} has executed the /fly command\""]
    },
    "gmsp": {
      "enabled": false
    },
    "spawn": {
      "chainedServerCommand": ["/scoreboard players add {playerName} spawn_cmd_count 1"]
    }
  }
}
```

In the above example, assuming the player running the command is called Player234:
- The /fly command is remapped to /togglefly in-game
- After the player ran the /togglefly command, the Player will run the command `/say I have executed the /fly command`
- After the player ran the /togglefly command, the Server will run the command `/tellraw @a "Player234 has executed the /fly command"`
- The /gmsp command has been disabled
- After the player ran the /spawn command, the Server will run the command `/scoreboard players add Player234 spawn_cmd_count 1`, increasing the player's score in scoreboard objective `spawn_cmd_count`.