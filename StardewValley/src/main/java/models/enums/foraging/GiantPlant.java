package models.enums.foraging;

import models.Fundementals.Location;
import models.Item;
import models.ProductsPackage.Quality;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GiantPlant extends Item {

    private GiantPlants giantPlants;
    private ArrayList<Location> location;
    private boolean hasBeenFertilized;
    private boolean hasBeenWatering;
    private int totalTimeNeeded;
    private int dayPast;
    private int currentStage ;
    private int age;

    public GiantPlant(GiantPlants giantPlants, ArrayList<Location> location, boolean hasBeenFertilized,
                      boolean hasBeenWatering, int totalTimeNeeded, int dayPast, int currentStage, int age){
        super(giantPlants.name(), Quality.NORMAL, giantPlants.baseSellPrice);
        this.giantPlants = giantPlants;
        this.location = location;
        this.hasBeenFertilized = hasBeenFertilized;
        this.hasBeenWatering = hasBeenWatering;
        this.totalTimeNeeded = totalTimeNeeded;
        this.dayPast = dayPast;
        this.currentStage = currentStage;
        this.age = age;
    }

    public void setLocation(ArrayList<Location> location) {
        this.location = location;
    }

    public ArrayList<Location> getLocation() {
        return location;
    }

    public void setDayPast(int dayPast) {
        this.dayPast = dayPast;
    }

    public int getDayPast() {
        return dayPast;
    }

    public int getTotalTimeNeeded() {
        return totalTimeNeeded;
    }

    public int getAge() {
        return age;
    }

    public boolean isHasBeenWatering() {
        return hasBeenWatering;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public void setTotalTimeNeeded(int totalTimeNeeded) {
        this.totalTimeNeeded = totalTimeNeeded;
    }

    public void setHasBeenWatering(boolean hasBeenWatering) {
        this.hasBeenWatering = hasBeenWatering;
    }

    public GiantPlants getGiantPlants() {
        return giantPlants;
    }

    public void setHasBeenFertilized(boolean hasBeenFertilized) {
        this.hasBeenFertilized = hasBeenFertilized;
    }

    public boolean isHasBeenFertilized() {
        return hasBeenFertilized;
    }

    public void setGiantPlants(GiantPlants giantPlants) {
        this.giantPlants = giantPlants;
    }

}
