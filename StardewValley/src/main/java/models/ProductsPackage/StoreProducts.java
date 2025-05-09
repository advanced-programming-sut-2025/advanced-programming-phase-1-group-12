package models.ProductsPackage;

import models.enums.Types.StoreProductsTypes;

public class StoreProducts implements Products{
    private StoreProductsTypes type;

    public StoreProductsTypes getType() {
        return type;
    }

    public void setType(StoreProductsTypes type) {
        this.type = type;
    }
}
