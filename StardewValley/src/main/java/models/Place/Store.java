package models.Place;

import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.ProductsPackage.StoreProducts;
import models.enums.Season;

import java.util.ArrayList;
import java.util.Map;

public class Store implements Place {

    private LocationOfRectangle locationOfRectangle;

    private String owner;

    private String nameOfStore;

    private int StartHour;

    private int CloseHour;

    //TODO:baraye print mahsoolat migim boro negahe enum e kon harchi mal in boodo print con
    private Map<StoreProducts, Integer> seasonLocations;

    public LocationOfRectangle getLocationOfRectangle() {
        return locationOfRectangle;
    }

    public void setLocationOfRectangle(LocationOfRectangle locationOfRectangle) {
        this.locationOfRectangle = locationOfRectangle;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNameOfStore() {
        return nameOfStore;
    }

    public void setNameOfStore(String nameOfStore) {
        this.nameOfStore = nameOfStore;
    }

    public int getStartHour() {
        return StartHour;
    }

    public void setStartHour(int startHour) {
        StartHour = startHour;
    }

    public int getCloseHour() {
        return CloseHour;
    }

    public void setCloseHour(int closeHour) {
        CloseHour = closeHour;
    }



    @Override
    public LocationOfRectangle getLocation() {
        return this.locationOfRectangle;
    }
}
