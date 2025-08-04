package org.example.Common.models.MapDetails;

import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Place.Place;

import java.io.Serializable;

public class Quarry implements Place, Serializable {
    LocationOfRectangle location;

    public Quarry(LocationOfRectangle quarryLocation){
        this.location = quarryLocation;
    }

    public Quarry() {
    }

    public void setLocation(LocationOfRectangle location) {
        this.location = location;
    }

    @Override
    public LocationOfRectangle getLocation() {
        return this.location;
    }

}
