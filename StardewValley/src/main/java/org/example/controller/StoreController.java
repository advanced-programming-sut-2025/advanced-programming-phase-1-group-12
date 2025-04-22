package controller;

import models.Fundementals.Location;
import models.*;
import models.Place.Store;
import models.ProductsPackage.StoreProducts;

import java.util.List;

public class StoreController {
    public Store findStore(Location location){return null;}

    public boolean isStoreOpen(Store store, Date date){return false;}

    public void ShowProducts(Store store){}

    public void showTotalProducts(List<Store> stores){}

    public void buyProduct(String productName, int Count){}

    public StoreProducts getStoreProducts(String productName){return new StoreProducts();}
    public void addCount(){}


}
