package org.example.Common.models.ProductsPackage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Item;
import org.example.Common.models.enums.Types.StoreProductsTypes;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreProducts extends Item {
    private StoreProductsTypes type;

    private int currentDailyLimit;

    public StoreProductsTypes getType() {
        return type;
    }

    public void setType(StoreProductsTypes type) {
        this.type = type;
    }

    public StoreProducts(StoreProductsTypes type, int price) {
        super(type.name(), Quality.NORMAL, price);
        this.type = type;
        this.currentDailyLimit = type.getDailyLimit();
    }
    public StoreProducts() {}

    public String getName(){
        return type.getName();
    }

    public int getCurrentDailyLimit() {
        return currentDailyLimit;
    }

    public void setCurrentDailyLimit(int currentDailyLimit) {
        this.currentDailyLimit = currentDailyLimit;
    }

    public boolean isAvailable() {
        if (currentDailyLimit<= 0){
            return false;
        }
        int price = switch (App.getCurrentGame().getDate().getSeason()) {
            case AUTUMN -> type.getFallPrice();
            case WINTER -> type.getWinterPrice();
            case SUMMER -> type.getSummerPrice();
            case SPRING -> type.getSpringPrice();
        };

        if(price == 0){
            return false;
        }
        return true;
    }
}
