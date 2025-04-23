package models;

import models.enums.CraftingRecepie;

public class Craft {
    private CraftingRecepie type;

    private String description;

    public CraftingRecepie getType() {
        return type;
    }

    public void setType(CraftingRecepie type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
