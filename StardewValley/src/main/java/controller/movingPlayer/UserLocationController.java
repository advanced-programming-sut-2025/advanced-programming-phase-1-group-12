package controller.movingPlayer;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Place.Farm;
import models.enums.Types.TypeOfTile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class UserLocationController {
    public static Result walkPlayer(String x, String y) {
        int targetX = Integer.parseInt(x);
        int targetY = Integer.parseInt(y);

        Location newLocation = App.getCurrentGame().getMainMap().findLocation(targetX, targetY);
        Location currentLocation = App.getCurrentGame().getCurrentPlayer().getUserLocation();
        Farm currentFarm = App.getCurrentGame().getCurrentPlayer().getOwnedFarm();
        if(!isInPlayersFarm(newLocation, currentFarm)) {
            return new Result(false, "You dont have allow to access this farm.");
        }

        int minDistance = bfsDistance(currentLocation, newLocation);

        if (minDistance != -1) {
            if(minDistance / 20 > App.getCurrentGame().getCurrentPlayer().getEnergy()){
                System.out.println(App.getCurrentGame().getCurrentPlayer().getUser().getUserName() + " will faint soon.");

                return new Result(false, "you dont have enough energy to moving");
            }
            App.getCurrentGame().getMainMap().findLocation(
                    currentLocation.getxAxis(), currentLocation.getyAxis()
            ).setObjectInTile(null);

            App.getCurrentGame().getCurrentPlayer().setUserLocation(newLocation);
            App.getCurrentGame().getMainMap().findLocation(
                    targetX, targetY
            ).setObjectInTile(App.getCurrentGame().getCurrentPlayer());
            int newEnergy = App.getCurrentGame().getCurrentPlayer().getEnergy() - (minDistance / 20);
            App.getCurrentGame().getCurrentPlayer().setEnergy(newEnergy);

            return new Result(true,
                    App.getCurrentGame().getCurrentPlayer().getUser().getUserName()
                            + " moved to new location " + x + " " + y
                            + " (distance = " + minDistance + ")"
            );
        }
        return new Result(false, "It is not possible to move to location " + x + " " + y + " because type of tile is " + newLocation.getTypeOfTile().name());
    }

    private static int bfsDistance(Location start, Location end) {
        int maxX = 400, maxY = 400;
        boolean[][] visited = new boolean[maxX][maxY];

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start.getxAxis(), start.getyAxis(), 0});

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], dist = current[2];

            if (x == end.getxAxis() && y == end.getyAxis()) return dist;

            if (x < 0 || y < 0 || x >= maxX || y >= maxY || visited[x][y]) continue;

            Location loc = App.getCurrentGame().getMainMap().findLocation(x, y);
            if (loc.getTypeOfTile() != TypeOfTile.GROUND) continue;

            visited[x][y] = true;
            for (int[] dir : directions) {
                queue.add(new int[]{x + dir[0], y + dir[1], dist + 1});
            }
        }
        return -1;
    }

    private static boolean isInPlayersFarm(Location loc, Farm farm) {
        int x = loc.getxAxis();
        int y = loc.getyAxis();
        for(Farm f : App.getCurrentGame().getFarms()) {
            if(f == farm) continue;
            if(x >= f.getLocation().getTopLeftCorner().getxAxis() &&
                    x <= f.getLocation().getDownRightCorner().getxAxis() &&
                    y >= f.getLocation().getTopLeftCorner().getyAxis() &&
                    y <= f.getLocation().getDownRightCorner().getyAxis()){
                return false;
            }
        }
        return true;
    }

}
