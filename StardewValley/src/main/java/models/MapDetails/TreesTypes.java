package models.MapDetails;

import models.Fundementals.Location;

public enum TreesTypes {
    PINE(null, 10),
    APPLETREE(null, 20);
    //TODO:etelaat dige
    private final Location location;

    private final int watering;

    TreesTypes(Location location, int watering) {
        this.location = location;
        this.watering = watering;
    }

    public Location getLocation() {
        return location;
    }

    public int getWatering() {
        return watering;
    }
}
