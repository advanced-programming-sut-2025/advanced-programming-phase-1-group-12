package models.ProductsPackage;

public enum Quality {
    NORMAL(1),
    SILVER(1.25),
    GOLDEN(1.5),
    IRIDIUM(2.0),;

    private final double priceMultiPlier;

    Quality(double priceMultiplier) {
        this.priceMultiPlier = priceMultiplier;
    }

    public double getPriceMultiPlier() {
        return priceMultiPlier;
    }
}
