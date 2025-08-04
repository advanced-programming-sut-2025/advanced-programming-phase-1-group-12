package org.example.Common.models.RelationShips;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RelationShip {
    private Player player1;
    private Player player2;
    private int friendshipLevel;
    private int XP;
    private List<String> talks;
    private boolean hasBouquet;
    private boolean areMarried;
    private boolean hasTalked;
    private boolean hasDealed;
    private boolean hasGifted;
    private boolean hasHugged;
    private String askedRing;
    private List<Gift> receivedGifts;
    private List<Gift> sentGifts;
    private boolean hasAskedToMarry;

    public RelationShip() {
    }

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
        this.askedRing = null;
        this.receivedGifts = new ArrayList<>();
        this.sentGifts = new ArrayList<>();

        this.hasAskedToMarry = false;

        player1.addRelationShip(this);
        player2.addRelationShip(this);
    }

    public void askToMarry() {
        this.hasAskedToMarry = true;
    }

    public boolean hasAskedToMarry() {
        return this.hasAskedToMarry;
    }

    public int calculateLevelXP() {
        return (this.friendshipLevel + 1) * 100;
    }

    public boolean checkNeededXP() {
        return this.XP >= this.calculateLevelXP();
    }

    public boolean isXPFilled() {
        return this.XP == calculateLevelXP();
    }

    public void increaseXP(int amount) {
        this.XP += amount;
        increaseFriendshipLevel();
    }

    public void decreaseXP(int amount) {
        this.XP -= amount;
    }


    public void increaseFriendshipLevel() {
        switch (this.friendshipLevel) {
            case 0, 1:
                if (checkNeededXP()) {
                    this.XP -= calculateLevelXP();
                    this.friendshipLevel++;
                }
                break;
            case 2:
                if (checkNeededXP() && hasBouquet) {
                    this.XP -= calculateLevelXP();
                    this.friendshipLevel++;
                }
                break;
            case 3:
                if (checkNeededXP() && areMarried) {
                    this.XP -= calculateLevelXP();
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

    public void talk(String message) {
        talks.add(message);
        if (areMarried) {
            player1.increaseEnergy(50);
            player2.increaseEnergy(50);
        }
        this.XP += 20;
        this.hasTalked = true;
    }

    public String talkHistory() {
        StringBuilder result = new StringBuilder("Talk History: \n");
        for (String message : talks) {
            result.append(message).append("\n");
        }
        return result.toString();
    }

    public boolean gift(Item gift, int amount) {
        if (player1.getBackPack().getItemByName(gift.getName()) == null ||
                player1.getBackPack().getItemCount(gift) < amount) {
            return false;
        }

        player1.getBackPack().decreaseItem(gift, amount);
        player2.getBackPack().addItem(gift, amount);

        Gift giftRecord = new Gift(player1, player2, gift, amount);
        player1.findRelationShip(player2).sentGifts.add(giftRecord);
        player2.findRelationShip(player1).receivedGifts.add(giftRecord);

        this.hasGifted = true;
        return true;
    }

    public String listGifts() {
        if (receivedGifts.isEmpty()) {
            return "You haven't received any gifts yet.";
        }

        StringBuilder result = new StringBuilder("Received Gifts:\n");
        for (int i = 0; i < receivedGifts.size(); i++) {
            Gift gift = receivedGifts.get(i);
            result.append(i + 1).append(". ")
                    .append(gift.getAmount()).append(" ")
                    .append(gift.getItem().getName())
                    .append(" from ").append(gift.getSender().getUser().getUserName())
                    .append(gift.isRated() ? " (Rated: " + gift.getRating() + ")" : " (Not rated yet)")
                    .append("\n");
        }
        return result.toString();
    }

    public boolean rateGift(int giftNumber, int rating) {
        if (giftNumber < 1 || giftNumber > receivedGifts.size()) {
            return false;
        }

        if (rating < 1 || rating > 5) {
            return false;
        }

        Gift gift = receivedGifts.get(giftNumber - 1);
        if (gift.isRated()) {
            return false;
        }

        gift.setRating(rating);

        int friendshipChange = 15 + 30 * (rating - 3);
        if (friendshipChange > 0) {
            increaseXP(friendshipChange);
        } else {
            decreaseXP(Math.abs(friendshipChange));
        }

        return true;
    }

    public String giftHistory() {
        StringBuilder result = new StringBuilder("Gift History:\n");

        result.append("Sent Gifts:\n");
        if (sentGifts.isEmpty()) {
            result.append("  No gifts sent.\n");
        } else {
            for (Gift gift : sentGifts) {
                result.append("  ").append(gift.toString()).append("\n");
            }
        }

        result.append("\nReceived Gifts:\n");
        if (receivedGifts.isEmpty()) {
            result.append("  No gifts received.\n");
        } else {
            for (Gift gift : receivedGifts) {
                result.append("  ").append(gift.toString()).append("\n");
            }
        }

        return result.toString();
    }

    public void hug() {
        this.hasHugged = true;
        increaseXP(60);

    }

    public boolean arePlayersAdjacent() {
        return Math.abs(player1.getUserLocation().getxAxis() - player2.getUserLocation().getxAxis())
                + Math.abs(player1.getUserLocation().getyAxis() - player2.getUserLocation().getyAxis()) <= 2;
    }

    public void flower() {
        Item flower = player1.getBackPack().getItemByName("Flower");
        if (flower != null) {
            hasBouquet = true;
            player2.getBackPack().addItem(flower, 1);
            player1.getBackPack().decreaseItem(flower, 1);
        }
    }

    public Boolean askMarriage(String ring) {
        askToMarry();
        if (player1.getBackPack().getItemByName(ring) != null) {
            this.askedRing = ring;
            if (player2.getUser().isFemale()) {
                return true;
            }
        }

        return false;
    }

    public void marriage(String ring) {
        Item ringItem = player1.getBackPack().getItemByName(ring);
        player1.getBackPack().decreaseItem(ringItem, 1);
        player2.getBackPack().addItem(ringItem, 1);
        areMarried = true;
        player1.setMarried();
        player2.setMarried();
        player1.setPartner(player2);
        player2.setPartner(player1);
        increaseFriendshipLevel();
        mergeMoney();
    }

    public void mergeMoney() {
        int allMoney = player1.getMoney() + player2.getMoney();
        player1.setMoney(allMoney);
        player2.setMoney(allMoney);
    }

    public void setFriendshipLevel(int level) {
        this.friendshipLevel = level;
    }

    public String getAskedRing() {
        return askedRing;
    }

    public void reject() {
        friendshipLevel = 0;
        player1.setEnergy(player1.getEnergy() / 2);
        player1.setRejectDate(App.getCurrentGame().getDate());
    }

    public int calculateGiftXp(int rate) {
        int calculatedXp = 0;
        calculatedXp = (rate - 3) * 30 + 15;
        return calculatedXp;
    }

    public List<String> getTalks() {
        return talks;
    }

    public boolean isHasBouquet() {
        return hasBouquet;
    }

    public boolean isAreMarried() {
        return areMarried;
    }

    public boolean isHasTalked() {
        return hasTalked;
    }

    public boolean isHasDealed() {
        return hasDealed;
    }

    public boolean isHasGifted() {
        return hasGifted;
    }

    public boolean isHasHugged() {
        return hasHugged;
    }

    public List<Gift> getReceivedGifts() {
        return receivedGifts;
    }

    public List<Gift> getSentGifts() {
        return sentGifts;
    }

    public void setHasBouquet() {
        this.hasBouquet = true;
    }
}
