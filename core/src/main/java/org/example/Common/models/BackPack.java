package org.example.Common.models;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.ToolsPackage.Tools;
import org.example.Common.models.ToolsPackage.ToolEnums.BackPackTypes;

import java.util.HashMap;
import java.util.Map;

public class BackPack {
    private Map<Item, Integer> items;
    private CaseInsensitiveMap<Item> itemNames;
    private BackPackTypes type;
    private int water;

    public BackPack(BackPackTypes type) {
        this.items = new HashMap<>();
        this.itemNames = new CaseInsensitiveMap<>();
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
        Item toolToUpdate = findItemIgnoreCase(toolName);
        if (toolToUpdate != null) {
            int currentQuantity = items.get(toolToUpdate);
            if (currentQuantity > amount) {
                items.put(toolToUpdate, currentQuantity - amount);
            } else {
                items.remove(toolToUpdate);
            }
            itemNames.remove(toolName);
        } else {
            System.out.println("Tool '" + toolName + "' not found in inventory.");
        }
    }

    public Result trash(String name, int amount, Tools trashCan) {
        Player player = App.getCurrentPlayerLazy();
        Item itemToTrash = findItemIgnoreCase(name);

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
                (recoveryRate * 100) + "% of its value.\nMoney: " + player.getMoney());
        } else {
            player.increaseMoney(itemToTrash.getPrice() * amount);
            return new Result(true, "Trashed " + amount + " " + name + ".\nMoney: " + player.getMoney());
        }
    }

    public void trash(String name, int amount) {
        decreaseToolQuantity(name, amount);
    }

    public Result trashAll(String toolName, Tools trashCan) {
        Player player = App.getCurrentPlayerLazy();
        Item toolToUpdate = findItemIgnoreCase(toolName);

        if (toolToUpdate == null) {
            return new Result(false, "Item '" + toolName + "' not found in inventory.");
        }

        double recoveryRate = 0.0;
        if (trashCan != null && trashCan.isTrashCan()) {
            recoveryRate = trashCan.getTrashCanRecoveryRate();
        }

        int count = getItemCount(toolName);
        items.remove(toolToUpdate);
        itemNames.remove(toolName);

        if (recoveryRate > 0) {
            int putBackAmount = (int) (toolToUpdate.getPrice() * count * recoveryRate);
            player.increaseMoney(putBackAmount);
            return new Result(true, "Trashed all " + toolName + " and recovered " +
                (recoveryRate * 100) + "% of its value.");
        } else {
            player.increaseMoney(toolToUpdate.getPrice() * count);
            return new Result(true, "Trashed all " + toolName + ".");
        }
    }

    public void trashAll(String toolName) {
        Item toolToUpdate = findItemIgnoreCase(toolName);
        if (toolToUpdate != null) {
            items.remove(toolToUpdate);
            itemNames.remove(toolName);
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
        this.itemNames = new CaseInsensitiveMap<>();
        for (Map.Entry<String, Item> entry : itemNames.entrySet()) {
            this.itemNames.put(entry.getKey(), entry.getValue());
        }
    }

    public boolean checkCapacity(int amount) {
        return (items.size() + amount) < this.getType().getBackPackCapacity();
    }

    public int getItemCount(Item item) {
        if (item == null) {
            return 0;
        }
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (entry.getKey().getName().equalsIgnoreCase(item.getName())) {
                return entry.getValue();
            }
        }
        return 0;
    }

    public int getItemCount(String itemName) {
        return getItemCount(findItemIgnoreCase(itemName));
    }

    public boolean hasItem(String itemName) {
        return itemNames.containsKey(itemName);
    }

    private Item findItemIgnoreCase(String name) {
        for (Item item : items.keySet()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    private static class CaseInsensitiveMap<V> extends HashMap<String, V> {
        @Override
        public V get(Object key) {
            if (key instanceof String strKey) {
                for (Entry<String, V> entry : this.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(strKey)) {
                        return entry.getValue();
                    }
                }
            }
            return null;
        }

        @Override
        public boolean containsKey(Object key) {
            if (key instanceof String strKey) {
                for (String existingKey : this.keySet()) {
                    if (existingKey.equalsIgnoreCase(strKey)) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public V remove(Object key) {
            if (key instanceof String strKey) {
                for (String existingKey : this.keySet()) {
                    if (existingKey.equalsIgnoreCase(strKey)) {
                        return super.remove(existingKey);
                    }
                }
            }
            return null;
        }
    }
}
