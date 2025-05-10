package controller;

import models.Animal.AnimalHome;
import models.Animal.FarmAnimals;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.*;
import models.Fundementals.LocationOfRectangle;
import models.Fundementals.Result;
import models.Place.Store;
import models.ProductsPackage.StoreProducts;
import models.enums.Animal;
import models.enums.Season;
import models.enums.Types.StoreProductsTypes;
import models.enums.Types.TypeOfTile;
import java.util.List;
import java.util.regex.Matcher;

public class StoreController {

    public Result buyAnimalBuilding(String buildingName, Location location) {
        if (!App.isLocationInPlace(location, App.getCurrentGame().getMainMap().getStores().get(3).getLocationOfRectangle())) {
            //TODO:money for building is not decreased
            return new Result(false, "You are not in Carpenter's shop");
        }
        if (App.isLocationInPlace(location, App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getLocation())) {
            return new Result(false, "given location is not in your farm at all");
        }
        if (!location.getTypeOfTile().equals(TypeOfTile.GROUND)) {
            return new Result(false, "You can not build this building here");
        }
        //i put the given location in the top left corner of the building
        //normal coop, Deluxe coop, big coop, normal barn, Deluxe barn, big barn:

        buildAnimalHomeSuccess(location, buildingName);
        return new Result(true, buildingName + "built successfully");
    }

