package models.MapDetails;

import models.Fundementals.LocationOfRectangle;
import models.Place.Place;

public class Shack implements Place {
    LocationOfRectangle shackLocation;

    public Shack(LocationOfRectangle quarryLocation){
        this.shackLocation = quarryLocation;
    }

    public void setShackLocation(LocationOfRectangle quarryLocation) {
        this.shackLocation = quarryLocation;
    }

    @Override
    public LocationOfRectangle getLocation() {
        return this.shackLocation;
    }

}
