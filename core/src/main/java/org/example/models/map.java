package org.example.models;

import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Place.Farm;
import org.example.models.Place.Store;

import java.util.*;

public class map {

    private ArrayList<Farm> farms = new ArrayList<>();
    private ArrayList<Location> tilesOfMap = new ArrayList<>();
    private ArrayList<Store> stores = new ArrayList<>();
    public ArrayList<Farm> getFarms() {
        return farms;
    }

    public void setFarms(ArrayList<Farm> farms) {
        this.farms = farms;
    }

    public ArrayList<Location> getTilesOfMap() {
        return tilesOfMap;
    }

    public void setTilesOfMap(ArrayList<Location> tilesOfMap) {
        this.tilesOfMap = tilesOfMap;
    }

    public Location findLocation(int x, int y){
        for(Location location: App.getCurrentGame().getMainMap().getTilesOfMap()){
            if(location.getxAxis() == x){
                if(location.getyAxis() ==y)
                    return location;
            }
        }
        return null;
    }

    public ArrayList<Store> getStores() {
        return stores;
    }

    public void setStores(ArrayList<Store> stores) {
        this.stores = stores;
    }
}