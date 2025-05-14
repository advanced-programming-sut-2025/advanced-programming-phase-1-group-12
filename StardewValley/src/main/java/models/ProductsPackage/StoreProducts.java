package models.ProductsPackage;

import models.Item;
import models.ItemBuilder;
import models.enums.Types.StoreProductsTypes;

public class StoreProducts extends Item {
    private StoreProductsTypes type;

    private int currentDailyLimit;

    public StoreProductsTypes getType() {
        return type;
    }

    public void setType(StoreProductsTypes type) {
        this.type = type;
    }

    public StoreProducts(StoreProductsTypes type) {
        //TODO:change price
        super(type.name(), Quality.NORMAL, type.getWinterPrice());
        this.type = type;
        this.currentDailyLimit = type.getDailyLimit();
    }

    public String getName(){
        return type.getName();
    }

    public int getCurrentDailyLimit() {
        return currentDailyLimit;
    }

    public void setCurrentDailyLimit(int currentDailyLimit) {
        this.currentDailyLimit = currentDailyLimit;
    }
}
