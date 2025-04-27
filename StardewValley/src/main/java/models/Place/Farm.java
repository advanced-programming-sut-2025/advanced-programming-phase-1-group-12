package models.Place;

import models.Animal.FarmAnimals;
import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Fundementals.Player;
import models.MapDetails.GreenHouse;
import models.MapDetails.Lake;
import models.MapDetails.Quarry;
import models.MapDetails.Shack;

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

    public Farm(LocationOfRectangle farmLocation, Player owner) {
        this.farmLocation = farmLocation;
        this.owner = owner;

        int sectionSize = 5;

        Location lake2TopLeft = farmLocation.getTopLeftCorner();
        Location lake2DownRight = new Location(lake2TopLeft.getxAxis() + sectionSize - 1, lake2TopLeft.getyAxis() + sectionSize - 1);
        this.lake2 = new Lake(new LocationOfRectangle(lake2TopLeft, lake2DownRight));

        Location lake1DownRight = farmLocation.getDownRightCorner();
        Location lake1TopLeft = new Location(lake1DownRight.getxAxis() - sectionSize - 1, lake1DownRight.getyAxis() - sectionSize - 1);
        this.lake1 = new Lake(new LocationOfRectangle(lake1TopLeft, lake1DownRight));

        Location greenhouseBottomLeft = new Location(farmLocation.getTopLeftCorner().getxAxis(), farmLocation.getDownRightCorner().getyAxis() - sectionSize);
        Location greenhouseTopRight = new Location(greenhouseBottomLeft.getxAxis() + sectionSize, greenhouseBottomLeft.getyAxis() + sectionSize);
        this.greenHouse = new GreenHouse(new LocationOfRectangle(greenhouseBottomLeft, greenhouseTopRight));

        Location shackTopRight = new Location(farmLocation.getDownRightCorner().getxAxis(), farmLocation.getTopLeftCorner().getyAxis());
        Location shackBottomLeft = new Location(shackTopRight.getxAxis() - sectionSize, shackTopRight.getyAxis() + sectionSize);
        this.shack = new Shack(new LocationOfRectangle(shackBottomLeft, shackTopRight));

        int centerX = (farmLocation.getTopLeftCorner().getxAxis() + farmLocation.getDownRightCorner().getxAxis()) / 2;
        int centerY = (farmLocation.getTopLeftCorner().getyAxis() + farmLocation.getDownRightCorner().getyAxis()) / 2;
        Location quarryTopLeft = new Location(centerX - sectionSize / 2, centerY - sectionSize / 2);
        Location quarryDownRight = new Location(centerX + sectionSize / 2, centerY + sectionSize / 2);
        this.quarry = new Quarry(new LocationOfRectangle(quarryTopLeft, quarryDownRight));
    }

    public LocationOfRectangle getFarmLocation() {
        return farmLocation;
    }

    public Player getOwner() {
        return owner;
    }

    public Lake getLake1() {
        return lake1;
    }

    public Lake getLake2() {
        return lake2;
    }

    public GreenHouse getGreenHouse() {
        return greenHouse;
    }

    public Shack getShack() {
        return shack;
    }

    public Quarry getQuarry() {
        return quarry;
    }

    public ArrayList<FarmAnimals> getFarmAnimals() {
        return farmAnimals;
    }

    public void setFarmAnimals(ArrayList<FarmAnimals> farmAnimals) {
        this.farmAnimals = farmAnimals;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    @Override
    public LocationOfRectangle locationOfRectangle() {
        return farmLocation;
    }
}
