package org.example.models.MapDetails;

import org.example.models.Fundementals.LocationOfRectangle;
import org.example.models.Place.Place;

public class GreenHouse implements Place {
    LocationOfRectangle greenHouseLocation;

    public GreenHouse(LocationOfRectangle GreenHouseLocation) {
        this.greenHouseLocation = GreenHouseLocation;
    }

    public LocationOfRectangle getGreenHouseLocation() {
        return greenHouseLocation;
    }

    public void setGreenHouseLocation(LocationOfRectangle greenHouseLocation) {
        this.greenHouseLocation = greenHouseLocation;
    }

    @Override
    public LocationOfRectangle getLocation() {
        return this.greenHouseLocation;
    }
}
