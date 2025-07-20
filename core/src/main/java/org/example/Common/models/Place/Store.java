package org.example.Common.models.Place;

import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.ProductsPackage.StoreProducts;

import java.util.ArrayList;

public class Store implements Place {

    private LocationOfRectangle locationOfRectangle;

    private String owner;

    private String nameOfStore;

    private int StartHour;

    private int CloseHour;

    private ArrayList<StoreProducts> storeProducts;

    public Store(LocationOfRectangle locationOfRectangle, String owner, String nameOfStore, int startHour, int closeHour, ArrayList<StoreProducts> storeProducts) {

        this.locationOfRectangle = locationOfRectangle;
        this.owner = owner;
        this.nameOfStore = nameOfStore;
        StartHour = startHour;
        CloseHour = closeHour;
        this.storeProducts = storeProducts;
    }

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

    public ArrayList<StoreProducts> getStoreProducts() {
        return storeProducts;
    }

    public void setStoreProducts(ArrayList<StoreProducts> storeProducts) {
        this.storeProducts = storeProducts;
    }


    @Override
    public LocationOfRectangle getLocation() {
        return this.locationOfRectangle;
    }
}
