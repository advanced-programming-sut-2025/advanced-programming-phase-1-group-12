package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.*;
import org.example.controllers.MenusController.GameMenuController;
import org.example.controllers.movingPlayer.UserLocationController;
import org.example.models.Animal.FarmAnimals;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Result;
import org.example.models.GameAssetManager;
import org.example.models.RelatedToUser.Ability;
import org.example.models.enums.commands.GameMenuCommands;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu extends AppMenu implements Screen {
    private final GameMenuController controller = new GameMenuController();
    private final ToolsController toolsController = new ToolsController();
    private final AnimalController animalController = new AnimalController();
    private final StoreController storeController = new StoreController();
    private final FarmingController farmingController = new FarmingController();
    private final CraftingController craftingController = new CraftingController();
    private final ArtisanController artisanController = new ArtisanController();

    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private final List<String> players;

    public GameMenu(List<String> players) {
        this.players = players;
    }

    @Override
    public void check(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = GameMenuCommands.EXIT.getMather(input)) != null) {
            System.out.println(controller.EXIT());
        } else if ((matcher = GameMenuCommands.LoadGame.getMather(input)) != null) {
            controller.loadGameById(matcher.end("gameID"));
        } else if ((matcher = GameMenuCommands.PRINT.getMather(input)) != null) {
            controller.printMap(Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y")), Integer.parseInt(matcher.group("size")), scanner);
        } else if ((matcher = GameMenuCommands.TIME.getMather(input)) != null) {
            showCurrentTime();
        } else if ((matcher = GameMenuCommands.HELP.getMather(input)) != null) {
            controller.helpToReadMap();
        } else if ((matcher = GameMenuCommands.SHOW_CURRENT_TYPE.getMather(input)) != null) {
            System.out.println(controller.showCurrentType(Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.PLACE_ON_GROUND.getMather(input)) != null) {
            System.out.println(farmingController.plantInGreenHouse(matcher.group("seed"),
                Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.NextTurn.getMather(input)) != null) {
            System.out.println(GameMenuController.nextTurn());
        } else if ((matcher = GameMenuCommands.PRODUCTION.getMather(input)) != null) {
            System.out.println(farmingController.putScarecrow(Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.DATE.getMather(input)) != null) {
            showCurrentDate();
        } else if ((matcher = GameMenuCommands.DATETIME.getMather(input)) != null) {
            dateTime();
        } else if ((matcher = GameMenuCommands.DAY_OF_WEEK.getMather(input)) != null) {
            dayOfWeek();
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCED_TIME.getMather(input)) != null) {
            cheatAdvancedTime(matcher.group("time"));
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCED_DATE.getMather(input)) != null) {
            cheatAdvancedDate(matcher.group("date"));
        } else if ((matcher = GameMenuCommands.SEASON.getMather(input)) != null) {
            showCurrentSeason();
        } else if ((matcher = GameMenuCommands.WEATHER.getMather(input)) != null) {
            System.out.println(controller.showWeather().getMessage());
        } else if ((matcher = GameMenuCommands.WEATHER_FORECAST.getMather(input)) != null) {
            weatherForecast();
        } else if ((matcher = GameMenuCommands.CHEAT_WEATHER_SET.getMather(input)) != null) {
            cheatWeather(matcher.group("type"));
        } else if ((matcher = GameMenuCommands.SHOW_POSITION.getMather(input)) != null) {
            System.out.println(controller.showLocation());
        } else if (App.getCurrentPlayerLazy().getEnergy() <= 0 && !App.getCurrentPlayerLazy().isEnergyUnlimited()) {
            System.out.println("You are not enough energy to do anything.");
        } else if ((matcher = GameMenuCommands.WALK.getMather(input)) != null) {
            System.out.println(UserLocationController.walkPlayer(matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.ENERGY_SHOW.getMather(input)) != null) {
            showEnergy();
        } else if ((matcher = GameMenuCommands.ENERGY_SET.getMather(input)) != null) {
            setEnergy(matcher.group("value"));
        } else if ((matcher = GameMenuCommands.ENERGY_UNLIMITED.getMather(input)) != null) {
            setEnergyUnlimited();
        } else if ((matcher = GameMenuCommands.INVENTORY_SHOW.getMather(input)) != null) {
            showInventory();
        } else if ((matcher = GameMenuCommands.INVENTORY_TRASH.getMather(input)) != null) {
            try {
                trashItem(matcher.group("item"), matcher.group("number"));
            } catch (IllegalArgumentException e) {
                trashItem(matcher.group("item"), null);
            }
        } else if ((matcher = GameMenuCommands.SHOW_CURRENT_TOOL.getMather(input)) != null) {
            Result result = toolsController.showCurrentTool();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.EQUIP_TOOL.getMather(input)) != null) {
            Result result = toolsController.equipTool(matcher.group("toolName"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.Picking_FRUIT.getMather(input)) != null) {
            System.out.println(farmingController.PickingFruit(Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.CUTTING_TREE.getMather(input)) != null) {
            System.out.println(farmingController.cuttingTrees(Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.EXTRACTION.getMather(input)) != null) {
            System.out.println(farmingController.extraction(Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.SHOW_AVAILABLE_TOOL.getMather(input)) != null) {
            Result result = toolsController.showToolsAvailable();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.UPGRADE_TOOL.getMather(input)) != null) {
            boolean isInSmithing = toolsController.checkIsInSmithing();
            Result result = toolsController.updateToolsCheck(matcher.group("toolName"), isInSmithing);
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.USE_TOOL.getMather(input)) != null) {
            Result result = toolsController.useTool(matcher.group("direction"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TALK.getMather(input)) != null) {
            Result result = controller.talk(matcher.group("username"), matcher.group("message"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TALK_HISTORY.getMather(input)) != null) {
            Result result = controller.talkHistory(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT.getMather(input)) != null) {
            Result result = controller.gift(matcher.group("username"), matcher.group("item"), matcher.group("amount"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_LIST.getMather(input)) != null) {
            Result result = controller.giftList();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_RATE.getMather(input)) != null) {
            Result result = controller.giftRate(matcher.group("giftNumber"), matcher.group("rate"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_HISTORY.getMather(input)) != null) {
            Result result = controller.giftHistory(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.HUG.getMather(input)) != null) {
            Result result = controller.hug(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.FLOWER.getMather(input)) != null) {
            Result result = controller.flower(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.ASK_MARRIAGE.getMather(input)) != null) {
            Result result = controller.askMarriage(matcher.group("username"), matcher.group("ring"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.RESPOND.getMather(input)) != null) {
            Result result = controller.respond(matcher.group(0), matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.START_TRADE.getMather(input)) != null) {
            Result result = controller.startTrade();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_CREATE.getMather(input)) != null) {
            String username = matcher.group("username");
            String type = matcher.group("type");
            String item = matcher.group("item");
            createTrade(username, type, item, matcher);

        } else if ((matcher = GameMenuCommands.TRADE_LIST.getMather(input)) != null) {
            Result result = controller.listTrades();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_RESPONSE.getMather(input)) != null) {
            String response = matcher.group(1); // accept or reject
            String id = matcher.group("id");
            Result result = controller.respondToTrade(response, id);
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_HISTORY.getMather(input)) != null) {
            Result result = controller.tradeHistory();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.SELL_ANIMAL.getMather(input)) != null) {
            System.out.println(animalController.sellAnimal(matcher.group("name")).getMessage());
        } else if ((matcher = GameMenuCommands.FISHING.getMather(input)) != null) {
            System.out.println(animalController.fishing(matcher.group("fishingPole")).getMessage());
        } else if ((matcher = GameMenuCommands.SHEPHERD_ANIMALS.getMather(input)) != null) {
            System.out.println(animalController.shepherd(matcher).getMessage());
        } else if ((matcher = GameMenuCommands.ANIMALS_LIST.getMather(input)) != null) {
            animalController.animalsList();
        } else if ((matcher = GameMenuCommands.SHEPHERD_ANIMALS.getMather(input)) != null) {
            System.out.println(animalController.shepherd(matcher).getMessage());
        } else if ((matcher = GameMenuCommands.CHEAT_SET_FRIENDSHIP.getMather(input)) != null) {
            System.out.println(animalController.cheatFriendship(matcher).getMessage());
        } else if ((matcher = GameMenuCommands.MILK.getMather(input)) != null) {
            System.out.println(animalController.milking(matcher.group("name")).getMessage());
        } else if ((matcher = GameMenuCommands.SHEAR.getMather(input)) != null) {
            System.out.println(animalController.shear(matcher.group("name")).getMessage());
        } else if ((matcher = GameMenuCommands.PET.getMather(input)) != null) {
            System.out.println(animalController.pet(matcher.group("name")).getMessage());
        } else if ((matcher = GameMenuCommands.FEED_HAY.getMather(input)) != null) {
            System.out.println(animalController.feedHay(matcher.group("animalName")).getMessage());
        } else if ((matcher = GameMenuCommands.BUY_ANIMAL.getMather(input)) != null) {
            System.out.println(storeController.buyAnimal(matcher).getMessage());
        } else if ((matcher = GameMenuCommands.SHOW_PRODUCTS.getMather(input)) != null) {
            storeController.ShowProducts();
        } else if ((matcher = GameMenuCommands.BUILD_BUILDING.getMather(input)) != null) {
            Location location = App.getCurrentGame().getMainMap().findLocation(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y")));
            System.out.println(storeController.buyAnimalBuilding(matcher.group("buildingName"), location).getMessage());
        } else if ((matcher = GameMenuCommands.PURCHASE.getMather(input)) != null) {
            System.out.println(storeController.buyProduct(matcher.group("productName"), Integer.parseInt(matcher.group("count"))));
        } else if ((matcher = GameMenuCommands.COLLECT.getMather(input)) != null) {
            System.out.println(animalController.collectProduce(matcher.group("name")).getMessage());
        } else if ((matcher = GameMenuCommands.COOKING_RECIPES.getMather(input)) != null) {
            System.out.println(craftingController.showRecipes());
        } else if ((matcher = GameMenuCommands.COOKING_REFRIGERATOR.getMather(input)) != null) {
            System.out.println(controller.refrigerator(matcher.group(0), matcher.group("item")));
        } else if ((matcher = GameMenuCommands.COOKING_PREPARE.getMather(input)) != null) {
            System.out.println(controller.prepare(matcher.group("recipeName")));
        } else if ((matcher = GameMenuCommands.EAT_FOOD.getMather(input)) != null) {
            System.out.println(controller.eat(matcher.group("foodName")));
        } else if ((matcher = GameMenuCommands.TALK.getMather(input)) != null) {
            Result result = controller.talk(matcher.group("username"), matcher.group("message"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TALK_HISTORY.getMather(input)) != null) {
            Result result = controller.talkHistory(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT.getMather(input)) != null) {
            Result result = controller.gift(matcher.group("username"), matcher.group("item"), matcher.group("amount"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_LIST.getMather(input)) != null) {
            Result result = controller.giftList();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_RATE.getMather(input)) != null) {
            Result result = controller.giftRate(matcher.group("gift_number"), matcher.group("rate"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_HISTORY.getMather(input)) != null) {
            Result result = controller.giftHistory(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.HUG.getMather(input)) != null) {
            Result result = controller.hug(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.FLOWER.getMather(input)) != null) {
            Result result = controller.flower(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.ASK_MARRIAGE.getMather(input)) != null) {
            Result result = controller.askMarriage(matcher.group("username"), matcher.group("ring"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.RESPOND.getMather(input)) != null) {
            Result result = controller.respond(matcher.group(0), matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.START_TRADE.getMather(input)) != null) {
            Result result = controller.startTrade();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_CREATE.getMather(input)) != null) {
            String username = matcher.group("username");
            String type = matcher.group("type");
            String item = matcher.group("item");
            createTrade(username, type, item, matcher);

        } else if ((matcher = GameMenuCommands.TRADE_LIST.getMather(input)) != null) {
            Result result = controller.listTrades();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_RESPONSE.getMather(input)) != null) {
            String response = matcher.group(1); // accept or reject
            String id = matcher.group("id");
            Result result = controller.respondToTrade(response, id);
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_HISTORY.getMather(input)) != null) {
            Result result = controller.tradeHistory();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.CRAFT_INFO.getMather(input)) != null) {
            System.out.println(farmingController.showCraftInto(matcher.group("craftName")));
        } else if ((matcher = GameMenuCommands.FORAGING_TREE_TYPE.getMather(input)) != null) {
            System.out.println(farmingController.showForagingTreeInfo(matcher.group("type")));
        } else if ((matcher = GameMenuCommands.TREE_TYPE.getMather(input)) != null) {
            System.out.println(farmingController.showTreeInfo(matcher.group("type")));
        } else if ((matcher = GameMenuCommands.FORAGING_CROPS.getMather(input)) != null) {
            System.out.println(farmingController.showForagingCropsInfo(matcher.group("type")));
        } else if ((matcher = GameMenuCommands.PLANT.getMather(input)) != null) {
            System.out.println(farmingController.plant(matcher.group("seed"), matcher.group("direction")));
        } else if ((matcher = GameMenuCommands.SHOW_PLANT.getMather(input)) != null) {
            System.out.println(farmingController.showPlant(matcher.group("X"), matcher.group("Y")));
        } else if ((matcher = GameMenuCommands.FERTILIZE.getMather(input)) != null) {
            System.out.println(farmingController.fertilize(matcher.group("fertilize"), matcher.group("direction")));
        } else if ((matcher = GameMenuCommands.REAPING.getMather(input)) != null) {
            System.out.println(farmingController.reaping(matcher.group("direction")));
        } else if ((matcher = GameMenuCommands.WATERING.getMather(input)) != null) {
            System.out.println(farmingController.watering(Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.HOW_MUCH_WATER.getMather(input)) != null) {
            System.out.println(farmingController.howMuchWater());
        } else if ((matcher = GameMenuCommands.ADD_TO_INVENTORY.getMather(input)) != null) {
            System.out.println(craftingController.addItem(matcher.group("itemName"),
                Integer.parseInt(matcher.group("count"))));
        } else if ((matcher = GameMenuCommands.PLACE_ON_GROUND.getMather(input)) != null) {
            System.out.println(craftingController.putItem(matcher.group("itemName"), matcher.group("direction")));
        } else if ((matcher = GameMenuCommands.MACK_CRAFT.getMather(input)) != null) {
            System.out.println(craftingController.makeCraft(matcher.group("itemName")));
        } else if ((matcher = GameMenuCommands.SHOW_RECIPES_CRAFTING.getMather(input)) != null) {
            System.out.println(craftingController.showRecipesCrafting());
        } else if ((matcher = GameMenuCommands.THOR.getMather(input)) != null) {
            System.out.println(controller.Thor(Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.WHICH_FERTILIZING.getMather(input)) != null) {
            System.out.println(farmingController.showFertilize());
        } else if ((matcher = GameMenuCommands.TakeFromGround.getMather(input)) != null) {
            System.out.println(craftingController.TakeFromGround(matcher.group("itemName"), Integer.parseInt(matcher.group("direction"))));
        } else if ((matcher = GameMenuCommands.MEET_NPC.getMather(input)) != null) {
            Result result = controller.meetNPC(matcher.group("npcName"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_NPC.getMather(input)) != null) {
            Result result = controller.giftNPC(matcher.group("npcName"), matcher.group("item"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.FRIENDSHIP_NPC_LIST.getMather(input)) != null) {
            Result result = controller.friendshipNPCList();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.FRIENDSHIP_LIST.getMather(input)) != null) {
            Result result = controller.friendshipList();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.QUESTS_LIST.getMather(input)) != null) {
            Result result = controller.questsList();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.QUESTS_FINISH.getMather(input)) != null) {
            Result result = controller.questsFinish(Integer.parseInt(matcher.group("index")));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.CHEAT_NPC_LOCATIONS.getMather(input)) != null) {
            Result result = controller.cheatNPCLocations();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.CHEAT_NPC_TEST_ITEMS.getMather(input)) != null) {
            Result result = controller.cheatNPCTestItems();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.SHOW_MONEY.getMather(input)) != null) {
            System.out.println(App.getCurrentPlayerLazy().getMoney());
        } else if ((matcher = GameMenuCommands.SELL.getMather(input)) != null) {
            System.out.println(controller.sellByShipping(matcher.group("productName"), matcher.group("count")).getMessage());
        } else if ((matcher = GameMenuCommands.SELL_ONE.getMather(input)) != null) {
            System.out.println(controller.sellByShippingWithoutCount(matcher.group("productName")).getMessage() + "sold without count");
        } else if ((matcher = GameMenuCommands.ARTISAN_GET.getMather(input)) != null) {
            System.out.println(artisanController.artisanGet(matcher.group("itemName")));
        } else if ((matcher = GameMenuCommands.ARTISAN_USE.getMather(input)) != null) {
            System.out.println(artisanController.artisanUse(matcher.group("artisanName"), matcher.group("itemName")));
        } else if ((matcher = GameMenuCommands.GREENHOUSE_BUILD.getMather(input)) != null) {
            System.out.println(controller.buildGreenHouse());
        } else if ((matcher = GameMenuCommands.ABILITIES_SHOW.getMather(input)) != null) {
            for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
                System.out.println(ability.getName() + " " + ability.getLevel() + " " + ability.getAmount());
            }
        } else if ((matcher = GameMenuCommands.CHEAT_PLAYER_MONEY.getMather(input)) != null) {
            System.out.println(App.getCurrentPlayerLazy().getMoney());
        } else if ((matcher = GameMenuCommands.SHOW_SHIPPING_BIN_LOCATION.getMather(input)) != null) {
            System.out.println(controller.showShippingBinLocation().getMessage());
        } else if ((matcher = GameMenuCommands.CHEAT_BUY_ITEM.getMather(input)) != null) {
            System.out.println(storeController.cheatBuyItem(matcher.group("productName"), Integer.parseInt(matcher.group("count"))));
        } else if ((matcher = GameMenuCommands.CHEAT_ADD_ITEM.getMather(input)) != null) {
            System.out.println(storeController.cheatAddItem(matcher.group("productName"), Integer.parseInt(matcher.group("count"))));
        } else if ((matcher = GameMenuCommands.CHEAT_FRIENDSHIP_LEVEL.getMather(input)) != null) {
            System.out.println(controller.cheatFriendShipLevel(matcher.group("name"), matcher.group("amount")));
        } else if ((matcher = GameMenuCommands.CHEAT_FRIENDSHIP_XP.getMather(input)) != null) {
            System.out.println(controller.cheatAddXP(matcher.group("username")));
        } else if ((matcher = GameMenuCommands.CHEAT_MAXIMIZE_ABILITY_LEVEL.getMather(input)) != null) {
            App.getCurrentPlayerLazy().getAbilityByName(matcher.group("ability")).setLevel(4);
        } else if ((matcher = GameMenuCommands.PRODUCES.getMather(input)) != null) {
            for (FarmAnimals farmAnimals : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()) {
                if (!farmAnimals.isHasCollectedProductToday()) {
                    System.out.println(farmAnimals.getName() + " " + animalController.whatWillProduceToday(farmAnimals.getAnimal(), farmAnimals));
                }
            }
        } else {
            System.out.println("invalid command");
        }

    }

    public void showCurrentTime() {
        Result result = controller.showHour();
        System.out.println(result.getMessage());
    }

    public void showCurrentDate() {
        Result result = controller.showDate();
        System.out.println(result.getMessage());
    }

    public void showCurrentSeason() {
        Result result = controller.showSeason();
        System.out.println(result.getMessage());
    }

    public void dateTime() {
        Result result = controller.showDateTime();
        System.out.println(result.getMessage());
    }

    public void dayOfWeek() {
        Result result = controller.showDayOfTheWeek();
        System.out.println(result.getMessage());
    }

    public void cheatAdvancedTime(String time) {
        Result result = controller.cheatAdvancedTime(time);
        System.out.println(result.getMessage());
    }

    public void cheatAdvancedDate(String day) {
        Result result = controller.cheatAdvancedDay(day);
        System.out.println(result.getMessage());
    }

    public void season() {
        Result result = controller.showSeason();
        System.out.println(result.getMessage());
    }

    public void weatherForecast() {
        Result result = controller.weatherForecast(App.getCurrentGame().getDate().getSeason());
        System.out.println(result.getMessage());
    }

    public void cheatWeather(String type) {
        Result result = controller.cheatWeather(type);
        System.out.println(result.getMessage());
    }

    public void showInventory() {
        Result result = controller.showInventory();
        System.out.println(result.getMessage());
    }

    public void trashItem(String item, String number) {
        Result result = controller.trashItem(item, number);
        System.out.println(result.getMessage());
    }

    public void setEnergyUnlimited() {
        Result result = controller.setUnlimited();
        System.out.println(result.getMessage());
    }

    public void setEnergy(String energy) {
        Result result = controller.setEnergy(energy);
        System.out.println(result.getMessage());
    }

    public void showEnergy() {
        Result result = controller.showEnergy();
        System.out.println(result.getMessage());
    }

    public void createTrade(String username, String type, String item, Matcher matcher) {
        int amount;
        try {
            amount = Integer.parseInt(matcher.group("amount"));
        } catch (Exception e) {
            System.out.println("amount must be a number!");
            return;
        }
        Integer price = null;
        String targetItem = null;
        Integer targetAmount = null;
        if (matcher.group("price") != null) {
            price = Integer.parseInt(matcher.group("price"));
        }
        if (matcher.group("targetItem") != null && matcher.group("targetAmount") != null) {
            targetItem = matcher.group("targetItem");
            try {
                targetAmount = Integer.parseInt(matcher.group("targetAmount"));
            } catch (NumberFormatException e) {
                System.out.println("targetAmount must be a number!");
                return;
            }
        }

        Result result = controller.createTrade(username, type, item, amount, price, targetItem, targetAmount);
        System.out.println(result.getMessage());
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
