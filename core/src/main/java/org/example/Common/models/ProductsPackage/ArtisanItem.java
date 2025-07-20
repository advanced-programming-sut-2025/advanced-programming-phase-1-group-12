package org.example.Common.models.ProductsPackage;

import org.example.Common.models.Item;
import org.example.Common.models.enums.Types.ArtisanTypes;

public class ArtisanItem extends Item {
    private ArtisanTypes type;

    private int hoursRemained;


    private int energy;

    public ArtisanItem(String name, ArtisanTypes type, int hoursRemained, int energy) {
        super(name, Quality.NORMAL, 0);
        this.type = type;
        this.hoursRemained = hoursRemained;
        this.energy = energy;
    }

    public ArtisanTypes getType() {
        return type;
    }

    public void setType(ArtisanTypes type) {
        this.type = type;
    }

    public int getHoursRemained() {
        return hoursRemained;
    }

    public void setHoursRemained(int hoursRemained) {
        this.hoursRemained = hoursRemained;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
