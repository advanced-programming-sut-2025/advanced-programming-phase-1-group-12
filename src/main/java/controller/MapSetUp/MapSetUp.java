package controller.MapSetUp;

import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Place.Farm;
import models.map;

import java.util.Random;

public class MapSetUp {

    static FarmSetUp farmSetUp = new FarmSetUp();

    static Random rand = new Random();
    public static void initilizeFarms(map newMap) {
        for (int i = 0; i < 4; i++) {
            Location topLeft =  new Location(rand.nextInt(100), (rand.nextInt(10*i - 30*i + 1) + 30*i));
            Location downRight =  new Location((rand.nextInt(100) + 30), ((rand.nextInt(10*i - 30*i + 1) + 30*i) + 35));
            Farm newFarm = new Farm(new LocationOfRectangle(topLeft, downRight), null);
            newMap.getFarms().add(farmSetUp.makeFarm(newFarm)) ;
        }
    }

}
