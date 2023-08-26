package com.lx862.svrutil.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CommandEntry {
    public String commandName = null;
    public int permLevel = -1;
    public boolean enabled = true;
    public List<String> chainedServerCommand;
    public List<String> chainedPlayerCommand;

    public CommandEntry(String commandName, int permLevel, boolean enabled, List<String> chainedServerCommand, List<String> chainedPlayerCommand) {
        this.commandName = commandName;
        this.permLevel = permLevel;
        this.enabled = enabled;
        this.chainedPlayerCommand = chainedServerCommand;
        this.chainedServerCommand = chainedPlayerCommand;
    }

    public CommandEntry(String commandName, int permLevel, boolean enabled, List<String> chainedServerCommand) {
        this(commandName, permLevel, enabled, chainedServerCommand, new ArrayList<>());
    }

    public CommandEntry(String commandName, int permLevel, boolean enabled) {
        this(commandName, permLevel, enabled, new ArrayList<>(), new ArrayList<>());
    }

    public CommandEntry(JsonObject jsonObject) {
        if(jsonObject.has("commandName")) {
            this.commandName = jsonObject.get("commandName").getAsString();
        }
        if(jsonObject.has("permLevel")) {
            this.permLevel = jsonObject.get("permLevel").getAsInt();
        }
        if(jsonObject.has("enabled")) {
            this.enabled = jsonObject.get("enabled").getAsBoolean();
        }

        this.chainedServerCommand = new ArrayList<>();
        this.chainedPlayerCommand = new ArrayList<>();

        if(jsonObject.has("chainedServerCommand")) {
            jsonObject.getAsJsonArray("chainedServerCommand").forEach(jsonElement -> {
                String command = jsonElement.getAsString();
                chainedServerCommand.add(command);
            });
        }
        if(jsonObject.has("chainedPlayerCommand")) {
            jsonObject.getAsJsonArray("chainedPlayerCommand").forEach(jsonElement -> {
                String command = jsonElement.getAsString();
                chainedPlayerCommand.add(command);
            });
        }
    }

    public CommandEntry(CommandEntry commandEntry) {
        this.commandName = commandEntry.commandName;
        this.chainedPlayerCommand = commandEntry.chainedServerCommand;
        this.chainedServerCommand = commandEntry.chainedServerCommand;
        this.enabled = commandEntry.enabled;
        this.permLevel = commandEntry.permLevel;
    }

    public CommandEntry apply(CommandEntry commandEntry) {
        CommandEntry copy = new CommandEntry(this);

        if(commandEntry.commandName != null) {
            copy.commandName = commandEntry.commandName;
        }
        if(commandEntry.permLevel != -1) {
            copy.permLevel = commandEntry.permLevel;
        }

        copy.enabled = commandEntry.enabled;
        copy.chainedPlayerCommand = commandEntry.chainedPlayerCommand;
        copy.chainedServerCommand = commandEntry.chainedServerCommand;
        return copy;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("commandName", commandName);
        jsonObject.addProperty("permLevel", permLevel);
        jsonObject.addProperty("enabled", enabled);
        if(!chainedServerCommand.isEmpty()) {
            JsonArray chainedServerCommandArray = new JsonArray();
            for(String command : chainedServerCommand) {
                chainedServerCommandArray.add(command);
            }
            jsonObject.add("chainedServerCommand", chainedServerCommandArray);
        }
        if(!chainedPlayerCommand.isEmpty()) {
            JsonArray chainedPlayerCommandArray = new JsonArray();
            for(String command : chainedPlayerCommand) {
                chainedPlayerCommandArray.add(command);
            }
            jsonObject.add("chainedPlayerCommand", chainedPlayerCommandArray);
        }
        return jsonObject;
    }
}
