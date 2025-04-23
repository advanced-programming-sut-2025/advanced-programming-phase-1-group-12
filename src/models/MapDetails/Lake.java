package models.MapDetails;

import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Place.Place;

public class Lake implements Place {
     private final LocationOfRectangle lakeLocation;

     public Lake(LocationOfRectangle lakeLocation) {
          this.lakeLocation = lakeLocation;
     }

     @Override
     public LocationOfRectangle locationOfRectangle() {
          return null;
     }


}
