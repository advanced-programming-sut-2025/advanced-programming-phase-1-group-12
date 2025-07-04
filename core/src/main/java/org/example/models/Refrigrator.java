package org.example.models;

import java.util.Map;

public class Refrigrator {
    private Map<Item, Integer> products;
    private int maxProduction;

    public Map<Item, Integer> getProducts() {
        return products;
    }

    public int getMaxProduction() {
        return maxProduction;
    }


    public void addItem(Item item, int amount) {
        products.put(item, amount);
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
}
