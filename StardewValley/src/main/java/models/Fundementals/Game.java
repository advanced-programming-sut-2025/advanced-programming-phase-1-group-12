package models.Fundementals;

import controller.MapSetUp.MapSetUp;
import models.Date;
import models.Fundementals.Location;
import models.Place.Farm;
import models.RelatedToUser.User;
import models.map;

import java.util.Map;

public class Game {
    private Date date;
    private Map<User, Integer> score;
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

    public Map<Farm, User> getUserAndMap() {
        return userAndMap;
    }

    public map getMainMap() {
        return mainMap;
    }

    public void setUserAndMap(Map<Farm, User> userAndMap) {
        this.userAndMap = userAndMap;
    }

    public Location[] getTilesOfMap() {return null;}

    public Date getDate(){
        return this.date;
    }

}
