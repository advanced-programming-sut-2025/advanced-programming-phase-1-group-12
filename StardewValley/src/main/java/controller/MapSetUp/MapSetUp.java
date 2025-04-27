package controller.MapSetUp;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Place.Farm;
import models.map;

import java.util.ArrayList;
import java.util.Random;

public class MapSetUp {

    static FarmSetUp farmSetUp = new FarmSetUp();
    static Random rand = new Random();

    public static void initializeFarms() {
        ArrayList<Farm> farms = new ArrayList<>();
        int startX = 0;
        int startY = 0;
        int farmWidth = 30;
        int farmHeight = 30;

        for (int i = 0; i < App.getCurrentGame().getPlayers().size() - 1; i++) {
            Location topLeft = new Location(startX, startY);
            Location downRight = new Location(startX + farmWidth - 1, startY + farmHeight - 1);
            LocationOfRectangle farmRectangle = new LocationOfRectangle(topLeft, downRight);

            Farm newFarm = new Farm(farmRectangle, App.getCurrentGame().getPlayers().get(i));
            farms.add(newFarm);

            farmSetUp.makeFarm(newFarm);
            startX += farmWidth + 5;
            startY += farmHeight + 5;
        }
        App.getCurrentGame().setFarms(farms);
    }

    public static void showMapWithFarms(map newMap) {
        System.out.println("\nFarms and their owners:");
        for (Farm farm : newMap.getFarms()) {
            String ownerName = (farm.getOwner() != null) ? farm.getOwner().getUser().getUserName() : "No Owner";
            System.out.println("- Farm from (" +
                    farm.getFarmLocation().getTopLeftCorner().getxAxis() + "," +
                    farm.getFarmLocation().getTopLeftCorner().getyAxis() + ") to (" +
                    farm.getFarmLocation().getDownRightCorner().getxAxis() + "," +
                    farm.getFarmLocation().getDownRightCorner().getyAxis() + ") owned by " +
                    ownerName);
        }
    }
}
