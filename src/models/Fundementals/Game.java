package models.Fundementals;

import models.Place.Farm;
import models.RelatedToUser.User;
import models.map;
import controller.MapSetUp.MapSetUp;

import java.util.Map;

public class Game {
    private int gameId;
    private map mainMap = new map();
    Map<Farm, User> userAndMap;

    public Game(){
        MapSetUp.initilizeFarms(mainMap);

    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Map<map, User> getUserAndMap() {
        return userAndMap;
    }

    public map getMainMap() {
        return mainMap;
    }

    public void setUserAndMap(Map<map, User> userAndMap) {
        this.userAndMap = userAndMap;
    }

    public Location[] getTilesOfMap() {
    }
}
