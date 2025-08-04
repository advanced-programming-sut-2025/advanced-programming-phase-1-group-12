package org.example.Common.models.MapDetails;

import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Place.Place;

import java.io.Serializable;

public class Shack implements Place, Serializable {
    LocationOfRectangle location;

    public Shack(LocationOfRectangle quarryLocation){
        this.location = quarryLocation;
    }

    public void setLocation(LocationOfRectangle quarryLocation) {
        this.location = quarryLocation;
    }

    @Override
    public LocationOfRectangle getLocation() {
        return this.location;
    }

    public Shack() {
    }
}
