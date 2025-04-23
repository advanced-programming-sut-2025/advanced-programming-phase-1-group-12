package models.MapDetails;

import models.Fundementals.Location;
import models.Place.Place;

public class Lake implements Place {
     private final Location.LocationOfRectangle lakeLocation;

     public Lake(Location.LocationOfRectangle lakeLocation) {
          this.lakeLocation = lakeLocation;
     }

     @Override
     public Location.LocationOfRectangle locationOfRectangle() {
          return null;
     }


}
