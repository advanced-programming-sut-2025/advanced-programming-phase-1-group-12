package org.example.models.MapDetails;

import org.example.models.Fundementals.LocationOfRectangle;
import org.example.models.Place.Place;

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
