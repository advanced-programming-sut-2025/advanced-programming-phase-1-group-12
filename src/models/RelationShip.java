package models;

import models.RelatedToUser.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RelationShip {
    private int levelOfRelationShip = 0;

    private Map<User, User>twoParties = new HashMap<>();

    private ArrayList<Gift> gifts = new ArrayList<>();

    private ArrayList<String> talk = new ArrayList<>();

    private void talk_u(){

    }
    public class Trade {
        Map<User, User>twoParties = new HashMap<User, User>();

        private int price;

        private ArrayList<String>items = new ArrayList<>();

        //TODO:class item baad ezafe shavad
    }
    public class Gift {
        private int id;

        private int rate;
    }

    public int getLevelOfRelationShip() {
        return levelOfRelationShip;
    }

    public void setLevelOfRelationShip(int levelOfRelationShip) {
        this.levelOfRelationShip = levelOfRelationShip;
    }

    public Map<User, User> getTwoParties() {
        return twoParties;
    }

    public void setTwoParties(Map<User, User> twoParties) {
        this.twoParties = twoParties;
    }

    public ArrayList<Gift> getGifts() {
        return gifts;
    }

    public void setGifts(ArrayList<Gift> gifts) {
        this.gifts = gifts;
    }

    public ArrayList<String> getTalk() {
        return talk;
    }

    public void setTalk(ArrayList<String> talk) {
        this.talk = talk;
    }
}
