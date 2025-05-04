package models;

import models.Fundementals.Result;
import models.ToolsPackage.Tools;
import models.enums.ToolEnums.BackPackTypes;

import java.util.HashMap;
import java.util.Map;

public class BackPack {
    private Map<Item, Integer> items;
    private Map<String, Item> itemNames;
    private BackPackTypes type;
    public BackPack(BackPackTypes type) {
        this.items = new HashMap<>();
        this.itemNames = new HashMap<>();
        this.type = type;
    }

    public void setItems(Map<Item, Integer> items) {
        this.items = items;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public BackPackTypes getType() {
        return type;
    }

    public void setType(BackPackTypes type) {
        this.type = type;
    }

    public void decreaseToolQuantity(String toolName, int amount) {
        Item toolToUpdate = null;
        for (Item item : items.keySet()) {
            if (item.getName().equals(toolName)) {
                toolToUpdate = item;
                break;
            }
        }

        if (toolToUpdate != null) {
            int currentQuantity = items.get(toolToUpdate);
            if (currentQuantity > amount) {
                items.put(toolToUpdate, currentQuantity - amount);
            } else {
                items.remove(toolToUpdate);
            }
        } else {
            System.out.println("Tool '" + toolName + "' not found in inventory.");
        }
    }


    public Result trash(String name, int amount, Tools trashCan) {
        Item itemToTrash = null;
        for (Item item : items.keySet()) {
            if (item.getName().equals(name)) {
                itemToTrash = item;
                break;
            }
        }

        if (itemToTrash == null) {
            return new Result(false, "Item '" + name + "' not found in inventory.");
        }

        double recoveryRate = 0.0;
        if (trashCan != null && trashCan.isTrashCan()) {
            recoveryRate = trashCan.getTrashCanRecoveryRate();
        }

        // TODO: Calculate and return coins based on recovery rate

        decreaseToolQuantity(name, amount);

        if (recoveryRate > 0) {
            return new Result(true, "Trashed " + amount + " " + name + " and recovered " + 
                              (recoveryRate * 100) + "% of its value.");
        } else {
            return new Result(true, "Trashed " + amount + " " + name + ".");
        }
    }

    public void trash(String name, int amount) {
        decreaseToolQuantity(name, amount);
    }

    public Result trashAll(String toolName, Tools trashCan){
        Item toolToUpdate = null;
        for (Item item : items.keySet()) {
            if (item.getName().equals(toolName)) {
                toolToUpdate = item;
                break;
            }
        }

        if (toolToUpdate == null) {
            return new Result(false, "Item '" + toolName + "' not found in inventory.");
        }

        double recoveryRate = 0.0;
        if (trashCan != null && trashCan.isTrashCan()) {
            recoveryRate = trashCan.getTrashCanRecoveryRate();
        }

        // TODO: Calculate and return coins based on recovery rate

        items.remove(toolToUpdate);

        if (recoveryRate > 0) {
            return new Result(true, "Trashed all " + toolName + " and recovered " + 
                              (recoveryRate * 100) + "% of its value.");
        } else {
            return new Result(true, "Trashed all " + toolName + ".");
        }
    }

    public void trashAll(String toolName){
        Item toolToUpdate = null;
        for (Item item : items.keySet()) {
            if (item.getName().equals(toolName)) {
                toolToUpdate = item;
                break;
            }
        }

        if (toolToUpdate != null) {
            items.remove(toolToUpdate);
        }
    }

    public void addItem(Item item, int amount) {
        items.put(item, amount);
        itemNames.put(item.getName(), item);
    }

    public void decreaseItem(Item item, int amount) {
        if (items.containsKey(item)) {
            int currentAmount = items.get(item);
            if (currentAmount <= amount) {
                items.remove(item);
                itemNames.remove(item.getName());
            } else {
                items.put(item, currentAmount - amount);
            }
        }
    }

    public Item getItemByName(String toolName) {
        return itemNames.get(toolName);
    }

}
