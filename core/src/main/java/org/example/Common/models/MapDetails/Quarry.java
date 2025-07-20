package org.example.Common.models.MapDetails;

import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Place.Place;

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
