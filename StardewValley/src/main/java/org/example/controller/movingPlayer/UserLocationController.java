package controller.movingPlayer;

import models.Fundementals.Location;
import models.Fundementals.Result;

import java.util.ArrayList;

public class UserLocationController {
    //for maintancay(behinegi)
    public boolean isDestinationValid(Location destinatingLocation) {
        return false;
    }

    //if returns null path does not exist
    //TODO:write this function in a way that it would return shortest one(DFS)
    public ArrayList<Location> path(Location originLocation, Location destinationLocation) {
        return null;
    }

    //for showing the process of moving to destination
    //TODO:  gets setter one by one
    public void goToDestinationProcess(ArrayList<Location> path) {

    }

    public Result printMap(Location location, int size) {}

}
