package org.example.Common.models.MapDetails;

import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Place.Place;

public class Lake implements Place {
     private final LocationOfRectangle lakeLocation;

     public Lake(LocationOfRectangle lakeLocation) {
          this.lakeLocation = lakeLocation;
     }



     @Override
     public LocationOfRectangle getLocation() {
          return this.lakeLocation;
     }
}
