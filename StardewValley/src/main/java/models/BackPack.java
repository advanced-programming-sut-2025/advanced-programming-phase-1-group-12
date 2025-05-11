package models;

import models.enums.ToolEnums.BackPackTypes;
import models.enums.Types.FertilizeType;
import models.enums.Types.SeedTypes;
import models.enums.foraging.Plant;
import models.enums.foraging.Seed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BackPack {
    private Map<Tools, Integer> tools;
    private BackPackTypes type;
    private ArrayList<Seed> seeds = new ArrayList<>();
    private ArrayList<FertilizeType> fertilize = new ArrayList<>();
    private int water;
    private ArrayList<Plant> plantOfPlayer = new ArrayList<>();

    public BackPack(BackPackTypes type) {
        this.tools = new HashMap<>();
        this.type = type;
        this.water = 100;
    }

    public ArrayList<Plant> getPlantOfPlayer() {
        return plantOfPlayer;
    }

    public void setPlantOfPlayer(ArrayList<Plant> plantOfPlayer) {
        this.plantOfPlayer = plantOfPlayer;
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

    public ArrayList<Seed> getSeeds() {
        return seeds;
    }

    public void setSeeds(ArrayList<Seed> seeds) {
        this.seeds = seeds;
    }

    public ArrayList<FertilizeType> getFertilize() {
        return fertilize;
    }

    public void setFertilize(ArrayList<FertilizeType> fertilize) {
        this.fertilize = fertilize;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }
}
