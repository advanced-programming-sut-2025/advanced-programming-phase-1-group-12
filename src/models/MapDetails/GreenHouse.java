package models.MapDetails;

import models.Fundementals.LocationOfRectangle;
import models.Place.Place;

public class GreenHouse implements Place {
    LocationOfRectangle greenHouseLocation;

    public LocationOfRectangle getGreenHouseLocation() {
        return greenHouseLocation;
    }
    //TODO:set it to 5 * 6 instead of this one
    public void setGreenHouseLocation(LocationOfRectangle greenHouseLocation) {
        this.greenHouseLocation = greenHouseLocation;
    }


    @Override
    public LocationOfRectangle locationOfRectangle() {
        return null;
    }
}
