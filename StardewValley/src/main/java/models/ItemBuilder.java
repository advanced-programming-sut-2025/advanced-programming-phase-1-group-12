package models;

import models.ToolsPackage.Tools;
import models.enums.ToolEnums.Tool;
import models.enums.Types.SeedTypes;
import models.enums.foraging.Seed;

public class ItemBuilder {

    public static Item builder(String name) {
        SeedTypes seedType = SeedTypes.stringToSeed(name);
        if (seedType != null) {
            return new Seed(seedType);
        }
        Tool abzar = Tool.stringToTool(name);
        if (abzar != null) {
            return new Tools(abzar);
        }
        return null;
    }

}
