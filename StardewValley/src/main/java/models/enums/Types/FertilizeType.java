package models.enums.Types;

public enum FertilizeType {

    DeluxeRetainingSoil("Deluxe Retaining Soil"),
    BasicRetainingSoil("Basic Retaining Soil"),
    QualityRetainingSoil("Quality Retaining Soil");

    private final String name;

    FertilizeType(String name) {
        this.name = name;
    }

    public static FertilizeType stringToFertilize(String fertilize) {
        for(FertilizeType ft : FertilizeType.values()) {
            if(ft.getName().equalsIgnoreCase(fertilize)) {
                return ft;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }
}
