package models.Tools;

import models.Animal.FarmAnimals;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Tools.Tools;
import models.enums.Animal;
import models.enums.ToolEnums.ToolTypes;

public class MilkPail extends Tools {

    public MilkPail() {
        super("Milk Pail", ToolTypes.NORMAL, 4);
    }

    public Result use(Location targetLocation, int skillLevel) {
        int energyCost = getBaseEnergyCost();
        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use the milk pail!");
        }
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);
        if (targetLocation.getObjectInTile() instanceof FarmAnimals) {
            FarmAnimals animal = (FarmAnimals) targetLocation.getObjectInTile();
            if (animal.getAnimal() == Animal.COW || animal.getAnimal() == Animal.GOAT) {
                // TODO: Add milk to player's inventory
                animal.setFriendShip(animal.getFriendShip() + 5);

                return new Result(true, "You milked " + animal.getName() + "!");
            } else {
                return new Result(false, "You can only milk cows and goats!");
            }
        }

        return new Result(false, "There's no animal here to milk!");
    }
}