package org.example.Server.controllers.movingPlayer;

import org.example.Client.controllers.MenusController.GameMenuController;
import org.example.Server.network.GameSessionManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.enums.Types.TypeOfTile;

import java.util.*;

public class UserLocationController {
    public static Result walkPlayer(String x, String y) {
        int targetX = Integer.parseInt(x);
        int targetY = Integer.parseInt(y);

        Location newLocation = App.getCurrentGame().getMainMap().findLocation(targetX, targetY);
        Location currentLocation = App.getCurrentGame().getCurrentPlayer().getUserLocation();
        Farm currentFarm = App.getCurrentGame().getCurrentPlayer().getOwnedFarm();

        for (Farm farm : App.getCurrentGame().getMainMap().getFarms()) {
            if (farm == currentFarm) {
                continue;
            }
            else{
                if(farm.getLocation().getLocationsInRectangle().contains(newLocation)){
                    return new Result(false, "you can't move to others farm.");
                }
            }
        }

        int[] result = bfsDistanceWithTurns(currentLocation, newLocation);
        int distance = result[0];
        int turns = result[1];

        if (distance == -1) {
            return new Result(false, "It is not possible to move to location " + x + " " + y + " because type of tile is " + newLocation.getTypeOfTile().name());
        }

        int energyNeeded = (distance + 10 * turns) / 20;
        int currentEnergy = App.getCurrentGame().getCurrentPlayer().getEnergy();

        if (energyNeeded > currentEnergy) {
            // Try partial movement: BFS with tracking energy
            Location finalReachable = bfsMaxReachable(currentLocation, targetX, targetY, currentEnergy);
            if (finalReachable == null) {
                return new Result(false, "You can't move at all with your current energy.");
            }

            App.getCurrentGame().getMainMap().findLocation(
                    currentLocation.getxAxis(), currentLocation.getyAxis()
            ).setObjectInTile(null);

            App.getCurrentGame().getCurrentPlayer().setUserLocation(finalReachable);
            App.getCurrentGame().getMainMap().findLocation(
                    finalReachable.getxAxis(), finalReachable.getyAxis()
            ).setObjectInTile(App.getCurrentGame().getCurrentPlayer());

            App.getCurrentGame().getCurrentPlayer().setEnergy(0);
            App.getCurrentGame().getCurrentPlayer().setHasCollapsed(true);
            System.out.println(App.getCurrentGame().getCurrentPlayer().getUser().getUserName() + " fainted on the way.");

            // Handle turn-based multiplayer
            if (App.getCurrentGame().isMultiplayer()) {
                // Get GameInstance and handle turn change
                GameSessionManager sessionManager = GameSessionManager.getInstance();
                if (sessionManager != null) {
                    var gameInstance = sessionManager.getCurrentGameInstance();
                    if (gameInstance != null) {
                        gameInstance.handleMultiplayerTurnChange();
                    } else {
                        // Fallback to direct game nextTurn
                        App.getCurrentGame().nextTurn();
                    }
                } else {
                    App.getCurrentGame().nextTurn();
                }
                return new Result(false, "You didn't have enough energy. You moved partially and fainted at "
                        + finalReachable.getxAxis() + ", " + finalReachable.getyAxis() + ". Turn passed to next player.");
            } else {
                // Single player behavior (unchanged)
                GameMenuController gameMenuController = App.getCurrentPlayerLazy().getPlayerController().getGameController();
                gameMenuController.nextTurn();
                return new Result(false, "You didn't have enough energy. You moved partially and fainted at "
                        + finalReachable.getxAxis() + ", " + finalReachable.getyAxis());
            }
        }

        App.getCurrentGame().getMainMap().findLocation(
                currentLocation.getxAxis(), currentLocation.getyAxis()
        ).setObjectInTile(null);

        App.getCurrentGame().getCurrentPlayer().setUserLocation(newLocation);
        App.getCurrentGame().getMainMap().findLocation(
                targetX, targetY
        ).setObjectInTile(App.getCurrentGame().getCurrentPlayer());
        App.getCurrentGame().getCurrentPlayer().setEnergy(currentEnergy - energyNeeded);

        // Check if energy reached zero after movement (for multiplayer turn-based system)
        if (App.getCurrentGame().isMultiplayer() && App.getCurrentGame().getCurrentPlayer().getEnergy() <= 0) {
            App.getCurrentGame().getCurrentPlayer().setHasCollapsed(true);

            // Get GameInstance and handle turn change
            GameSessionManager sessionManager = GameSessionManager.getInstance();
            if (sessionManager != null) {
                var gameInstance = sessionManager.getCurrentGameInstance();
                if (gameInstance != null) {
                    gameInstance.handleMultiplayerTurnChange();
                } else {
                    // Fallback to direct game nextTurn
                    App.getCurrentGame().nextTurn();
                }
            } else {
                App.getCurrentGame().nextTurn();
            }

            return new Result(true,
                    App.getCurrentGame().getCurrentPlayer().getUser().getUserName()
                            + " moved to new location " + x + " " + y
                            + " (distance = " + distance + ", turns = " + turns + ", energy cost = " + energyNeeded + ")"
                            + " and energy reached zero. Turn passed to next player."
            );
        }

        if(App.isLocationInPlace(newLocation, App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getShack().getLocation())){
            houseMenu();
        }
        return new Result(true,
                App.getCurrentGame().getCurrentPlayer().getUser().getUserName()
                        + " moved to new location " + x + " " + y
                        + " (distance = " + distance + ", turns = " + turns + ", energy cost = " + energyNeeded + ")"
        );
    }

