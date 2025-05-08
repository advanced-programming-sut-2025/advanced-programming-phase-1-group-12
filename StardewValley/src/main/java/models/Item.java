package models;

import models.enums.Types.StoreProductsTypes;

public class Item {
    private StoreProductsTypes itemType; // non-static field

    public Item(StoreProductsTypes type) {
        this.itemType = type;
    }

    public StoreProductsTypes getItemType() {
        return itemType;
    }
}