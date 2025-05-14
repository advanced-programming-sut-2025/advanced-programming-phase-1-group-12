package models;

import models.ProductsPackage.Quality;

import java.util.Objects;

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
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(name, item.name) && quality == item.quality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quality);
    }

}
