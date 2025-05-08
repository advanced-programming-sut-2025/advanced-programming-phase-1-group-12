package models.RelationShips;

import models.Fundementals.Player;
import models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Trade {
    private String id;
    private Player requester;
    private Player target;
    private String type; // request or offer
    private Item item;
    private int amount;
    private int price;
    private Item targetItem;
    private int targetAmount;
    private String status; // pending, accepted, rejected
    private boolean isNew;

    public Trade(Player requester, Player target, String type, Item item, int amount, int price) {
        this.id = UUID.randomUUID().toString().substring(0, 8); // Generate a short unique ID
        this.requester = requester;
        this.target = target;
        this.type = type;
        this.item = item;
        this.amount = amount;
        this.price = price;
        this.targetItem = null;
        this.targetAmount = 0;
        this.status = "pending";
        this.isNew = true;
    }

    public Trade(Player requester, Player target, String type, Item item, int amount, Item targetItem, int targetAmount) {
        this.id = UUID.randomUUID().toString().substring(0, 8); // Generate a short unique ID
        this.requester = requester;
        this.target = target;
        this.type = type;
        this.item = item;
        this.amount = amount;
        this.price = 0;
        this.targetItem = targetItem;
        this.targetAmount = targetAmount;
        this.status = "pending";
        this.isNew = true;
    }

    public String getId() {
        return id;
    }

    public Player getRequester() {
        return requester;
    }

    public Player getTarget() {
        return target;
    }

    public String getType() {
        return type;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }

    public Item getTargetItem() {
        return targetItem;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isMoneyTrade() {
        return targetItem == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trade ID: ").append(id).append("\n");
        sb.append("Type: ").append(type).append("\n");
        sb.append("Status: ").append(status).append("\n");

        if (type.equals("request")) {
            sb.append(requester.getUser().getUserName()).append(" requests ");
        } else {
            sb.append(requester.getUser().getUserName()).append(" offers ");
        }

        sb.append(amount).append(" ").append(item.getItemType().getName());

        if (isMoneyTrade()) {
            sb.append(" for ").append(price).append(" money");
        } else {
            sb.append(" for ").append(targetAmount).append(" ").append(targetItem.getItemType().getName());
        }

        return sb.toString();
    }
}