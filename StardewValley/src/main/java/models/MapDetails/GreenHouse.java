package models.MapDetails;

import models.Fundementals.Location;
import models.Place.Place;

public class GreenHouse implements Place {
    Location.LocationOfRectangle greenHouseLocation;

    public Location.LocationOfRectangle getGreenHouseLocation() {
        return greenHouseLocation;
    }
    //TODO:set it to 5 * 6 instead of this one
    public void setGreenHouseLocation(Location.LocationOfRectangle greenHouseLocation) {
        this.greenHouseLocation = greenHouseLocation;
    }


}
