package org.example.Common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.ProductsPackage.Quality;
import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.enums.Types.Cooking;
import org.example.Common.models.enums.foraging.MineralTypes;
import org.example.Common.models.enums.foraging.Plant;
import org.example.Common.models.enums.foraging.TypeOfPlant;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    private String name;
    private Quality quality;
    private int price;
    @JsonIgnore
    private transient Texture texture;

    public Item(String name, Quality quality, int price) {
        this.name = name;
        this.quality = quality;
        this.price = price;
    }

    public Item() {
        // No-arg constructor for Jackson
    }

    public String getName() {
        return name;
    }

    public Quality getQuality() {
        return quality;
    }

    public int getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }

    public void setPrice(int price) {
        this.price = price;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Item item = (Item) obj;
        return Objects.equals(name, item.name) && quality == item.quality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quality);
    }

    @JsonIgnore
    public Texture getTexture() {
        if(texture == null){
            for(Cooking cooking : Cooking.values()){
                if(name.equals(cooking.getName())){
                    return cooking.getTexture();
                }
            }
            if(name.contains("Seeds")){
                Plant plant = new Plant(App.getCurrentGame().getMainMap().findLocation(0, 0), false, TypeOfPlant.AMARANTH);
                return plant.getTexture();
            } if(name.contains("Starter")){
                Plant plant = new Plant(App.getCurrentGame().getMainMap().findLocation(0, 0), false, TypeOfPlant.HOPS);
                return plant.getTexture();
            }
            for(MineralTypes type : MineralTypes.values()){
                String name1 = name.toLowerCase();
                String name2 = type.name().toLowerCase();
                if(name1.contains(name2) || name2.contains(name1)){
                    return type.getTexture();
                }
            }
        }
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
