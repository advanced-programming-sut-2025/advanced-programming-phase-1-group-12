package controller.MapSetUp;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Place.Farm;
import models.enums.Types.TypeOfTile;
import models.map;

import java.util.ArrayList;
import java.util.Random;

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
}