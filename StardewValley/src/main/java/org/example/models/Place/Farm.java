package models.Place;

import models.Fundementals.Location;
import models.MapDetails.*;
import models.RelatedToUser.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Farm implements Place {

    private  Location.LocationOfRectangle farmLocation;

    private User owner;
    private Lake lake1;
    private Lake lake2;
    private GreenHouse greenHouse;
    private Shack shack;
    private Quarry quarry;

    public Farm(Location.LocationOfRectangle farmLocation, User owner, Lake lake1, Lake lake2, GreenHouse greenHouse,
                Shack shack, Quarry quarry) {
        //TODO:location of rest will be set similarly
        this.farmLocation = farmLocation;
        this.owner = owner;
        this.lake1 = new Lake(new Location.LocationOfRectangle(new Location(farmLocation.getDownRightCorner().getxAxis()-5,
                farmLocation.getDownRightCorner().getyAxis()-5), farmLocation.getDownRightCorner()));
        this.lake2 =  new Lake(new Location.LocationOfRectangle(new Location(farmLocation.getTopLeftCorner().getxAxis()+5,
                farmLocation.getTopLeftCorner().getyAxis()+5), farmLocation.getTopLeftCorner()));
        this.greenHouse = greenHouse;
        this.shack = shack;
        this.quarry = quarry;
    }

    public Location.LocationOfRectangle getFarmLocation() {
        return farmLocation;
    }

    public void setFarmLocation(Location.LocationOfRectangle farmLocation) {
        this.farmLocation = farmLocation;
    }

    @Override
    public Location.LocationOfRectangle locationOfRectangle() {
        return null;
    }


}
