package models.Place;

import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.MapDetails.*;
import models.RelatedToUser.User;

public class Farm implements Place {

    private  LocationOfRectangle farmLocation;

    private User owner;
    private Lake lake1;
    private Lake lake2;
    private GreenHouse greenHouse;
    private Shack shack;
    private Quarry quarry;

    public Farm(LocationOfRectangle farmLocation, User owner) {
        //TODO:location of rest will be set similarly
        this.farmLocation = farmLocation;
        this.owner = owner;
        this.lake1 = new Lake(new LocationOfRectangle(new Location(farmLocation.getDownRightCorner().getxAxis()-5,
                farmLocation.getDownRightCorner().getyAxis()-5), farmLocation.getDownRightCorner()));
        this.lake2 =  new Lake(new LocationOfRectangle(new Location(farmLocation.getTopLeftCorner().getxAxis()+5,
                farmLocation.getTopLeftCorner().getyAxis()+5), farmLocation.getTopLeftCorner()));
        this.greenHouse = new GreenHouse();
        this.shack = new Shack();
        this.quarry = new Quarry();
    }

    public LocationOfRectangle getFarmLocation() {
        return farmLocation;
    }

    public void setFarmLocation(LocationOfRectangle farmLocation) {
        this.farmLocation = farmLocation;
    }

    public GreenHouse getGreenHouse() {
        return greenHouse;
    }

    public Lake getLake1() {
        return lake1;
    }

    public Lake getLake2() {
        return lake2;
    }

    public Quarry getQuarry() {
        return quarry;
    }

    public Shack getShack() {
        return shack;
    }

    public User getOwner() {
        return owner;
    }

    public void setGreenHouse(GreenHouse greenHouse) {
        this.greenHouse = greenHouse;
    }

    public void setLake1(Lake lake1) {
        this.lake1 = lake1;
    }

    public void setLake2(Lake lake2) {
        this.lake2 = lake2;
    }

    @Override
    public LocationOfRectangle locationOfRectangle() {
        return null;
    }


}
