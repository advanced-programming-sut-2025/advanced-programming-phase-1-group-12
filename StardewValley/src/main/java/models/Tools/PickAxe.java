package models.Tools;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Tools.Tools;
import models.enums.ToolEnums.ToolTypes;
import models.enums.Types.TypeOfTile;

public class PickAxe extends Tools {

    public PickAxe(ToolTypes type) {
        super("Pickaxe", type, 5); // Base energy cost is 5
    }

    public Result use(Location targetLocation, int miningSkill) {
        int energyCost = calculateEnergyCost(miningSkill);

        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use the pickaxe!");
        }

        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);

        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType == TypeOfTile.STONE) {
            targetLocation.setTypeOfTile(TypeOfTile.GROUND);
            return new Result(true, "You broke the stone!");
        }
        else if (tileType == TypeOfTile.QUARRY) {
            if (getLevel() >= 1) {
                return new Result(true, "You mined some ore!");
            } else {
                return new Result(false, "Your pickaxe isn't strong enough to mine this!");
            }
        }
        else if (targetLocation.getObjectInTile() != null) {
            targetLocation.setObjectInTile(null);
            return new Result(true, "You removed the object!");
        }

        return new Result(false, "You can't use your pickaxe here!");
    }
}