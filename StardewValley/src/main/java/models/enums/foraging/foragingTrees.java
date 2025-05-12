package models.enums.foraging;

import models.enums.Types.FruitType;

public enum foragingTrees {
    Acorns(FruitType.Acrons),
    MapleSeeds(FruitType.Maple_Syrup),
    PineCones(FruitType.Pine_Tar),
    MahoganySeeds(FruitType.MahoganySeeds),
    MushroomTreeSeeds(FruitType.MushroomTreeSeeds);

    private final FruitType fruitType;

    foragingTrees(FruitType fruitType){
        this.fruitType = fruitType;
    }

    public FruitType getFruitType() {
        return fruitType;
    }
}
