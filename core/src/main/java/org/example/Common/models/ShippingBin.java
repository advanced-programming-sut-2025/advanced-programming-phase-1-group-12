package org.example.Common.models;

import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShippingBin  {
    private Location shippingBinLocation;
    Map<Item, Integer> shippingItem;
    Player owner;
    public ShippingBin(Location shippingBinLocation, Player owner) {
        this.shippingBinLocation = shippingBinLocation;
        this.owner = owner;
        this.shippingItem = new HashMap<>();
    }

    public ShippingBin() {
    }

    public void addShippingItem(Item item, int quantity){
        if(shippingItem.containsKey(item)){
            shippingItem.put(item, shippingItem.get(item) + quantity);
        }
        else{
            shippingItem.put(item, quantity);
        }
    }

    public void removeShippingItem(Item item){
        shippingItem.remove(item);
    }

    public Location getShippingBinLocation() {
        return shippingBinLocation;
    }

    public Map<Item, Integer> getShippingItemMap() {
        return shippingItem;
    }

    public Player getOwner() {
        return owner;
    }

    public List<Item> getShippingItem(Map<Item, Integer> shippingItem) {
        List<Item> items = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : shippingItem.entrySet()) {
            items.add(entry.getKey());
        }
        return items;
    }
    public Location getLocation(){
        return this.shippingBinLocation;
    }

}
