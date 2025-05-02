package models;

import models.Fundementals.Player;
import models.RelatedToUser.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationShip {
    private Player player1;
    private Player player2;
    private int friendshipLevel;
    private int XP;
    private List<String> talks;
    private Boolean hasBouquet;
    private Boolean areMarried;
    private Boolean hasTalked;
    private Boolean hasDealed;
    private Boolean hasGifted;
    private Boolean hasHugged;
    private Boolean isDealSuccessful;

    public RelationShip(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.talks = new ArrayList<>();
        this.friendshipLevel = 0;
        this.XP = 0;
        this.hasBouquet = false;
        this.areMarried = false;
        this.hasTalked = false;
        this.hasDealed = false;
        this.hasGifted = false;
        this.hasHugged = false;
        this.isDealSuccessful = null;
    }

    public int calculateLevelXP(){
        return (this.friendshipLevel + 1) * 100;
    }

    public boolean checkNeededXP(){
        return this.XP >= this.calculateLevelXP();
    }
    public boolean isXPFilled(){
        return this.XP ==calculateLevelXP();
    }

    public void increaseXP(int amount){
        this.XP += amount;
    }

    public void decreaseXP(int amount){
        this.XP-=amount;
    }


    public void increaseFriendshipLevel(){
        switch (this.friendshipLevel){
            case 0, 1:
                if(checkNeededXP()){
                    this.friendshipLevel++;
                }
                break;
            case 2:
                if(checkNeededXP() && hasBouquet){
                    this.friendshipLevel++;
                }
                break;
            case 3:
                if(checkNeededXP() && areMarried){
                    this.friendshipLevel++;
                }
                break;
            default: // cant be more than 4
                break;

        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public int getFriendshipLevel() {
        return friendshipLevel;
    }

    public int getXP() {
        return XP;
    }

    public void talk(String message){
        talks.add(message);
        this.XP +=20;
    }

    public String talkHistory(){
        StringBuilder result = new StringBuilder("Talk History: \n");
        for(String message: talks){
            result.append(message).append("\n");
        }
        return result.toString();
    }

    public void deal(){
        // not fully implemented
        if(isDealSuccessful){
            increaseXP(50);
        }
        else{
            decreaseXP(30);
        }
    }


}
