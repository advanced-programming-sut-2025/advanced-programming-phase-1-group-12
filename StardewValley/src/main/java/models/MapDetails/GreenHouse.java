package models.MapDetails;

import models.Fundementals.LocationOfRectangle;
import models.Place.Place;

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
    public LocationOfRectangle locationOfRectangle() {
        return null;
    }
}
