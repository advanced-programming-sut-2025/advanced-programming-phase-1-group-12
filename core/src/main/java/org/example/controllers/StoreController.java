package org.example.controllers;

import org.example.models.Animal.AnimalHome;
import org.example.models.Animal.FarmAnimals;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.*;
import org.example.models.Fundementals.LocationOfRectangle;
import org.example.models.Fundementals.Result;
import org.example.models.MapDetails.Shack;
import org.example.models.Place.Store;
import org.example.models.ProductsPackage.Quality;
import org.example.models.ProductsPackage.StoreProducts;
import org.example.models.enums.Animal;
import org.example.models.enums.Types.Cooking;
import org.example.models.enums.Types.CraftingRecipe;
import org.example.models.enums.Types.StoreProductsTypes;
import org.example.models.enums.Types.TypeOfTile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class StoreController {

    public Result buyAnimalBuilding(String buildingName, Location location) {
//        if (!App.isInStore("Carpenter's Shop")) {
//            return new Result(false, "You are not in Carpenter's shop");
//        }
        if (!App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getLocation().getLocationsInRectangle().contains(location)) {
            return new Result(false, "given location is not in your farm at all");
        }
        if (!location.getTypeOfTile().equals(TypeOfTile.GROUND)) {
            return new Result(false, "You can not build this building here");
        }

        Location otherCorner;
       if (buildingName.contains("coop")) {
            otherCorner = App.getCurrentGame().getMainMap().findLocation(location.getxAxis() + 3, location.getyAxis() + 4);
            if (otherCorner == null || !App.getCurrentPlayerLazy().getOwnedFarm().getLocation().getLocationsInRectangle().contains(otherCorner)) {
                return new Result(false, "You can not build this building here");
            }
        }//it is a barn:
        else {
            otherCorner = App.getCurrentGame().getMainMap().findLocation(location.getxAxis() + 4, location.getyAxis() + 3);
            if (otherCorner == null || !App.getCurrentPlayerLazy().getOwnedFarm().getLocation().getLocationsInRectangle().contains(otherCorner)) {
                return new Result(false, "You can not build this building here");
            }
        }
        LocationOfRectangle buildingPlace = new LocationOfRectangle(location, otherCorner);
        if (!isAllLocationGround(buildingPlace)) {
            return new Result(false, "You can not build this building here");
        }
        return buildAnimalHomeSuccess(location, buildingName);
    }

    public Result buildAnimalHomeSuccess(Location givenLocation, String buildingName) {
        Location otherCorner;

        // Determine dimensions based on building type
        if (buildingName.contains("coop")) {
            if (buildingName.equals("big coop") || buildingName.equals("deluxe coop")) {
                otherCorner = App.getCurrentGame().getMainMap().findLocation(
                    givenLocation.getxAxis() + 4, // 3 + 1
                    givenLocation.getyAxis() + 5  // 4 + 1
                );
            } else {
                otherCorner = App.getCurrentGame().getMainMap().findLocation(
                    givenLocation.getxAxis() + 3,
                    givenLocation.getyAxis() + 4
                );
            }
        } else { // it's a barn
            if (buildingName.equals("big barn") || buildingName.equals("deluxe barn")) {
                otherCorner = App.getCurrentGame().getMainMap().findLocation(
                    givenLocation.getxAxis() + 3, // 2 + 1
                    givenLocation.getyAxis() + 4  // 3 + 1
                );
            } else {
                otherCorner = App.getCurrentGame().getMainMap().findLocation(
                    givenLocation.getxAxis() + 2,
                    givenLocation.getyAxis() + 3
                );
            }
        }

        LocationOfRectangle buildingPlace = new LocationOfRectangle(givenLocation, otherCorner);
        if (!isAllLocationGround(buildingPlace)) {
            return new Result(false, "you can not build this building here");
        }

        Item Wood = App.getCurrentPlayerLazy().getBackPack().getItemNames().get("Wood");
        Item Stone = App.getCurrentPlayerLazy().getBackPack().getItemNames().get("Stone");

        if (Wood == null || App.getCurrentPlayerLazy().getBackPack().getItems().get(Wood) < 50 || Stone == null
            || App.getCurrentPlayerLazy().getBackPack().getItems().get(Stone) < 50) {
            return new Result(false, "You do not have enough Wood or Stone to build this building");
        }

        App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Wood"), 50);
        App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Stone"), 50);

        switch (buildingName) {
            case "coop":
                App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes().add(
                    new AnimalHome("coop", Quality.NORMAL, 0, 4, "coop", buildingPlace)
                );
                break;
            case "deluxe coop":
                App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes().add(
                    new AnimalHome("deluxe coop", Quality.NORMAL, 0, 12, "deluxe coop", buildingPlace)
                );
                break;
            case "big coop":
                App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes().add(
                    new AnimalHome("big coop", Quality.NORMAL, 0, 8, "big coop", buildingPlace)
                );
                break;
            case "barn":
                App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes().add(
                    new AnimalHome("barn", Quality.NORMAL, 0, 4, "barn", buildingPlace)
                );
                break;
            case "deluxe barn":
                App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes().add(
                    new AnimalHome("deluxe barn", Quality.NORMAL, 0, 12, "deluxe barn", buildingPlace)
                );
                break;
            case "big barn":
                App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes().add(
                    new AnimalHome("big barn", Quality.NORMAL, 0, 8, "big barn", buildingPlace)
                );
                break;
            default:
                return new Result(false, "Invalid building name entered");
        }

        changeTypeTileAfterBuild(buildingName, buildingPlace);

        return new Result(true, buildingName + " built successfully");
    }


    public void changeTypeTileAfterBuild(String type, LocationOfRectangle buildingPlace) {
        if (type.contains("coop")) {
            for (Location location : buildingPlace.getLocationsInRectangle()) {
                location.setTypeOfTile(TypeOfTile.COOP);
            }
        } else if (type.contains("barn")) {
            for (Location location : buildingPlace.getLocationsInRectangle()) {
                location.setTypeOfTile(TypeOfTile.BARN);
            }
        }
    }

    public boolean isAllLocationGround(LocationOfRectangle place) {
        for (Location location : place.getLocationsInRectangle()) {
            if (!location.getTypeOfTile().equals(TypeOfTile.GROUND) &&
                    !App.getCurrentGame().getMainMap().findLocation(location.getxAxis(), location.getyAxis()).getTypeOfTile().equals(TypeOfTile.GROUND)) {
                return false;
            }
        }
        return true;
    }

    //TODO:daily limits are not conducted with this yet:
    public Result buyAnimal(Matcher matcher) {
        String name = matcher.group("name");
        String animalType = matcher.group("animal");

        Animal type = findAnimalType(animalType);
        if (type == null) {
            return new Result(false, "Animal type " + animalType + " not found");
        }
        if (!App.isInStore("Marnie's Ranch")) {
            return new Result(false, "You are not in Marnie's Ranch");
        }
        if (App.getCurrentPlayerLazy().getMoney() < type.getPurchaseCost()) {
            return new Result(false, "You do not have enough money to buy this animal");
        }
        AnimalHome home = null;
        for (AnimalHome animalHome : App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes()) {
            if (animalHome.getCapacityRemained() > 0 && type.getPlacesCanStay().contains(animalHome.getType())) {
                home = animalHome;
                break;
            }
        }
        if (home == null) {
            return new Result(false, "You can't buy this animal because there is no home for it");
        }
        //pay for it
        App.getCurrentPlayerLazy().decreaseMoney(type.getPurchaseCost());
        FarmAnimals newAnimal = new FarmAnimals(type, 0, home, name,
                App.getCurrentGame().getMainMap().findLocation(home.getLocation().getTopLeftCorner().getxAxis() + 1, home.getLocation().getTopLeftCorner().getyAxis() + 1));
        //TODO:vaghti too khoonan roo ham bood,doroste in?
        boolean isEmpty = true;
        for(Location location : home.getLocation().getLocationsInRectangle()){
            for(FarmAnimals animals: App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()){
                if(animals.getPosition().equals(location)){
                    isEmpty = false;
                }
                if(isEmpty){
                    newAnimal.setPosition(location);
                    break;
                }
            }
            break;
        }
        App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals().add(newAnimal);
        home.setCapacityRemained(home.getCapacityRemained() - 1);

        return new Result(true, "You bought this animal");
    }

    public Animal findAnimalType(String animalType) {
        for (Animal type : Animal.values()) {
            if (animalType.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return null;
    }

    public void ShowProducts() {
        Store store = null;

        for (Store store1 : App.getCurrentGame().getMainMap().getStores()) {
            if (App.isLocationInPlace(App.getCurrentPlayerLazy().getUserLocation(), store1.getLocationOfRectangle())) {
                store = store1;
            }
        }
        if (store == null) {
            System.out.println("You are not in any store");
            return;
        }//TODO:does it print it right?
        String storeName = store.getNameOfStore();
        for (StoreProductsTypes storeProductsTypes : StoreProductsTypes.values()) {
            if (storeProductsTypes.getShop().equalsIgnoreCase(storeName)) {
                System.out.println(storeProductsTypes.getName());
                //TODO:print price
            }
        }
    }

    public void showTotalProducts(List<Store> stores) {
    }

    public Result buyProduct(Store store,String productName, int count) {
        if (store == null) {
            return new Result(false, "You are not in any store");
        }

        if(App.getCurrentGame().getDate().getHour() < store.getStartHour() || App.getCurrentGame().getDate().getHour() > store.getCloseHour()){
            return new Result(false, "Store is currently closed");
        }

        StoreProducts item = null;
        for (StoreProducts item1 : store.getStoreProducts()) {
            if (item1.getName().equalsIgnoreCase(productName)) {
                item = item1;
                break;
            }
        }
        if (item == null) {
            return new Result(false, "The store doesn't have this product");
        }

        if (item.getCurrentDailyLimit() < count) {
            return new Result(false, "Not enough stock in the store today.");
        }

        int price = switch (App.getCurrentGame().getDate().getSeason()) {
            case AUTUMN -> item.getType().getFallPrice();
            case WINTER -> item.getType().getWinterPrice();
            case SUMMER -> item.getType().getSummerPrice();
            case SPRING -> item.getType().getSpringPrice();
        };

        if(price == 0){
            return new Result(false, "You can not buy this product in this season");
        }

        int totalCost = price * count;
        if (App.getCurrentPlayerLazy().getMoney() < totalCost) {
            return new Result(false, "You do not have enough money to buy this product");
        }


        if (item.getName().equalsIgnoreCase(StoreProductsTypes.CARPENTER_WELL.getName())) {
            Item Wood = App.getCurrentPlayerLazy().getBackPack().getItemNames().get("Wood");

            if (Wood == null || App.getCurrentPlayerLazy().getBackPack().getItems().get(Wood) < 75) {
                return new Result(false, "You do not have enough Wood or Stone to build this building");
            }
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(Wood, 75);

            ArrayList<Location> wellLocation = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                Shack shack = App.getCurrentPlayerLazy().getOwnedFarm().getShack();
                wellLocation.add(App.getCurrentGame().getMainMap().findLocation(shack.getLocation().getTopLeftCorner().getxAxis() + i, shack.getLocation().getTopLeftCorner().getyAxis()));
                wellLocation.get(i).setTypeOfTile(TypeOfTile.LAKE);
            }
            return new Result(true, "you bought this Well");

        }
        if (item.getName().equalsIgnoreCase(StoreProductsTypes.CARPENTER_SHIPPING_BIN.getName())) {
            Item Wood = App.getCurrentPlayerLazy().getBackPack().getItemNames().get("Wood");

            if (Wood == null || App.getCurrentPlayerLazy().getBackPack().getItems().get(Wood) < 150) {
                return new Result(false, "You do not have enough Wood or Stone to build this building");
            }
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Wood"), 100);
            ShippingBin shippingBin = new ShippingBin(App.getCurrentPlayerLazy().getOwnedFarm().getShack().getLocation().getTopLeftCorner(), App.getCurrentPlayerLazy());
            shippingBin.getShippingBinLocation().setObjectInTile(shippingBin);

            App.getCurrentPlayerLazy().setShippingBin(shippingBin);

            return new Result(true, "You bought this shipping bin");
        }

        // Handle Crafting Recipe
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            if (productName.equalsIgnoreCase(recipe.getName())) {
                //TODO:ghimat hame 50 e alan taghiresh bedam
                Item Wood = App.getCurrentPlayerLazy().getBackPack().getItemNames().get("Wood");
                if( Wood == null || App.getCurrentPlayerLazy().getBackPack().getItems().get(Wood) < 30){
                    return new Result(false, "you do not have enough Wood or Stone to buy this recipe");
                }
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Wood"), 30);
                App.getCurrentPlayerLazy().getRecepies().put(recipe, true);
                App.getCurrentPlayerLazy().decreaseMoney(totalCost);
                return new Result(true, "You bought this recipe");
            }
        }

        // Handle Cooking Recipe
        for (Cooking cooking : Cooking.values()) {
            if (productName.equalsIgnoreCase(cooking.getName())) {
                App.getCurrentPlayerLazy().getCookingRecepies().put(cooking, true);
                App.getCurrentPlayerLazy().decreaseMoney(totalCost);
                return new Result(true, "You bought this recipe");
            }
        }
        BackPack backpack = App.getCurrentPlayerLazy().getBackPack();
        if (!backpack.checkCapacity(1)) {
            return new Result(false, "Not enough space in your inventory to add " + count + " of this item.");
        }

        // Regular item
        App.getCurrentPlayerLazy().decreaseMoney(totalCost);
        item.setCurrentDailyLimit(item.getCurrentDailyLimit() - count);
        ItemBuilder.addToBackPack(item, count, Quality.NORMAL); // assuming getType() returns Item


        return new Result(true, "You bought this product");
    }

    public Result cheatBuyItem(String productName, int count) {

        StoreProducts item = null;
        for (Store store1 : App.getCurrentGame().getMainMap().getStores()) {
            for (StoreProducts item1 : store1.getStoreProducts()) {
                if (item1.getName().equalsIgnoreCase(productName)) {
                    item = item1;
                    break;
                }
            }
        }


        if (item == null) {
            return new Result(false, "The store doesn't have this product");
        }

        if (item.getCurrentDailyLimit() < count) {
            return new Result(false, "Not enough stock in the store today.");
        }

        int price = switch (App.getCurrentGame().getDate().getSeason()) {
            case AUTUMN -> item.getType().getFallPrice();
            case WINTER -> item.getType().getWinterPrice();
            case SUMMER -> item.getType().getSummerPrice();
            case SPRING -> item.getType().getSpringPrice();
        };
        if(price == 0){
            return new Result(false, "You can not buy this product in this season");
        }

        int totalCost = price * count;
        if (App.getCurrentPlayerLazy().getMoney() < totalCost) {
            return new Result(false, "You do not have enough money to buy this product");
        }

        if (item.getName().equalsIgnoreCase(StoreProductsTypes.CARPENTER_WELL.getName())) {

            ArrayList<Location> wellLocation = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                Shack shack = App.getCurrentPlayerLazy().getOwnedFarm().getShack();
                wellLocation.add(App.getCurrentGame().getMainMap().findLocation(shack.getLocation().getTopLeftCorner().getxAxis() + i, shack.getLocation().getTopLeftCorner().getyAxis()));
                wellLocation.get(i).setTypeOfTile(TypeOfTile.LAKE);
            }
            return new Result(true, "you bought this Well");

        }
        if (item.getName().equalsIgnoreCase(StoreProductsTypes.CARPENTER_SHIPPING_BIN.getName())) {

            ShippingBin shippingBin = new ShippingBin(App.getCurrentPlayerLazy().getOwnedFarm().getShack().getLocation().getTopLeftCorner(), App.getCurrentPlayerLazy());
            shippingBin.getShippingBinLocation().setObjectInTile(shippingBin);

            App.getCurrentPlayerLazy().setShippingBin(shippingBin);

            return new Result(true, "You bought this shipping bin");
        }
        ItemBuilder.addToBackPack(item, count, Quality.NORMAL); // assuming getType() returns Item
        return new Result(true, "You bought this product");
    }

    public Result cheatAddItem(String productName, int count) {
        Item item = ItemBuilder.builder(productName, Quality.NORMAL, 10);

        if (item.getName().equals(productName) && item.getClass() == Item.class) {
            boolean isValidItem = false;
            for (StoreProductsTypes type : StoreProductsTypes.values()) {
                if (type.getName().equalsIgnoreCase(productName)) {
                    isValidItem = true;
                    break;
                }
            }

            if (!isValidItem) {
                return new Result(false, "The item '" + productName + "' does not exist in the game.");
            }
        }

        BackPack backpack = App.getCurrentPlayerLazy().getBackPack();
        if (!backpack.checkCapacity(1)) {
            return new Result(false, "Not enough space in your inventory to add " + count + " of this item.");
        }

        ItemBuilder.addToBackPack(item, count, Quality.NORMAL);
        return new Result(true, "Successfully added " + count + " " + productName + " to your inventory.");
    }
}
