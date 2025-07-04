package org.example.models.MapDetails;

import org.example.models.Fundementals.LocationOfRectangle;
import org.example.models.Place.Place;

public class Quarry implements Place {
    LocationOfRectangle quarryLocation;

    public Quarry(LocationOfRectangle quarryLocation){
        this.quarryLocation = quarryLocation;
    }

    public void setQuarryLocation(LocationOfRectangle quarryLocation) {
        this.quarryLocation = quarryLocation;
    }

    @Override
    public LocationOfRectangle getLocation() {
        return this.quarryLocation;
    }

}
