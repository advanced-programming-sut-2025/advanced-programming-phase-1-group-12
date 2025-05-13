package models;

import models.ProductsPackage.Quality;

public class Item {
    private String name;
    private Quality quality;
    private int price;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Quality getQuality() {
        return quality;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
