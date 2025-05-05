package models;

import models.Animal.Fish;
import models.ToolsPackage.ToolObject;
import models.enums.ToolEnums.BackPackTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BackPack {
    private Map<ToolObject, Integer> tools;
    private BackPackTypes type;
    ArrayList<Fish>fishes = new ArrayList<>();
    public BackPack(BackPackTypes type) {
        this.tools = new HashMap<>();
        this.type = type;
    }

    public void setTools(Map<ToolObject, Integer> tools) {
        this.tools = tools;
    }

    public Map<ToolObject, Integer> getTools() {
        return tools;
    }

    public BackPackTypes getType() {
        return type;
    }

    public void setType(BackPackTypes type) {
        this.type = type;
    }

    public void decreaseToolQuantity(String toolName, int amount) {
        ToolObject toolToUpdate = null;
        for (ToolObject tool : tools.keySet()) {
            if (tool.getToolType().getName().equals(toolName)) {
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
        ToolObject toolToUpdate = null;
        for (ToolObject tool : tools.keySet()) {
            if (tool.getToolType().getName().equals(toolName)) {
                toolToUpdate = tool;
                break;
            }
        }

        if (toolToUpdate != null) {
            tools.remove(toolToUpdate);
        }
    }

    public ArrayList<Fish> getFishes() {
        return fishes;
    }
}
