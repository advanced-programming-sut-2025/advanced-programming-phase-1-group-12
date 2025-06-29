package models.enums.foraging;

import models.Item;
import models.ProductsPackage.Quality;

public class Stone extends Item {

    private MineralTypes mineralTypes;

    public Stone(MineralTypes mineralTypes) {
        super(mineralTypes.name(), Quality.NORMAL, 0);
        this.mineralTypes = mineralTypes;
    }

    public MineralTypes getMineralTypes() {
        return mineralTypes;
    }

    public void setMineralTypes(MineralTypes mineralTypes) {
        this.mineralTypes = mineralTypes;
    }
}
