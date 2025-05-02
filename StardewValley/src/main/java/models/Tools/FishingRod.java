package models.Tools;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Tools.Tools;
import models.enums.ToolEnums.ToolTypes;
import models.enums.Types.TypeOfTile;
import models.enums.Fish;
import models.enums.Season;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FishingRod extends Tools {

    public enum FishingRodType {
        TRAINING(8, false, 25),
        BAMBOO(8, true, 500),
        FIBERGLASS(6, true, 1800),
        IRIDIUM(4, true, 7500);

        private final int energyCost;
        private final boolean canCatchAllFish;
        private final int cost;

        FishingRodType(int energyCost, boolean canCatchAllFish, int cost) {
            this.energyCost = energyCost;
            this.canCatchAllFish = canCatchAllFish;
            this.cost = cost;
        }

        public int getEnergyCost() {
            return energyCost;
        }

        public boolean canCatchAllFish() {
            return canCatchAllFish;
        }

        public int getCost() {
            return cost;
        }
    }

    private FishingRodType rodType;

    public FishingRod(FishingRodType rodType) {
        super("Fishing Rod", ToolTypes.NORMAL, rodType.getEnergyCost());
        this.rodType = rodType;
    }

    public FishingRodType getRodType() {
        return rodType;
    }

    public Result use(Location targetLocation, int fishingSkill) {
        int energyCost = rodType.getEnergyCost();
        if (fishingSkill == 10) {
            energyCost -= 1;
        }
        energyCost = Math.max(0, energyCost);

        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use the fishing rod!");
        }
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);

        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType != TypeOfTile.LAKE) {
            return new Result(false, "You can only fish in water!");
        }

        Season currentSeason = App.getCurrentGame().getDate().getSeason();

        List<Fish> availableFish = new ArrayList<>();
        for (Fish fish : Fish.values()) {
            if (fish.getSeasons().contains(currentSeason)) {
                if (rodType.canCatchAllFish() || !fish.isLegendary()) {
                    availableFish.add(fish);
                }
            }
        }

        if (availableFish.isEmpty()) {
            return new Result(false, "There are no fish to catch in this season!");
        }

        if (rodType == FishingRodType.TRAINING) {
            Fish cheapestFish = availableFish.get(0);
            for (Fish fish : availableFish) {
                if (fish.getBasePrice() < cheapestFish.getBasePrice()) {
                    cheapestFish = fish;
                }
            }

            // TODO: Add fish to player's inventory
            return new Result(true, "You caught a " + cheapestFish.name() + "!");
        }

        //catch a random fish
        Random random = new Random();
        Fish caughtFish = availableFish.get(random.nextInt(availableFish.size()));

        // TODO: Add fish to player's inventory
        return new Result(true, "You caught a " + caughtFish.name() + "!");
    }
}