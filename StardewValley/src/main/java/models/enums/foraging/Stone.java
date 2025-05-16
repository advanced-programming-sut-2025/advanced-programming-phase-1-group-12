package models.enums.foraging;

import models.Item;

public class Stone extends Item {

    private MineralTypes mineralTypes;

    public Stone(MineralTypes mineralTypes) {
        super(mineralTypes.name());
        this.mineralTypes = mineralTypes;
    }

    public MineralTypes getMineralTypes() {
        return mineralTypes;
    }

    public void setMineralTypes(MineralTypes mineralTypes) {
        this.mineralTypes = mineralTypes;
    }
}
