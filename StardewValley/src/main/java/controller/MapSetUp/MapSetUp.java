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
    static Random rand = new Random();

    public static void initializeFarms() {
        for (int i = 0; i < 200; i++) {
            for (int j = 0; j < 200; j++) {
                Location location = new Location(i, j);
                location.setTypeOfTile(TypeOfTile.GROUND);
                App.getCurrentGame().getMainMap().getTilesOfMap().add(location);
            }
        }
        ArrayList<Farm> farms = new ArrayList<>();
        int startX = 0;
        int startY = 0;
        int farmWidth = 30;
        int farmHeight = 30;

        for (int i = 0; i < 4; i++) {
            Location topLeft = new Location(startX, startY);
            Location downRight = new Location(startX + farmWidth - 1, startY + farmHeight - 1);
            LocationOfRectangle farmRectangle = new LocationOfRectangle(topLeft, downRight);

            Farm newFarm = new Farm(farmRectangle);
            farms.add(newFarm);

            newFarmSetUp.makeFarm(newFarm);
            startX += farmWidth + 5;
            startY += farmHeight + 5;
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

