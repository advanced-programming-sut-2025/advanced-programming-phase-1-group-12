package models.MapDetails;

import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Place.Place;

public class Shack implements Place {
    LocationOfRectangle shackLocation;

    public LocationOfRectangle getShackLocation() {
        return shackLocation;
    }

    public void setShackLocation(LocationOfRectangle shackLocation) {
        this.shackLocation = shackLocation;
    }
}
