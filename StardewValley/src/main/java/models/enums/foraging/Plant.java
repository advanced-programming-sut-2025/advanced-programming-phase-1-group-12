package models.enums.foraging;

import models.Fundementals.Location;

public class Plant {
    private Location location;
    private Seed seed;
    private boolean hasBeenFertilized ;
    private int dayPast ;
    private int currentStage ;
    private int age;
    private boolean isForaging;

    public Plant(Location location, Seed seed, boolean isForaging){
        this.location = location;
        this.seed = seed;
        hasBeenFertilized = false;
        dayPast = 2;
        currentStage = 0;
        age = 0;
        this.isForaging = isForaging;
    }

    public boolean isHasBeenFertilized() {
        return hasBeenFertilized;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getAge() {
        return age;
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

    public void setSeed(Seed seed) {
        this.seed = seed;
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

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public void setDayPast(int dayPast) {
        this.dayPast = dayPast;
    }

    public void setHasBeenFertilized(boolean hasBeenFertilized) {
        this.hasBeenFertilized = hasBeenFertilized;
    }

}