    public void buildAnimalHomeSuccess(Location givenLocation, String buildingName) {
        Location otherCorner;
        if (buildingName.contains("coop")) {
            otherCorner = App.getCurrentGame().getMainMap().findLocation(givenLocation.getxAxis() + 3, givenLocation.getyAxis() + 6);
            if (otherCorner == null || !App.getCurrentPlayerLazy().getOwnedFarm().getLocation().getLocationsInRectangle().contains(otherCorner)) {
                System.out.println("you can not build this building here.");
                return;
            }
        }//it is a barn:
        else {
            otherCorner = App.getCurrentGame().getMainMap().findLocation(givenLocation.getxAxis() + 4, givenLocation.getyAxis() + 7);
            if (otherCorner == null || !App.getCurrentPlayerLazy().getOwnedFarm().getLocation().getLocationsInRectangle().contains(otherCorner)) {
                System.out.println("you can not build this building here.");
                return;
            }
        }
        LocationOfRectangle buildingPlace = new LocationOfRectangle(givenLocation, otherCorner);
        if(!isAllLocationGround(buildingPlace)) {
            System.out.println("you can not build this building here.");
        }
        switch (buildingName){
            case "coop":
                App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes().add(
                        new AnimalHome(4, "coop", buildingPlace)
                );
                break;
            case "deluxe coop":
                App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes().add(
                        new AnimalHome(12, "Deluxe coop", buildingPlace)
                );
                break;
            case "big coop":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(8, "big coop", buildingPlace));
                break;
            case "barn":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(4, "normal barn", buildingPlace));
                break;
            case "deluxe barn":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(12, "Deluxe barn", buildingPlace));
                break;
            case "big barn":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(8, "big barn", buildingPlace));
                break;
            default:
                System.out.println("Invalid building name entered");
        }
    }
    public boolean isAllLocationGround(LocationOfRectangle place){
        for(Location location : place.getLocationsInRectangle()){
            if( !location.getTypeOfTile().equals(TypeOfTile.GROUND) &&
                    !App.getCurrentGame().getMainMap().findLocation(location.getxAxis(), location.getyAxis()).getTypeOfTile().equals(TypeOfTile.GROUND)){
                return false;
            }
        }
        return true;
    }

    public Result buyAnimal(Matcher matcher) {
        String name = matcher.group("name");
        String animalType = matcher.group("animal");

        Animal type = findAnimalType(animalType);
        if(type == null) {
            return new Result(false, "Animal type " + animalType + " not found");
        }
        if (!App.isLocationInPlace(App.getCurrentPlayerLazy().getUserLocation(),
                App.getCurrentGame().getMainMap().getStores().get(5).getLocationOfRectangle())) {
            return new Result(false, "You are not in Marnie's Ranch");
        }
        if(App.getCurrentPlayerLazy().getMoney() < type.getPurchaseCost()) {
            return new Result(false, "You do not have enough money to buy this animal");
        }
        AnimalHome home = null;
        for(AnimalHome animalHome : App.getCurrentPlayerLazy().getOwnedFarm().getAnimalHomes()){
            if(animalHome.getCapacityRemained() > 0 && type.getPlacesCanStay().contains(animalHome.getType())){
                home = animalHome;
                break;
            }
        }
        if(home == null) {
            return new Result(false, "You can't buy this animal because there is no home for it");
        }
        //pay for it
        App.getCurrentPlayerLazy().setMoney(App.getCurrentPlayerLazy().getMoney() - type.getPurchaseCost());
        FarmAnimals newAnimal = new FarmAnimals(type, 0, home, name,
                App.getCurrentGame().getMainMap().findLocation(home.getLocation().getTopLeftCorner().getxAxis() + 1, home.getLocation().getTopLeftCorner().getyAxis() + 1));
        //vaghti too khoonan roo haman
        App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals().add(newAnimal);
        home.setCapacityRemained(home.getCapacityRemained() - 1);

        return new Result(true, "You bought this animal");
    }
    public Animal findAnimalType(String animalType){
        for(Animal type : Animal.values()){
            if(animalType.equalsIgnoreCase(type.name())){
                return type;
            }
        }
        return null;
    }
    public Store findStore(Location location){return null;}

    public boolean isStoreOpen(Store store, Date date){return false;}

    public void ShowProducts(){
        Store store = null;

        for(Store store1: App.getCurrentGame().getMainMap().getStores()){
            if(App.isLocationInPlace(App.getCurrentPlayerLazy().getUserLocation(), store1.getLocationOfRectangle())){
                store = store1;
            }
        }
        if(store == null) {
            System.out.println("You are not in any store");
            return;
        }//TODO:does it print it right?
        for(StoreProductsTypes type: StoreProductsTypes.values()){
            if(type.getShop().equals(store)){
                System.out.println(type.getName().toString());
            }
        }
    }

    public void showTotalProducts(List<Store> stores){}

    public Result buyProduct(String productName, int Count){

        Store store = null;
        for(Store store1 : App.getCurrentGame().getMainMap().getStores()){
            if(App.isLocationInPlace(App.getCurrentPlayerLazy().getUserLocation(), store1.getLocationOfRectangle())){
                store = store1;
                break;
            }
        }
        if(store == null) {
            return new Result(false, "You are not in any store");
        }
        StoreProducts item = null;
        for(StoreProducts item1 : store.getAllProducts()){
            if(item1.getName().equals(productName)){
                item = item1;
                break;
            }
        }
        if(item == null) {
            return new Result(false, "The store doesn't have this product");
        }
        item.setCurrentDailyLimit(item.getCurrentDailyLimit() - Count);
        int price = 0;
        switch(App.getCurrentGame().getDate().getSeason()){
            case Season.AUTUMN -> price = item.getType().getFallPrice();
            case Season.WINTER -> price = item.getType().getWinterPrice();
            case Season.SUMMER -> price = item.getType().getSummerPrice();
            case Season.SPRING -> price = item.getType().getSpringPrice();
        }//TODO:handle the ones that are paid differently
        if(App.getCurrentPlayerLazy().getMoney() < price*Count){
            return new Result(false, "You do not have enough money to buy this product");
        }
        App.getCurrentPlayerLazy().setMoney(App.getCurrentPlayerLazy().getMoney() - price*Count);
        if(App.getCurrentPlayerLazy().getBackPack().getItemNames().containsKey(productName)){
            Item addToBackPack = App.getToolByName(productName);
            App.getCurrentPlayerLazy().getBackPack().getItems().put(addToBackPack,
                    App.getCurrentPlayerLazy().getBackPack().getItems().get(addToBackPack) + Count);
            return new Result(true, "You bought this product");
        }
        Item item1 = ItemBuilder.builder(productName);
        App.getCurrentPlayerLazy().getBackPack().getItems().put(item1, Count);
        return new Result(true, "You bought this product");

    }

    public void addCount(){

    }

}