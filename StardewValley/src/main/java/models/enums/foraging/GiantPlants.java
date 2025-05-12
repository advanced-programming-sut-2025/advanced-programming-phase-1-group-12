package models.enums.foraging;

import models.enums.Season;
import models.enums.Types.SeedTypes;

import java.util.List;

public enum GiantPlants {
    CAULIFLOWER("Cauliflower", SeedTypes.CauliflowerSeeds, "1-2-4-4-1", 12, true, -1, 175, true, 75, 33, List.of(Season.SPRING)),
    MELON("Melon", SeedTypes.MelonSeeds, "1-2-3-3-3", 12, true, -1, 250, true, 113, 50, List.of(Season.SUMMER));

    public final String name;
    public final SeedTypes source;
    public final int[] stages;
    public final int totalHarvestTime;
    public final boolean oneTime;
    public final int regrowthTime;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final int baseHealth;
    public final List<Season> seasons;

    GiantPlants(String name, SeedTypes source, String stages, int totalHarvestTime, boolean oneTime, int regrowthTime,
             int baseSellPrice, boolean isEdible, int energy, int baseHealth, List<Season> seasons) {
        this.name = name;
        this.source = source;
        this.stages = parseStages(stages);
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.baseHealth = baseHealth;
        this.seasons = seasons;
    }

    private static int[] parseStages(String str) {
        String[] parts = str.split("-");

        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }


    public static GiantPlants nameToCraftType(String name){
        for(GiantPlants ct : GiantPlants.values()){
            if(ct.name.equals(name)){
                return ct;
            }
        }
        return null;
    }

    public static GiantPlants sourceTypeToCraftType(SeedTypes seedTypes){
        for(GiantPlants ct : GiantPlants.values()){
            if(ct.source.equals(seedTypes)){
                return ct;
            }
        }
        return null;
    }
}
