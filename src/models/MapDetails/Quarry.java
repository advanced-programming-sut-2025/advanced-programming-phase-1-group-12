package models.MapDetails;

import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Place.Place;

public class Quarry implements Place {
    LocationOfRectangle quarryLocation;

    public LocationOfRectangle getQuarryLocation() {
        return quarryLocation;
    }

    public void setQuarryLocation(LocationOfRectangle quarryLocation) {
        this.quarryLocation = quarryLocation;
    }
}
