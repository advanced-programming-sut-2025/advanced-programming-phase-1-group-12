package models.enums.foraging;

import models.Fundementals.Location;
import models.enums.Types.FruitType;
import models.enums.Types.SaplingTypes;
import models.enums.Types.TreeType;

public class Tree {
    private TreeType type;
    private boolean hasBeenFertilized;
    private SaplingTypes saplingTypes;
    private Location location;
    private boolean hasBeenWatering;
    private int totalTimeNeeded;
    private int dayPast;
    private int currentStage;
    private int age;
    private boolean canPickUp;
    private boolean isForaging;
    private FruitType fruitType;

    public Tree(Location location, TreeType type, boolean isForaging, FruitType fruitType) {
        this.type = type;
        this.canPickUp = false;
        this.location = location;
        this.hasBeenFertilized = false;
        this.age = 0;
        this.hasBeenWatering = false;
        this.totalTimeNeeded = type.totalHarvestTime;
        this.saplingTypes = type.seedSource;
        this.dayPast = 2;
        this.currentStage = 0;
        this.isForaging = isForaging;
        this.fruitType = fruitType;
    }

    public TreeType getType() {
        return type;
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }

    public boolean isCanPickUp() {
        return canPickUp;
    }

    public void setCanPickUp(boolean canPickUp) {
        this.canPickUp = canPickUp;
    }

    public void setHasBeenWatering(boolean hasBeenWatering) {
        this.hasBeenWatering = hasBeenWatering;
    }

    public void setTotalTimeNeeded(int totalTimeNeeded) {
        this.totalTimeNeeded = totalTimeNeeded;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public boolean isHasBeenWatering() {
        return hasBeenWatering;
    }

    public int getTotalTimeNeeded() {
        return totalTimeNeeded;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public int getDayPast() {
        return dayPast;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setDayPast(int dayPast) {
        this.dayPast = dayPast;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isForaging() {
        return isForaging;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setForaging(boolean foraging) {
        isForaging = foraging;
    }

    public SaplingTypes getSaplingTypes() {
        return saplingTypes;
    }

    public void setSaplingTypes(SaplingTypes saplingTypes) {
        this.saplingTypes = saplingTypes;
    }

    public void setType(TreeType type) {
        this.type = type;
    }

    public boolean isHasBeenFertilized() {
        return hasBeenFertilized;
    }

    public void setHasBeenFertilized(boolean hasBeenFertilized) {
        this.hasBeenFertilized = hasBeenFertilized;
    }
}