package models.Place;

import models.Fundementals.Location;
import models.ProductsPackage.StoreProducts;

import java.util.ArrayList;

public class Store implements Place {
    private Location.LocationOfRectangle shackLocation;
    private String owner;

    private String nameOfStore;

    private int StartHour;

    private int CloseHour;

    private ArrayList<StoreProducts> storeProducts;

    public Location.LocationOfRectangle getShackLocation() {
        return shackLocation;
    }

    public void setShackLocation(Location.LocationOfRectangle shackLocation) {
        this.shackLocation = shackLocation;
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

    public ArrayList<StoreProducts> getStoreProducts() {
        return storeProducts;
    }

    public void setStoreProducts(ArrayList<StoreProducts> storeProducts) {
        this.storeProducts = storeProducts;
    }
}
