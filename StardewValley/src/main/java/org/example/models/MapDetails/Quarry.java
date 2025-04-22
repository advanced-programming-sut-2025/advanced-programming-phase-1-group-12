package models.MapDetails;

import models.Fundementals.Location;
import models.Place.Place;

public class Quarry implements Place {
    Location.LocationOfRectangle quarryLocation;

    public Location.LocationOfRectangle getQuarryLocation() {
        return quarryLocation;
    }

    public void setQuarryLocation(Location.LocationOfRectangle quarryLocation) {
        this.quarryLocation = quarryLocation;
    }
}
