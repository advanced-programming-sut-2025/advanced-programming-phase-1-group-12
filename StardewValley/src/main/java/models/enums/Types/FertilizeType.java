package models.enums.Types;

public enum FertilizeType {
    fertilize1("fertilize1"),
    fertilize2("fertilize2"),
    fertilize3("fertilize3"),
    fertilize4("fertilize4");

    private final String name;

    FertilizeType(String name) {
        this.name = name;
    }

    public static FertilizeType stringToFertilize(String fertilize) {
        for(FertilizeType ft : FertilizeType.values()) {
            if(ft.toString().equals(fertilize)) {
                return ft;
            }
        }
        return null;
    }
}
