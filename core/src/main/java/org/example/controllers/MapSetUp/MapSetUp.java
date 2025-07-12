package org.example.controllers.MapSetUp;

import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.LocationOfRectangle;
import org.example.models.Place.Farm;
import org.example.models.Place.Store;
import org.example.models.ProductsPackage.StoreProducts;
import org.example.models.enums.Types.StoreProductsTypes;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.map;

import java.util.ArrayList;

public class MapSetUp {

    static FarmSetUp newFarmSetUp = new FarmSetUp();

    public static void initializeFarms() {
        for (int i = 0; i < 400; i++) {
            for (int j = 0; j < 400; j++) {
                Location location = new Location(i, j);
                location.setTypeOfTile(TypeOfTile.GROUND);
                App.getCurrentGame().getMainMap().getTilesOfMap().add(location);
            }
        }
        ArrayList<Farm> farms = new ArrayList<>();
        int farmWidth = 30;
        int farmHeight = 30;
        int id = 0;

        int[][] farmCorners = {{0, 0}, {370, 0}, {0, 370}, {370, 370}};

        for (int[] corner : farmCorners) {
            int startX = corner[0];
            int startY = corner[1];

            Location topLeft = new Location(startX, startY);
            Location downRight = new Location(startX + farmWidth, startY + farmHeight);
            LocationOfRectangle farmRectangle = new LocationOfRectangle(topLeft, downRight);

            Farm newFarm = new Farm(farmRectangle);
            farms.add(newFarm);

            newFarmSetUp.makeFarm(newFarm, id);
            id++;
        }
        App.getCurrentGame().getMainMap().setFarms(farms);
    }

    public static void showMapWithFarms(map newMap) {
        System.out.println("\nFarms and their owners:");
        for (Farm farm : newMap.getFarms()) {
            String ownerName = (farm.getOwner() != null) ? farm.getOwner().getUser().getUserName() : "No Owner";
            System.out.println("- Farm from (" +
                farm.getLocation().getTopLeftCorner().getxAxis() + "," +
                farm.getLocation().getTopLeftCorner().getyAxis() + ") to (" +
                farm.getLocation().getDownRightCorner().getxAxis() + "," +
                farm.getLocation().getDownRightCorner().getyAxis() + ") owned by " +
                ownerName);
        }
    }

    public static void storesSetUp() {

        //in case we may want to make it rectangle instead of square
        int storeWidth = 20;
        int storeHeight = 20; //   uppest side down side left side
        int[][] storeCorners = {{0, 50}, {0, 90}, {380, 50}, {380, 90}, {50, 0}, {90, 0}, {50, 380}};

        for (int i = 0; i < storeCorners.length; i++) {
            int startX = storeCorners[i][0];
            int startY = storeCorners[i][1];

            Location topLeft = new Location(startX, startY);
            Location downRight = new Location(startX + storeWidth, startY + storeHeight);
            LocationOfRectangle storeRectangle = new LocationOfRectangle(topLeft, downRight);
            //setting types as store
            for (int x = startX; x < startX + storeWidth; x++) {
                for (int y = startY; y < startY + storeHeight; y++) {
                    App.getCurrentGame().getMainMap().findLocation(x, y).setTypeOfTile(TypeOfTile.STORE);
                }
            }
            App.getCurrentGame().getMainMap().getStores().add(storeDetailsSetUp(i, storeRectangle));

        }
    }

    public static Store storeDetailsSetUp(int storeId, LocationOfRectangle storeLocation) {
        //TODO:products be set
        Store store;
        switch (storeId) {
            case 0:
                store = new Store(storeLocation, "Clint", "Blacksmith", 9, 16, new ArrayList<>());
                break;
            case 1:
                store = new Store(storeLocation, "Morris", "JojaMart", 9, 23, new ArrayList<>());
                break;
            case 2:
                store = new Store(storeLocation, "Pierre", "Pierre's General Store", 9, 17, new ArrayList<>());
                break;
            case 3:
                store = new Store(storeLocation, "Robin", "Carpenter's Shop", 9, 20, new ArrayList<>());
                break;
            case 4:
                store = new Store(storeLocation, "Willy", "Fish Shop", 9, 17, new ArrayList<>());
                break;
            case 5:
                store = new Store(storeLocation, "Marnie", "Marnie's Ranch", 9, 16, new ArrayList<>());
                break;
            case 6:
                store = new Store(storeLocation, "Gus", "The Stardrop Saloon", 12, 24, new ArrayList<>());
                break;
            default:
                store = null;
        }
        StoreProductsSetUp(store);
        return store;
    }

    public static void StoreProductsSetUp(Store store) {
        for(StoreProductsTypes type : StoreProductsTypes.values()) {
            if(type.getShop().equals(store.getNameOfStore())){
                StoreProducts storeProducts = new StoreProducts(type);
                store.getStoreProducts().add(storeProducts);
            }
        }
    }

    public static void NPCsetUp() {

        int npcWidth = 40;
        int npcHeight = 40;

        int StartX = 180;
        int startY = 180;

        for (int x = StartX; x < StartX + npcWidth; x++) {
            for (int y = startY; y < startY + npcHeight; y++) {
                App.getCurrentGame().getMainMap().findLocation(x, y).setTypeOfTile(TypeOfTile.NPC_VILLAGE);
            }
        }
    }
}
