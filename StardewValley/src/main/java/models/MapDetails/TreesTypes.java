package models.MapDetails;

import models.Fundementals.Location;
import models.ProductsPackage.ProductTypes;

public enum TreesTypes implements ProductTypes {
    //TODO:sapling watering be added
    PINE(10),

    ApricotSapling(0),

    CherrySapling(1),

    AppleSapling(2),

    OrangeSapling(3),

    PeachSapling(4),

    PomegranateSapling(5),

    APPLETREE(20);
    //TODO:etelaat dige

    private final int watering;

    TreesTypes(int watering) {
        this.watering = watering;
    }
    public int getWatering() {
        return watering;
    }
}
