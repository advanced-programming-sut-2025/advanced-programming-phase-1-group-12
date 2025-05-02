package models.Tools;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Tools.Tools;
import models.enums.ToolEnums.ToolTypes;

public class Scythe extends Tools {

    public Scythe() {
        super("Scythe", ToolTypes.NORMAL, 2);
    }

    public Result use(Location targetLocation, int skillLevel) {
        int energyCost = getBaseEnergyCost();
        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use the scythe!");
        }App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);

        // logic not implemented yet
        return new Result(true, "You swung your scythe!");
    }
}