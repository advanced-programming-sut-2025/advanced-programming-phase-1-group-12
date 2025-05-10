package models.enums.foraging;

import models.Fundementals.Location;
import models.enums.Types.SeedTypes;

public class Seed {

    private SeedTypes type;
    private Location location;

    public Seed(SeedTypes type) {
        this.type = type;
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

}
