package org.example.Common.models.enums.foraging;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Fundementals.App;

public class Seed extends Item {

    private SeedTypes type;

    public Seed(String name, Quality quality, int price, SeedTypes type) {
        super(name, quality, price);
        this.type = type;
    }

    public void setType(SeedTypes type) {
        this.type = type;
    }

    public SeedTypes getType() {
        return type;
    }

    public Texture getTexture() {
        String type1 = type.getName().toLowerCase();
        for(TypeOfPlant t : TypeOfPlant.values()) {
            String type2 = t.getName().toLowerCase();
            if(type1.contains(type2)) {
                Plant plant = new Plant(App.getCurrentGame().getMainMap().findLocation(0, 0), false, t);
                return plant.getTexture();
            }
        }
        return null;
    }
}
