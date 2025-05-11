package models;

import models.Eating.Food;

import java.util.ArrayList;

public class Refrigrator {
    private ArrayList<Item> products;
    private ArrayList<Food> foodMade;
    private int maxProduction;

    public ArrayList<Item> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Item> products) {
        this.products = products;
    }

    public ArrayList<Food> getFoodMade() {
        return foodMade;
    }

    public void setFoodMade(ArrayList<Food> foodMade) {
        this.foodMade = foodMade;
    }

    public void addProducts(Item product1){
        products.add(product1);
    }

    public void addFoodMade(Food food){
        foodMade.add(food);
    }

    public void setMaxProduction(int maxProduction) {
        this.maxProduction = maxProduction;
    }

    public int getMaxProduction() {
        return maxProduction;
    }
}
