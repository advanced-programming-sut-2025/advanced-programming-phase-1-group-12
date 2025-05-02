package models;

import models.ToolsPackage.Tools;
import models.enums.ToolEnums.BackPackTypes;

import java.util.HashMap;
import java.util.Map;

public class BackPack {
    private Map<Tools, Integer> tools;
    private BackPackTypes type;
    public BackPack(BackPackTypes type) {
        this.tools = new HashMap<>();
        this.type = type;
    }

    public void setTools(Map<Tools, Integer> tools) {
        this.tools = tools;
    }

    public Map<Tools, Integer> getTools() {
        return tools;
    }

    public BackPackTypes getType() {
        return type;
    }

    public void setType(BackPackTypes type) {
        this.type = type;
    }

    public void decreaseToolQuantity(String toolName, int amount) {
        Tools toolToUpdate = null;
        for (Tools tool : tools.keySet()) {
            if (tool.getName().equals(toolName)) {
                toolToUpdate = tool;
                break;
            }
        }

        if (toolToUpdate != null) {
            int currentQuantity = tools.get(toolToUpdate);
            if (currentQuantity > amount) {
                tools.put(toolToUpdate, currentQuantity - amount);
            } else {
                tools.remove(toolToUpdate);
            }
        } else {
            System.out.println("Tool '" + toolName + "' not found in inventory.");
        }
    }


    public void trash(String name, int amount) {
        decreaseToolQuantity(name,amount);
    }

    public void trashAll(String toolName){
        Tools toolToUpdate = null;
        for (Tools tool : tools.keySet()) {
            if (tool.getName().equals(toolName)) {
                toolToUpdate = tool;
                break;
            }
        }

        if (toolToUpdate != null) {
            tools.remove(toolToUpdate);
        }
    }

}
