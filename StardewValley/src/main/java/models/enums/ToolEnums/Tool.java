package models.enums.ToolEnums;

import models.Fundementals.Result;
import models.ToolsPackage.ToolFunction;
import models.ToolsPackage.UpgradeFunction;
import models.enums.Types.TypeOfTile;
import models.Animal.FarmAnimals;
import models.enums.Animal;

public enum Tool {
    HOE("Hoe", 5) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType == TypeOfTile.GROUND) {
                    // TODO: implement functionality
                    return new Result(true, "You tilled the soil!");
                }
                return new Result(false, "You can't use your hoe here!");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "Hoe upgraded to level " + (currentLevel + 1));
        }
    },

    PICKAXE("Pickaxe", 5) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType == TypeOfTile.STONE) {
                    location.setTypeOfTile(TypeOfTile.GROUND);
                    return new Result(true, "You broke the stone");
                }
                if (tileType == TypeOfTile.QUARRY) {
                    models.ToolsPackage.Tools tool = (models.ToolsPackage.Tools) location.getObjectInTile();
                    if (tool != null && tool.getLevel() >= 1) {
                        return new Result(true, "You mined some ore");
                    } else {
                        return new Result(false, "Your pickaxe isn't strong enough to mine this");
                    }
                }
                if (location.getObjectInTile() != null) {
                    location.setObjectInTile(null);
                    return new Result(true, "You removed the object");
                }
                return new Result(false, "There's nothing here to mine");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "PickAxe upgraded to level " + (currentLevel + 1));
        }
    },

    AXE("Axe", 5) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType == TypeOfTile.TREE) {
                    location.setTypeOfTile(TypeOfTile.GROUND);
                    return new Result(true, "You chopped down the tree and collected wood!");
                }
                return new Result(false, "You can't use your axe here!");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "Axe upgraded to level " + (currentLevel + 1));
        }
    },

    WATERING_CAN("Watering Can", 5) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                models.ToolsPackage.Tools tool = (models.ToolsPackage.Tools) location.getObjectInTile();

                if (tileType == TypeOfTile.LAKE) {
                    if (tool != null && tool.isWateringCan()) {
                        Result fillResult = tool.fillWateringCan();
                        return new Result(true, "You filled your watering can to capacity: " + tool.getCapacity());
                    }
                    return new Result(true, "You filled your watering can");
                }

                if (tool != null && tool.isWateringCan()) {
                    if (tool.getCurrentWater() <= 0) {
                        return new Result(false, "Your watering can is empty! Fill it at a water source.");
                    }

                    Result useResult = tool.useWater(1);
                    if (!useResult.isSuccessful()) {
                        return useResult;
                    }

                    return new Result(true, "You watered the soil. Water remaining: " + tool.getCurrentWater());
                }

                return new Result(true, "You watered the soil");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "WateringCan upgraded to level " + (currentLevel + 1));
        }
    },

    FISHING_POLE("Fishing Pole", 8) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType != TypeOfTile.LAKE) {
                    return new Result(false, "You can only fish in water");
                }
                return new Result(true, "You caught a fish");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "FishingRod upgraded to level " + (currentLevel + 1));
        }
    },

    SCYTHE("Seythe", 2) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> new Result(true, "You swung your scythe");
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "Scythe upgraded to level " + (currentLevel + 1));
        }
    },

    MILKPALE("Milk Pale", 4) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> {
                if (location.getObjectInTile() instanceof FarmAnimals) {
                    FarmAnimals animal = (FarmAnimals) location.getObjectInTile();
                    if (animal.getAnimal() == Animal.COW || animal.getAnimal() == Animal.GOAT) {
                        animal.setFriendShip(animal.getFriendShip() + 5);
                        return new Result(true, "You milked " + animal.getName());
                    } else {
                        return new Result(false, "You can only milk cows and goats");
                    }
                }
                return new Result(false, "There's no animal here to milk");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "MilkPail upgraded to level " + (currentLevel + 1));
        }
    },

    SHEAR("Shear", 4) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> {
                if (location.getObjectInTile() instanceof FarmAnimals) {
                    FarmAnimals animal = (FarmAnimals) location.getObjectInTile();
                    if (animal.getAnimal() == Animal.SHEEP) {
                        animal.setFriendShip(animal.getFriendShip() + 5);
                        return new Result(true, "You sheared " + animal.getName());
                    } else {
                        return new Result(false, "You can only shear sheep");
                    }
                }
                return new Result(false, "There's no animal here to shear");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "Shears upgraded to level " + (currentLevel + 1));
        }
    },

    TRASH_CAN("Trash Can", 0) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel) -> {
                return new Result(false, "Use the inventory commands to trash items");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel) -> new Result(true, "Trash Can upgraded to level " + (currentLevel + 1));
        }
    };

    private final String name;
    private final int energyCost;

    Tool(String name, int energyCost) {
        this.name = name;
        this.energyCost = energyCost;
    }

    public String getName() {
        return name;
    }

    public int getEnergyCost() {
        return energyCost;
    }

    public abstract ToolFunction getUseFunction();
    public abstract UpgradeFunction getUpgradeFunction();
}
