package org.example.Common.models.MapDetails;

import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Place.Place;

import java.io.Serializable;

public class Lake implements Place, Serializable {
    LocationOfRectangle location;

     public Lake(LocationOfRectangle lakeLocation) {
          this.location = lakeLocation;
     }

    public Lake() {
    }

    public void setLocation(LocationOfRectangle location) {
        this.location = location;
    }

    @Override
     public LocationOfRectangle getLocation() {
          return this.location;
     }
}
