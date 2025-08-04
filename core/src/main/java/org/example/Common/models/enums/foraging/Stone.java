package org.example.Common.models.enums.foraging;

import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import com.badlogic.gdx.graphics.Texture;
public class Stone extends Item {

    private MineralTypes mineralTypes;

    public Stone(MineralTypes mineralTypes) {
        super(mineralTypes.name(), Quality.NORMAL, 0);
        this.mineralTypes = mineralTypes;
    }

    public Stone() {}

    public MineralTypes getMineralTypes() {
        return mineralTypes;
    }

    public void setMineralTypes(MineralTypes mineralTypes) {
        this.mineralTypes = mineralTypes;
    }

    public Texture getTexture() {
        return mineralTypes.getTexture();
    }
}
