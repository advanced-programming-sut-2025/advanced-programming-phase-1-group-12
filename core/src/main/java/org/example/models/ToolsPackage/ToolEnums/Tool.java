package org.example.models.ToolsPackage.ToolEnums;

import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Result;
import org.example.models.ToolsPackage.ToolFunction;
import org.example.models.ToolsPackage.Tools;
import org.example.models.ToolsPackage.UpgradeFunction;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.Animal.FarmAnimals;
import org.example.models.enums.Animal;

public enum Tool {
    HOE("Hoe", 5) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tool) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType == TypeOfTile.GROUND) {
                    location.setTypeOfTile(TypeOfTile.PLOUGHED_LAND);
                    App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - tool.getType().getEnergyDamage());
                    return new Result(true, "You tilled the soil!");
                }
                return new Result(false, "You can't use your hoe here!");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
//            App.getCurrentPlayerLazy().decreaseMoney(35);
            return (currentLevel, tools) -> new Result(true, "Hoe upgraded to level " + (currentLevel + 1));
        }
    },

    PICKAXE("Pickaxe", 5) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tool) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType == TypeOfTile.STONE) {
                    location.setTypeOfTile(TypeOfTile.GROUND);
                    App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - tool.getType().getEnergyDamage());
                    return new Result(true, "You broke the stone");
                }
                if (tileType == TypeOfTile.QUARRY) {
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
//            App.getCurrentPlayerLazy().decreaseMoney(40);
            return (currentLevel, tools) -> new Result(true, "PickAxe upgraded to level " + (currentLevel + 1));
        }
    },
    AXE("Axe", 5) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tools) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType == TypeOfTile.TREE) {
                    location.setTypeOfTile(TypeOfTile.GROUND);
                    App.getCurrentPlayerLazy().setEnergy(
                            App.getCurrentPlayerLazy().getEnergy() - tools.getType().getEnergyDamage()
                    );
                    return new Result(true, "You chopped down the tree and collected wood!");
                }
                return new Result(false, "You can't use your axe here!");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel, tools) -> {
                Tools tool = App.getCurrentPlayerLazy().getCurrentTool();
                switch (currentLevel) {
                    case 0:
                        if(!tool.isTrashCan()){
                        tool.setType(ToolTypes.COPPER);
                        tool.setLevel(tool.getLevel() + 1);}
                        break;
                    case 1:
                        tool.setType(ToolTypes.STEEL);
                        tool.setLevel(tool.getLevel() + 1);
                        break;
                    case 2:
                        tool.setType(ToolTypes.GOLD);
                        tool.setLevel(tool.getLevel() + 1);
                        break;
                    case 3:
                        tool.setType(ToolTypes.IRIDIUM);
                        tool.setLevel(tool.getLevel() + 1);
                        break;
                    default:
                        tool.setType(ToolTypes.IRIDIUM);
                        tool.setLevel(tool.getLevel() + 1);
                }
//                App.getCurrentPlayerLazy().decreaseMoney(35);
                return new Result(true, "Axe upgraded to level " + (currentLevel + 1));
            };
        }
    },

    WATERING_CAN("Watering Can", 5) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tools) -> {
                TypeOfTile tileType = location.getTypeOfTile();

                if (tileType == TypeOfTile.LAKE) {
                    if (tools != null && tools.isWateringCan()) {
                        Result fillResult = tools.fillWateringCan();
                        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - tools.getType().getEnergyDamage());
                        return new Result(true, "You filled your watering can to capacity: " + tools.getCapacity());
                    }
                    return new Result(true, "You filled your watering can");
                }

                if (tools != null && tools.isWateringCan()) {
                    if (tools.getCurrentWater() <= 0) {
                        return new Result(false, "Your watering can is empty! Fill it at a water source.");
                    }

                    Result useResult = tools.useWater(1);
                    if (!useResult.isSuccessful()) {
                        return useResult;
                    }
                    App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - tools.getType().getEnergyDamage());
                    return new Result(true, "You watered the soil. Water remaining: " + tools.getCurrentWater());
                }

                return new Result(true, "You watered the soil");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
//            App.getCurrentPlayerLazy().decreaseMoney(42);
            return (currentLevel, tools) -> new Result(true, "WateringCan upgraded to level " + (currentLevel + 1));
        }
    },

    FISHING_POLE("Fishing Pole", 8) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tools) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType != TypeOfTile.LAKE) {
                    return new Result(false, "You can only fish in water");
                }
                return new Result(true, "You caught a fish");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
//            App.getCurrentPlayerLazy().decreaseMoney(37);
            return (currentLevel, tools) -> new Result(true, "FishingRod upgraded to level " + (currentLevel + 1));
        }
    },

    SCYTHE("Scythe", 2) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tool) -> {
                TypeOfTile tileType = location.getTypeOfTile();
                if (tileType == TypeOfTile.GROUND) {
                    location.setTypeOfTile(TypeOfTile.PLOUGHED_LAND);
                    App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - tool.getType().getEnergyDamage());
                    return new Result(true, "You swang the scythe!");
                }
                return new Result(false, "You can't use your Scythe here!");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
//            App.getCurrentPlayerLazy().decreaseMoney(35);
            return (currentLevel, tools) -> new Result(true, "Scythe upgraded to level " + (currentLevel + 1));
        }
    },

    MILKPALE("Milk Pale", 4) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tools) -> {
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
//            App.getCurrentPlayerLazy().decreaseMoney(43);
            return (currentLevel, tools) -> new Result(true, "MilkPail upgraded to level " + (currentLevel + 1));
        }
    },

    SHEAR("Shear", 4) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tools) -> {
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
//            App.getCurrentPlayerLazy().decreaseMoney(16);
            return (currentLevel, tools) -> new Result(true, "Shears upgraded to level " + (currentLevel + 1));
        }
    },

    TRASH_CAN("Trash Can", 0) {
        @Override
        public ToolFunction getUseFunction() {
            return (location, skillLevel, tools) -> {
                return new Result(false, "Use the inventory commands to trash items");
            };
        }

        @Override
        public UpgradeFunction getUpgradeFunction() {
            return (currentLevel, tools) -> new Result(true, "Trash Can upgraded to level " + (currentLevel + 1));
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

    public static Tool stringToTool(String name) {
        for (Tool type : Tool.values()) {
            if (name.equalsIgnoreCase(type.name)) {
                return type;
            }
        }
        return null;
    }
}
