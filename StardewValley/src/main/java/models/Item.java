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
}
