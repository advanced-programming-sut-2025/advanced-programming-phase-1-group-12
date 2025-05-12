package models.ProductsPackage;

import models.Item;
import models.enums.Types.ArtisanTypes;

public class ArtisanItem extends Item {
    private ArtisanTypes type;

    private int hoursRemained;

    private int price;

    private int energy;

    public ArtisanItem(String name, ArtisanTypes type, int hoursRemained, int price, int energy) {
        super(name);
        this.type = type;
        this.hoursRemained = type.getProcessingTime();
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
