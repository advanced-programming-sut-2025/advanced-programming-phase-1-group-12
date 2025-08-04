package org.example.Common.models;

import org.example.Common.models.Fundementals.Location;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Refrigrator {
    private Map<Item, Integer> products;
    private int maxProduction;
    private Location location;

    public Refrigrator() {
        products = new HashMap<>();
    }

    public Refrigrator(Location location) {
        this.location = location;
        products = new HashMap<>();
    }

    public Map<Item, Integer> getProducts() {
        return products;
    }

    public int getMaxProduction() {
        return maxProduction;
    }


    public void addItem(Item item, int amount) {
        for(Item item1 : products.keySet()){
            if(item1.getName().equalsIgnoreCase(item.getName())){
                products.put(item1, products.get(item1) + amount);
                return;
            }
        }
        products.put(item,  amount);
    }

    public void decreaseItem(Item item, int amount) {
        if (products.containsKey(item)) {
            int currentAmount = products.get(item);
            if (currentAmount <= amount) {
                products.remove(item);
            } else {
                products.put(item, currentAmount - amount);
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public Item getItemByName(String itemName) {
        for(Item item : products.keySet()){
            if(item.getName().equalsIgnoreCase(itemName)){
                return item;
            }
        }
        return null;
    }
}
