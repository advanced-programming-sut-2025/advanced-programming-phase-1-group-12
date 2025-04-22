package models;

import models.Fundementals.Location;
import models.MapDetails.GreenHouse;
import models.MapDetails.Lake;
import models.NPC.NPCvillage;
import models.Place.Farm;
import models.Place.Store;
import models.RelatedToUser.User;

import java.util.*;

public class map {
    private ArrayList<Farm> farms;
    public map() {
        initilizeFarms();
    }
    Random rand = new Random();

    private void initilizeFarms() { // size of farms are 30 * 35
        for (int i = 0; i < farms.size(); i++) {
            Location topLeft =  new Location(rand.nextInt(100), (rand.nextInt(10*i - 30*i + 1) + 30*i));
            Location downRight =  new Location((rand.nextInt(100) + 30), ((rand.nextInt(10*i - 30*i + 1) + 30*i) + 35));
            farms.get(i).setFarmLocation(new Location.LocationOfRectangle(topLeft, downRight));
        }
    }


}
