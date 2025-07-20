package org.example.Common.models.Eating;

import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.enums.Season;

public class Fruits extends Item {
    private String fruitName;
    private String type;
    private String origin;
    private int stage;
    private int fullGrowthtime;
    private boolean isCollected;
    private boolean canCollect;
    private int collectDay;
    private double price;
    private boolean isEdible;
    private int energyGain;
    private Season season;
    private boolean giganticGrowth;
    public Fruits(String fruitName, String type, String origin,
                  int fullGrowthtime, boolean canCollect, boolean isCollected,
                  int collectDay, double price, boolean isEdible, int energyGain,
                  Season season, boolean giganticGrowth) {
        super(fruitName, Quality.NORMAL, (int) price);
        this.fruitName = fruitName;
        this.type = type;
        this.origin = origin;
        this.stage = 0;
        this.fullGrowthtime = fullGrowthtime;
        this.isCollected = isCollected;
        this.canCollect = canCollect;
        this.collectDay = collectDay;
        this.price = price;
        this.isEdible = isEdible;
        this.energyGain = energyGain;
        this.season = season;
        this.giganticGrowth = giganticGrowth;
    }

    public String getFruitName() {
        return fruitName;
    }

    public void setFruitName(String fruitName) {
        this.fruitName = fruitName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getFullGrowthtime() {
        return fullGrowthtime;
    }

    public void setFullGrowthtime(int fullGrowthtime) {
        this.fullGrowthtime = fullGrowthtime;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public boolean isCanCollect() {
        return canCollect;
    }

    public void setCanCollect(boolean canCollect) {
        this.canCollect = canCollect;
    }

    public int getCollectDay() {
        return collectDay;
    }

    public void setCollectDay(int collectDay) {
        this.collectDay = collectDay;
    }

    public int getPrice() {
        return (int) price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isEdible() {
        return isEdible;
    }

    public void setEdible(boolean edible) {
        isEdible = edible;
    }

    public int getEnergyGain() {
        return energyGain;
    }

    public void setEnergyGain(int energyGain) {
        this.energyGain = energyGain;
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public boolean isGiganticGrowth() {
        return giganticGrowth;
    }

    public void setGiganticGrowth(boolean giganticGrowth) {
        this.giganticGrowth = giganticGrowth;
    }
}
