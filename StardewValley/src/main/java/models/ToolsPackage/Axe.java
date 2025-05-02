package models.ToolsPackage;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.enums.ToolEnums.ToolTypes;
import models.enums.Types.TypeOfTile;

public class Axe extends Tools {

    public Axe(ToolTypes type) {
        super("Axe", type, 5);
    }
    public Result use(Location targetLocation, int foragingSkill) {
        int energyCost = calculateEnergyCost(foragingSkill);
        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use the axe!");
        }

        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);

        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType == TypeOfTile.TREE) {
            // Chop tree
            targetLocation.setTypeOfTile(TypeOfTile.GROUND);
            return new Result(true, "You chopped down the tree and collected wood!");
        }

        return new Result(false, "You can't use your axe here!");
    }
}