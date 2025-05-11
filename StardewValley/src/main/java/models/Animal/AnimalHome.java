package models.Animal;

import models.Fundementals.LocationOfRectangle;
import models.Item;

public class AnimalHome extends Item {
    private int CapacityRemained;

    //normal coop, Deluxe coop, big coop, normal barn, Deluxe barn, big barn:
    private String Type;

    private LocationOfRectangle location;

    public AnimalHome(int capacityRemained, String type, LocationOfRectangle location) {
        super(type);
        CapacityRemained = capacityRemained;
        Type = type;
        this.location = location;
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
}