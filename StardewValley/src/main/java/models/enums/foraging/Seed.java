package models.enums.foraging;

import models.enums.Types.SeedTypes;

public class Seed {
    SeedTypes type;
    int x;
    int y;
    SeedSeason seedSeason;

    public Seed(SeedTypes type, SeedSeason seedSeason) {
        this.type = type;
        this.seedSeason = seedSeason;
    }

    public void setType(SeedTypes type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public SeedTypes getType() {
        return type;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public SeedSeason getSeedSeason() {
        return seedSeason;
    }

    public void setSeedSeason(SeedSeason seedSeason) {
        this.seedSeason = seedSeason;
    }
}
