package org.example.Common.models.Animal;

import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;

import java.io.Serializable;

public class AnimalHome extends Item {
    private int CapacityRemained;

    //normal coop, Deluxe coop, big coop, normal barn, Deluxe barn, big barn:
    private String Type;

    private LocationOfRectangle location;

    public AnimalHome(String name, Quality quality, int price, int capacityRemained, String type, LocationOfRectangle location) {
        super(name, quality, price);
        CapacityRemained = capacityRemained;
        Type = type;
        this.location = location;
    }

    public AnimalHome() {
    }

    public int getCapacityRemained() {
        return CapacityRemained;
    }

    public void setCapacityRemained(int capacityRemained) {
        CapacityRemained = capacityRemained;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public LocationOfRectangle getLocation() {
        return location;
    }

    public void setLocation(LocationOfRectangle location) {
        this.location = location;
    }

    public Texture getTexture() {
        return GameAssetManager.getGameAssetManager().getAnimalHome();
    }
}
