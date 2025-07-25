package org.example.models.RelationShips;

import org.example.models.Fundementals.Player;
import org.example.models.Item;

import java.util.Date;

public class Gift {
    private Player sender;
    private Player receiver;
    private Item item;
    private int amount;
    private int rating; // 1-5, where 1 is hate and 5 is love
    private boolean isRated;
    private Date giftDate;

    public Gift(Player sender, Player receiver, Item item, int amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.item = item;
        this.amount = amount;
        this.rating = 0; // Not rated yet
        this.isRated = false;
        this.giftDate = new Date(); // Current date/time
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public int getRating() {
        return rating;
    }

    public boolean isRated() {
        return isRated;
    }

    public Date getGiftDate() {
        return giftDate;
    }

    public void setRating(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
            this.isRated = true;
        }
    }

    @Override
    public String toString() {
        return "Gift: " + amount + " " + item.getName() + " from " + sender.getUser().getUserName() + " to " + receiver.getUser().getUserName() + 
               (isRated ? " (Rated: " + rating + ")" : " (Not rated yet)");
    }
}
