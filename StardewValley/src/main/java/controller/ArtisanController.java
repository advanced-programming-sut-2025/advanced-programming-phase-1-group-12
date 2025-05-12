package controller;

import models.Fundementals.App;
import models.Fundementals.Result;
import models.Item;
import models.enums.Types.CraftingRecipe;

public class ArtisanController {

    public Result artisanUse(String artisan, String item) {
        if(App.getCurrentPlayerLazy().getBackPack().hasItem(artisan)) {
            return new Result(false, "You do not have this artisan");
        }

    }
    public Item makeHoney(){

    }
}
