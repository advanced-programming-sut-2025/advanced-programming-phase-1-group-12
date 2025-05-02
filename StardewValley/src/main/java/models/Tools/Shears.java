package models.Tools;

import models.Animal.FarmAnimals;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Tools.Tools;
import models.enums.Animal;
import models.enums.ToolEnums.ToolTypes;

public class Shears extends Tools {

    public Shears() {
        super("Shears", ToolTypes.NORMAL, 4);
    }
    public Result use(Location targetLocation, int skillLevel) {
        int energyCost = getBaseEnergyCost();
        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use the shears!");
        }
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);
        if (targetLocation.getObjectInTile() instanceof FarmAnimals) {
            FarmAnimals animal = (FarmAnimals) targetLocation.getObjectInTile();

            if (animal.getAnimal() == Animal.SHEEP) {
                // TODO: Add wool to player's inventory
                animal.setFriendShip(animal.getFriendShip() + 5);

                return new Result(true, "You sheared " + animal.getName() + "!");
            } else {
                return new Result(false, "You can only shear sheep!");
            }
        }

        return new Result(false, "There's no animal here to shear!");
    }
}