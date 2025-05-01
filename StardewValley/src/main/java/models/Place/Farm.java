package models.Place;

import models.Animal.FarmAnimals;
import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Fundementals.Player;
import models.MapDetails.*;
import models.RelatedToUser.User;

import java.util.ArrayList;

public class Farm implements Place {

    private LocationOfRectangle farmLocation;

    private Player owner;
    private Lake lake1;
    private Lake lake2;
    private GreenHouse greenHouse;
    private Shack shack;
    private Quarry quarry;
    private ArrayList<FarmAnimals> farmAnimals = new ArrayList<>();

    public Farm(LocationOfRectangle farmLocation) {
        this.farmLocation = farmLocation;
        int sectionSize = 4;

        // Top-Left Lake
        Location lakeTopLeftCorner = farmLocation.getTopLeftCorner();
        Location lakeBottomRightCorner = new Location(
                lakeTopLeftCorner.getxAxis() + sectionSize ,
                lakeTopLeftCorner.getyAxis() + sectionSize
        );
        this.lake1 = new Lake(new LocationOfRectangle(lakeTopLeftCorner, lakeBottomRightCorner));
        // Bottom-Left Lake
        Location bottomLeftCorner = new Location(
                farmLocation.getTopLeftCorner().getxAxis() ,
                farmLocation.getDownRightCorner().getyAxis() - sectionSize
        );
        Location bottomLeftLakeDownRight = new Location(
                bottomLeftCorner.getxAxis() + sectionSize ,
                bottomLeftCorner.getyAxis() + sectionSize
        );
        this.lake2 = new Lake(new LocationOfRectangle(bottomLeftCorner, bottomLeftLakeDownRight));

        // Top-Right Shack
        Location topRightCorner = new Location(
                farmLocation.getDownRightCorner().getxAxis() - sectionSize ,
                farmLocation.getTopLeftCorner().getyAxis()
        );
        Location shackDownRight = new Location(
                topRightCorner.getxAxis() + sectionSize ,
                topRightCorner.getyAxis() + sectionSize
        );
        this.shack = new Shack(new LocationOfRectangle(topRightCorner, shackDownRight));

        // Bottom-Right Greenhouse
        Location bottomRightCorner = new Location(
                farmLocation.getDownRightCorner().getxAxis() - sectionSize ,
                farmLocation.getDownRightCorner().getyAxis() - sectionSize
        );
        Location greenhouseTopLeft = new Location(
                bottomRightCorner.getxAxis(),
                bottomRightCorner.getyAxis()
        );
        Location greenhouseDownRight = new Location(
                greenhouseTopLeft.getxAxis() + sectionSize ,
                greenhouseTopLeft.getyAxis() + sectionSize
        );
        this.greenHouse = new GreenHouse(new LocationOfRectangle(greenhouseTopLeft, greenhouseDownRight));

        // Quarry in Center
        int centerX = (farmLocation.getTopLeftCorner().getxAxis() + farmLocation.getDownRightCorner().getxAxis()) / 2;
        int centerY = (farmLocation.getTopLeftCorner().getyAxis() + farmLocation.getDownRightCorner().getyAxis()) / 2;

        Location quarryTopLeft = new Location(
                centerX - (sectionSize / 2),
                centerY - (sectionSize / 2)
        );
        Location quarryDownRight = new Location(
                quarryTopLeft.getxAxis() + sectionSize ,
                quarryTopLeft.getyAxis() + sectionSize
        );
        this.quarry = new Quarry(new LocationOfRectangle(quarryTopLeft, quarryDownRight));
    }


    public LocationOfRectangle getLocation() {
        return farmLocation;
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

    public Player getOwner() {
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

    public void setQuarry(Quarry quarry) {
        this.quarry = quarry;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    public ArrayList<FarmAnimals> getFarmAnimals() {
        return farmAnimals;
    }

    public void setFarmAnimals(ArrayList<FarmAnimals> farmAnimals) {
        this.farmAnimals = farmAnimals;
    }
}
