package org.example.models.MapDetails;

import org.example.models.Fundementals.LocationOfRectangle;
import org.example.models.Place.Place;

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
