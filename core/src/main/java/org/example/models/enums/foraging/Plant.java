package org.example.models.enums.foraging;

import org.example.models.Fundementals.Location;
import org.example.models.Item;
import org.example.models.ProductsPackage.Quality;

public class Plant extends Item {
    private Location location;
    private Seed seed;
    private AllCrops allCrops;
    private boolean hasBeenFertilized;
    private boolean hasBeenWatering;
    private int totalTimeNeeded;
    private int dayPast;
    private int currentStage ;
    private int age;
    private boolean isForaging;
    private boolean isOneTime;
    private int regrowthTime;
    private boolean isGiantPlant;

    public Plant(Location location, Seed seed, boolean isForaging, AllCrops allCrops){
        super(seed.getName(), Quality.NORMAL, seed.getPrice());
        this.location = location;
        this.seed = seed;
        this.hasBeenFertilized = false;
        this.dayPast = 2;
        this.currentStage = 0;
        this.age = 0;
        this.isOneTime = true;
        this.isForaging = isForaging;
        this.totalTimeNeeded = seed.getType().getDay();
        this.allCrops = allCrops;
        this.hasBeenWatering = false;
        this.regrowthTime = 0;
//        this.quallity = ....;
    }

    public boolean isOneTime() {
        return isOneTime;
    }

    public void setOneTime(boolean oneTime) {
        isOneTime = oneTime;
    }

    public int getRegrowthTime() {
        return regrowthTime;
    }

    public void setRegrowthTime(int regrowthTime) {
        this.regrowthTime = regrowthTime;
    }

    public boolean isGiantPlant() {
        return isGiantPlant;
    }

    public void setGiantPlant(boolean giantPlant) {
        isGiantPlant = giantPlant;
    }

    public boolean isHasBeenFertilized() {
        return hasBeenFertilized;
    }

    public int getTotalTimeNeeded() {
        return totalTimeNeeded;
    }

    public void setTotalTimeNeeded(int totalTimeNeeded) {
        this.totalTimeNeeded = totalTimeNeeded;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public AllCrops getAllCrops() {
        return allCrops;
    }

    public void setAllCrops(AllCrops allCrops) {
        this.allCrops = allCrops;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isForaging() {
        return isForaging;
    }

    public Seed getSeed() {
        return seed;
    }

    public void setForaging(boolean foraging) {
        isForaging = foraging;
    }

    public boolean isHasBeenWatering() {
        return hasBeenWatering;
    }

    public void setHasBeenWatering(boolean hasBeenWatering) {
        this.hasBeenWatering = hasBeenWatering;
    }

    public void setSeed(Seed seed) {
        this.seed = seed;
    }

    public int getDayPast() {
        return dayPast;
    }

    public void setDayPast(int dayPast) {
        this.dayPast = dayPast;
    }

    public void setHasBeenFertilized(boolean hasBeenFertilized) {
        this.hasBeenFertilized = hasBeenFertilized;
    }

    public void decreaseDayPast() {
        dayPast--;
    }

}
