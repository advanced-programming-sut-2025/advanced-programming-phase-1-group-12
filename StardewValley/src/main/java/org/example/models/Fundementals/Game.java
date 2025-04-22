package models.Fundementals;

import models.RelatedToUser.User;
import models.map;

import java.util.Map;

public class Game {
    private int gameId;

    Map<map, User> userAndMap;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Map<map, User> getUserAndMap() {
        return userAndMap;
    }

    public void setUserAndMap(Map<map, User> userAndMap) {
        this.userAndMap = userAndMap;
    }
}
