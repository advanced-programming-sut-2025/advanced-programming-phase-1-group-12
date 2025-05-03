package models.RelationShips;

import models.Fundementals.Player;
import models.Item;

import java.util.ArrayList;
import java.util.List;

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
    private String askedRing;

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
        this.askedRing = null;
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
        hasTalked = true;
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

    public void gift(Item gift){

    }

    public void hug(){
        if(arePlayersAdjacent() && !hasHugged){
            hasHugged = true;
            increaseXP(60);
        }
    }

    public boolean arePlayersAdjacent(){
        return Math.abs(player1.getUserLocation().getxAxis() - player2.getUserLocation().getxAxis())
                + Math.abs(player1.getUserLocation().getyAxis() - player2.getUserLocation().getyAxis()) <= 2;
    }

    public void flower(){
        if(arePlayersAdjacent() && !hasBouquet){
            Item flower = player1.getBackPack().getItemByName("Flower");
            if(flower != null){
                hasBouquet = true;
                player2.getBackPack().addItem(flower, 1);
                player1.getBackPack().decreaseItem(flower, 1);
            }
        }
    }

    public Boolean askMarriage(String ring){
        if(arePlayersAdjacent() && !player1.isMarried() && !player1.getUser().isFemale() && friendshipLevel == 3){
            if(player1.getBackPack().getItemByName(ring) != null){
                this.askedRing = ring;
                if(player2.getUser().isFemale()){
                    return true;
                }
            }
        }
        return false;
    }

    public void marriage(String ring){
        Item ringItem = player1.getBackPack().getItemByName(ring);
        player1.getBackPack().decreaseItem(ringItem, 1);
        player2.getBackPack().addItem(ringItem, 1);
        areMarried = true;
        player1.setMarried();
        player2.setMarried();
        player1.setPartner(player2); player2.setPartner(player1);
        increaseFriendshipLevel();
        mergeMoney();
    }

    public void mergeMoney(){
        int allMoney = player1.getMoney() + player2.getMoney();
        player1.setMoney(allMoney);
        player2.setMoney(allMoney);
    }

    public void setFriendshipLevel(int level){
        this.friendshipLevel = level;
    }

    public String getAskedRing() {
        return askedRing;
    }

    public void reject(){
        friendshipLevel = 0;
        player1.setEnergy(player1.getEnergy() /2);
    }

}
