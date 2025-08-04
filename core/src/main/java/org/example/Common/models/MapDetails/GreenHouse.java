package org.example.Common.models.MapDetails;

import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Place.Place;

import java.io.Serializable;

public class GreenHouse implements Place, Serializable {
    LocationOfRectangle location;

    public GreenHouse(LocationOfRectangle GreenHouseLocation) {
        this.location = GreenHouseLocation;
    }

    public LocationOfRectangle getGreenHouseLocation() {
        return location;
    }

    public void setGreenHouseLocation(LocationOfRectangle greenHouseLocation) {
        this.location = greenHouseLocation;
    }

    public GreenHouse() {
    }

    @Override
    public LocationOfRectangle getLocation() {
        return this.location;
    }
}
