package models.enums.foraging;

import models.Fundementals.Location;
import models.Item;
import models.enums.Types.SeedTypes;

public class Seed extends Item {

    private SeedTypes type;
    private Location location;
    private int totalTimeNeeded;
    private boolean fertilized;

    public Seed(SeedTypes type) {
        super(type.name());
        this.type = type;
        this.fertilized = false;
        this.totalTimeNeeded = type.getDay();
    }

    public void setType(SeedTypes type) {
        this.type = type;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public SeedTypes getType() {
        return type;
    }

    public boolean isFertilized() {
        return fertilized;
    }

    public int getTotalTimeNeeded() {
        return totalTimeNeeded;
    }

    public void setFertilized(boolean fertilized) {
        this.fertilized = fertilized;
    }

    public void setTotalTimeNeeded(int totalTimeNeeded) {
        this.totalTimeNeeded = totalTimeNeeded;
    }
}