package models.MapDetails;

import models.Fundementals.Location;
import models.Place.Place;

public class Shack implements Place {
    Location.LocationOfRectangle shackLocation;

    public Location.LocationOfRectangle getShackLocation() {
        return shackLocation;
    }

    public void setShackLocation(Location.LocationOfRectangle shackLocation) {
        this.shackLocation = shackLocation;
    }
}
