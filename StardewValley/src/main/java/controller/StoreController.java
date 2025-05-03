package controller;

import controller.MenusController.GameMenuController;
import models.Animal.AnimalHome;
import models.Animal.FarmAnimals;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.*;
import models.Fundementals.LocationOfRectangle;
import models.Fundementals.Result;
import models.Place.Store;
import models.ProductsPackage.StoreProducts;
import models.enums.Types.TypeOfTile;

import java.util.List;
import java.util.Scanner;

public class StoreController {

    public Result buyAnimalBuilding(String buildingName, Location location) {
        if (!App.isLocationInPlace(location, App.getCurrentGame().getMainMap().getStores().get(3).getLocationOfRectangle())) {
            //TODO:money for building is not decreased.no money is said anywhere!
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
            case "Deluxe coop":
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
            case "Deluxe barn":
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
    public Store findStore(Location location){return null;}

    public boolean isStoreOpen(Store store, Date date){return false;}

    public void ShowProducts(Store store){}

    public void showTotalProducts(List<Store> stores){}

    public void buyProduct(String productName, int Count){}

    public StoreProducts getStoreProducts(String productName){return new StoreProducts();}
    public void addCount(){

    }


}
