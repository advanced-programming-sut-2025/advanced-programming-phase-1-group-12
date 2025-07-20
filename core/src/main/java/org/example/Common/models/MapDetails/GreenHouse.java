package org.example.Common.models.MapDetails;

import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Place.Place;

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
