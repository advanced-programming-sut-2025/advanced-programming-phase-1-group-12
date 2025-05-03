package models;

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


    public void trash(String name, int amount) {
        decreaseToolQuantity(name,amount);
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
