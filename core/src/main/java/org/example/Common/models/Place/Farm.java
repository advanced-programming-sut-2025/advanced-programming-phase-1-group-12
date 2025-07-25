package org.example.Common.models.Place;

import org.example.Common.models.Animal.AnimalHome;
import org.example.Common.models.Animal.FarmAnimals;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.MapDetails.GreenHouse;
import org.example.Common.models.MapDetails.Lake;
import org.example.Common.models.MapDetails.Quarry;
import org.example.Common.models.MapDetails.Shack;
import org.example.Common.models.enums.foraging.GiantPlant;
import org.example.Common.models.enums.foraging.Plant;

import java.util.ArrayList;

public class Farm implements Place {

    private LocationOfRectangle farmLocation;

    private Player owner;
    private int farmID;
    private Lake lake1;
    private Lake lake2;
    private GreenHouse greenHouse;
    private GreenHouse greenHouse2;
    private Shack shack;
    private Shack shack2;
    private Quarry quarry;
    private Quarry quarry2;
    private ArrayList<FarmAnimals> farmAnimals = new ArrayList<>();
    private ArrayList<AnimalHome> animalHomes = new ArrayList<>();
    private ArrayList<Plant> PlantOfFarm = new ArrayList<>();
    private ArrayList<GiantPlant> giantPlants = new ArrayList<>();


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

        Location bottomLeftCornerShack2 = new Location(
                farmLocation.getTopLeftCorner().getxAxis() ,
                farmLocation.getDownRightCorner().getyAxis() - sectionSize
        );
        Location bottomLeftLakeDownRightShack2 = new Location(
                bottomLeftCornerShack2.getxAxis() + sectionSize ,
                bottomLeftCornerShack2.getyAxis() + sectionSize
        );
        this.shack2 = new Shack(new LocationOfRectangle(bottomLeftCornerShack2, bottomLeftLakeDownRightShack2));

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
        Location bottomLeftCornerGreenHouse2 = new Location(
                farmLocation.getTopLeftCorner().getxAxis() ,
                farmLocation.getDownRightCorner().getyAxis() - sectionSize
        );
        Location bottomLeftLakeDownRightGreenHouse2 = new Location(
                bottomLeftCornerGreenHouse2.getxAxis() + sectionSize ,
                bottomLeftCornerGreenHouse2.getyAxis() + sectionSize
        );
        this.greenHouse2 = new GreenHouse(new LocationOfRectangle(bottomLeftCornerGreenHouse2, bottomLeftLakeDownRightGreenHouse2));

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

        Location bottomLeftCornerGreenQuarry2 = new Location(
                farmLocation.getTopLeftCorner().getxAxis() ,
                farmLocation.getDownRightCorner().getyAxis() - sectionSize
        );
        Location bottomLeftLakeDownRightQuarry2 = new Location(
                bottomLeftCornerGreenQuarry2.getxAxis() + sectionSize ,
                bottomLeftCornerGreenQuarry2.getyAxis() + sectionSize
        );
        this.quarry2 = new Quarry(new LocationOfRectangle(bottomLeftCornerGreenQuarry2, bottomLeftLakeDownRightQuarry2));
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

    public LocationOfRectangle getFarmLocation() {
        return farmLocation;
    }

    public GreenHouse getGreenHouse2() {
        return greenHouse2;
    }

    public Quarry getQuarry2() {
        return quarry2;
    }

    public void setFarmLocation(LocationOfRectangle farmLocation) {
        this.farmLocation = farmLocation;
    }

    public Shack getShack2() {
        return shack2;
    }

    public void setGreenHouse2(GreenHouse greenHouse2) {
        this.greenHouse2 = greenHouse2;
    }

    public void setQuarry2(Quarry quarry2) {
        this.quarry2 = quarry2;
    }

    public void setShack(Shack shack) {
        this.shack = shack;
    }

    public void setShack2(Shack shack2) {
        this.shack2 = shack2;
    }

    public ArrayList<AnimalHome> getAnimalHomes() {
        return animalHomes;
    }

    public void setAnimalHomes(ArrayList<AnimalHome> animalHomes) {
        this.animalHomes = animalHomes;
    }

    public ArrayList<Plant> getPlantOfFarm() {
        return this.PlantOfFarm;
    }

    public void setPlantOfFarm(ArrayList<Plant> plantOfFarm) {
        PlantOfFarm = plantOfFarm;
    }


    public void setGiantPlants(ArrayList<GiantPlant> giantPlants) {
        this.giantPlants = giantPlants;
    }

    public ArrayList<GiantPlant> getGiantPlants() {
        return giantPlants;
    }

    public boolean contains(int newX, int newY) {
        Location newLocation = App.getCurrentGame().getMainMap().findLocation(newX, newY);
        ArrayList<Location> farmLocations = getFarmLocation().getLocationsInRectangle();
        return !farmLocations.contains(newLocation);
    }

    public void setFarmID(int farmID) {
        this.farmID = farmID;
    }

    public int getFarmId() {
        return this.farmID;
    }
}
