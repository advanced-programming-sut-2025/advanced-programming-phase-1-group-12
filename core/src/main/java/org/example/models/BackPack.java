package org.example.models;

import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Player;
import org.example.models.Fundementals.Result;
import org.example.models.ToolsPackage.Tools;
import org.example.models.ToolsPackage.ToolEnums.BackPackTypes;
import java.util.HashMap;
import java.util.Map;

public class BackPack {
    private Map<Item, Integer> items;
    private Map<String, Item> itemNames;
    private BackPackTypes type;
    private int water;

    public BackPack(BackPackTypes type) {
        this.items = new HashMap<>();
        this.itemNames = new HashMap<>();
        this.type = type;
        this.water = 100;
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
        Player player = App.getCurrentPlayerLazy();
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


        decreaseToolQuantity(name, amount);

        if (recoveryRate > 0) {
            int putBackAmount = (int) (itemToTrash.getPrice() * amount * recoveryRate);
            player.increaseMoney(putBackAmount);
            return new Result(true, "Trashed " + amount + " " + name + " and recovered " +
                              (recoveryRate * 100) + "% of its value." +"\nMoney: " + player.getMoney() + "\nTotal recovery rate: " + recoveryRate + "\n put back amount: " + putBackAmount);
        } else {
            player.increaseMoney(itemToTrash.getPrice() * amount);
            return new Result(true, "Trashed " + amount + " " + name + ". \nMoney: " + player.getMoney());
        }
    }

    public void trash(String name, int amount) {
        decreaseToolQuantity(name, amount);
    }

    public Result trashAll(String toolName, Tools trashCan){
        Player player = App.getCurrentPlayerLazy();
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
        int count = getItemCount(toolToUpdate);
        items.remove(toolToUpdate);

        if (recoveryRate > 0) {
            int putBackAmount = (int) (toolToUpdate.getPrice() * count * recoveryRate);
            player.increaseMoney(putBackAmount);
            return new Result(true, "Trashed all " + toolName + " and recovered " +
                              (recoveryRate * 100) + "% of its value.");
        } else {
            player.increaseMoney(toolToUpdate.getPrice() * count );
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
        this.items.put(item, amount);
        this.itemNames.put(item.getName(), item);
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

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public Item getItemByName(String toolName) {
        return itemNames.get(toolName);
    }

    public Map<String, Item> getItemNames() {
        return itemNames;
    }

    public void setItemNames(Map<String, Item> itemNames) {
        this.itemNames = itemNames;
    }

    public boolean checkCapacity(int amount){
        System.out.println(items.size() + " " + itemNames.size() + " " + amount);

        if((items.size() + amount)<this.getType().getBackPackCapacity()){
            return true;
        }else{
            return false;
        }
    }

    public int getItemCount(Item item) {
        if(item ==null){
            return 0;
        }
        for(Map.Entry<Item, Integer> entry : items.entrySet()) {
            if(entry.getKey().getName().equals(item.getName())) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public boolean hasItem(String itemName){
        return itemNames.containsKey(itemName);
    }
}
