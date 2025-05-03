package controller;

import models.Animal.AnimalHome;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.*;
import models.Fundementals.Result;
import models.Place.Store;
import models.ProductsPackage.StoreProducts;
import models.enums.Types.TypeOfTile;

import java.util.List;

public class StoreController {

    public Result buyAnimalBuilding(String buildingName, Location location){
        if(!App.isLocationInPlace(location, App.getCurrentGame().getMainMap().getStores().get(3).getLocationOfRectangle())){
            //TODO:money for building is not decreased.no money is said anywhere!
            return new Result(false, "You are not in Carpenter's shop");
        }
        if(!location.getTypeOfTile().equals(TypeOfTile.GROUND)){
            return new Result(false, "You can not build this building here");
        }
        //normal coop, Deluxe coop, big coop, normal barn, Deluxe barn, big barn:
        switch (buildingName){
            case "normal coop":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(4, "normal coop", location));
                App.getCurrentGame().getMainMap().findLocation(location.getxAxis(), location.getyAxis()).setTypeOfTile(TypeOfTile.COOP);
                break;
            case "Deluxe coop":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(12, "Deluxe coop", location));
                App.getCurrentGame().getMainMap().findLocation(location.getxAxis(), location.getyAxis()).setTypeOfTile(TypeOfTile.COOP);
                break;
            case "big coop":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(8, "big coop", location));
                App.getCurrentGame().getMainMap().findLocation(location.getxAxis(), location.getyAxis()).setTypeOfTile(TypeOfTile.COOP);
                break;
            case "normal barn":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(4, "normal barn", location));
                App.getCurrentGame().getMainMap().findLocation(location.getxAxis(), location.getyAxis()).setTypeOfTile(TypeOfTile.BARN);
                break;
            case "Deluxe barn":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(12, "Deluxe barn", location));
                App.getCurrentGame().getMainMap().findLocation(location.getxAxis(), location.getyAxis()).setTypeOfTile(TypeOfTile.BARN);
                break;
            case "big barn":
                App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getAnimalHomes().add
                        (new AnimalHome(8, "big barn", location));
                App.getCurrentGame().getMainMap().findLocation(location.getxAxis(), location.getyAxis()).setTypeOfTile(TypeOfTile.BARN);
                break;
            default:
                return new Result(false, "You are not in Wrong building name");
        }
        return new Result(true, buildingName + "built successfully");
    }
    public Store findStore(Location location){return null;}

    public boolean isStoreOpen(Store store, Date date){return false;}

    public void ShowProducts(Store store){}

    public void showTotalProducts(List<Store> stores){}

    public void buyProduct(String productName, int Count){}

    public StoreProducts getStoreProducts(String productName){return new StoreProducts();}
    public void addCount(){}
}