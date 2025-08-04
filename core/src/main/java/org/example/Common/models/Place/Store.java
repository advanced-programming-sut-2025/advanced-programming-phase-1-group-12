package org.example.Common.models.Place;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.ProductsPackage.StoreProducts;

import java.util.ArrayList;

public class Store implements Place {

    private LocationOfRectangle location;

    private String owner;

    private String nameOfStore;

    private int StartHour;

    private int CloseHour;

    private ArrayList<StoreProducts> storeProducts;

    @JsonIgnore
    private transient Texture storeTexture;

    public Store() {
    }

    public Store(LocationOfRectangle locationOfRectangle, String owner, String nameOfStore, int startHour, int closeHour, ArrayList<StoreProducts> storeProducts) {

        this.location = locationOfRectangle;
        this.owner = owner;
        this.nameOfStore = nameOfStore;
        StartHour = startHour;
        CloseHour = closeHour;
        this.storeProducts = storeProducts;

        switch (nameOfStore) {
            case "Blacksmith":
                storeTexture = GameAssetManager.blackSmith;
                break;
            case "JojaMart":
                storeTexture = GameAssetManager.jojaMArt;
                break;
            case "Pierre's General Store":
                storeTexture = GameAssetManager.PierresGeneral;
                break;
            case "Carpenter's Shop":
                storeTexture = GameAssetManager.Carpenter;
                break;
            case "Fish Shop":
                storeTexture = GameAssetManager.fishShop;
                break;
            case "Marnie's Ranch":
                storeTexture = GameAssetManager.Marnie;
                break;
            default:
                storeTexture = GameAssetManager.StardropSaloon;
        }
    }

    public void setLocation(LocationOfRectangle location) {
        this.location = location;
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
        return this.location;
    }

    @JsonIgnore
    public Texture getStoreTexture() {
        return storeTexture;
    }
}
