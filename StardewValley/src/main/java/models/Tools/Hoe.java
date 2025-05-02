package models.Tools;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Tools.Tools;
import models.enums.ToolEnums.ToolTypes;
import models.enums.Types.TypeOfTile;

public class Hoe extends Tools {

    public Hoe(ToolTypes type) {
        super("Hoe", type, 5);
    }
    public Result use(Location targetLocation, int farmingSkill) {
        int energyCost = calculateEnergyCost(farmingSkill);
        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use the hoe!");
        }
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);

        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType == TypeOfTile.GROUND) {

            return new Result(true, "You tilled the soil!");
        }

        return new Result(false, "You can't use your hoe here!");
    }
}