    private static void houseMenu() {
        System.out.println("Hey!\n" + "you are at your home location");
        System.out.println("you can do this WORK in your house: ");
        System.out.println("1) crafting");
        System.out.println("2) cooking");
        System.out.println("3) access to your refrigerator");
    }

    private static int[] bfsDistanceWithTurns(Location start, Location end) {
        int maxX = 400, maxY = 400;
        boolean[][] visited = new boolean[maxX][maxY];

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start.getxAxis(), start.getyAxis(), 0, 0, 0, 0}); // x, y, dist, turns, dx, dy

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], dist = current[2], turns = current[3], dx = current[4], dy = current[5];

            if (x == end.getxAxis() && y == end.getyAxis()) {
                return new int[]{dist, turns};
            }

            if (x < 0 || y < 0 || x >= maxX || y >= maxY || visited[x][y]) continue;

            Location loc = App.getCurrentGame().getMainMap().findLocation(x, y);
            if (loc.getTypeOfTile() != TypeOfTile.GROUND && loc.getTypeOfTile() != TypeOfTile.PLOUGHED_LAND &&
                    loc.getTypeOfTile() != TypeOfTile.STORE && loc.getTypeOfTile() != TypeOfTile.HOUSE &&
                    loc.getTypeOfTile() != TypeOfTile.NPC_VILLAGE && loc.getTypeOfTile() != TypeOfTile.NPC_HOUSE) continue;

            visited[x][y] = true;

            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                int newTurn = (dx == dir[0] && dy == dir[1]) || (dx == 0 && dy == 0) ? turns : turns + 1;
                queue.add(new int[]{nx, ny, dist + 1, newTurn, dir[0], dir[1]});
            }
        }

        return new int[]{-1, -1};
    }

    private static Location bfsMaxReachable(Location start, int targetX, int targetY, int maxEnergy) {
        int maxX = 400, maxY = 400;
        boolean[][] visited = new boolean[maxX][maxY];

        int[][] directions = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
        };

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{start.getxAxis(), start.getyAxis(), 0, 0, 0, 0}); // x, y, dist, turns, dx, dy

        Location lastValid = start;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0], y = current[1], dist = current[2], turns = current[3], dx = current[4], dy = current[5];

            if (x < 0 || y < 0 || x >= maxX || y >= maxY || visited[x][y]) continue;

            Location loc = App.getCurrentGame().getMainMap().findLocation(x, y);
            if (loc.getTypeOfTile() != TypeOfTile.GROUND && loc.getTypeOfTile() != TypeOfTile.PLOUGHED_LAND &&
                    loc.getTypeOfTile() != TypeOfTile.STORE && loc.getTypeOfTile() != TypeOfTile.HOUSE &&
                    loc.getTypeOfTile() != TypeOfTile.NPC_VILLAGE && loc.getTypeOfTile() != TypeOfTile.NPC_HOUSE) continue;

            int energyNeeded = (dist + 10 * turns) / 20;
            if (energyNeeded > maxEnergy) continue;

            visited[x][y] = true;
            lastValid = loc;

            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];
                int newTurn = (dx == dir[0] && dy == dir[1]) || (dx == 0 && dy == 0) ? turns : turns + 1;
                queue.add(new int[]{nx, ny, dist + 1, newTurn, dir[0], dir[1]});
            }
        }
        return lastValid.equals(start) ? null : lastValid;
    }
}
