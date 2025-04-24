package models;

import models.Eating.Food;
import models.ProductsPackage.Products;

import java.util.ArrayList;

public class Refrigrator {
    private ArrayList<Products> products;
    private ArrayList<Food> foodMade;
    private int maxProduction;

    public ArrayList<Products> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Products> products) {
        this.products = products;
    }

    public ArrayList<Food> getFoodMade() {
        return foodMade;
    }

    public void setFoodMade(ArrayList<Food> foodMade) {
        this.foodMade = foodMade;
    }

    public void addProducts(Products product1){
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
