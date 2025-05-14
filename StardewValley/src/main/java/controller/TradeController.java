package controller;

import models.Fundementals.Player;
import models.Item;
import models.RelationShips.RelationShip;
import models.RelationShips.Trade;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TradeController {
    private static List<Trade> trades = new ArrayList<>();

    public static Trade createTrade(Player requester, Player target, String type, Item item, int amount, int price) {
        Trade trade = new Trade(requester, target, type, item, amount, price);
        trades.add(trade);
        return trade;
    }
    public static Trade createTrade(Player requester, Player target, String type, Item item, int amount, Item targetItem, int targetAmount) {
        Trade trade = new Trade(requester, target, type, item, amount, targetItem, targetAmount);
        trades.add(trade);
        return trade;
    }

    public static List<Trade> getTradesForPlayer(Player player) {
        return trades.stream().filter(trade -> trade.getRequester().equals(player) ||
                trade.getTarget().equals(player)).collect(Collectors.toList());
    }

    public static List<Trade> getPendingTradesForTarget(Player player) {
        return trades.stream().filter(trade -> trade.getTarget().equals(player)
                && trade.getStatus().equals("pending")).collect(Collectors.toList());
    }

    public static List<Trade> getNewTradesForTarget(Player player) {
        return trades.stream()
                .filter(trade -> trade.getTarget().equals(player) && trade.getStatus().equals("pending") && trade.isNew())
                .collect(Collectors.toList());
    }

    public static Trade getTradeById(String id) {
        return trades.stream()
                .filter(trade -> trade.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static String acceptTrade(String id) {
        Trade trade = getTradeById(id);
        if (trade == null) {
            return "Trade not found with ID: " + id;
        }

        if (!trade.getStatus().equals("pending")) {
            return "This trade is already " + trade.getStatus();
        }

        Player requester = trade.getRequester();
        Player target = trade.getTarget();
        Item item = trade.getItem();
        int amount = trade.getAmount();

        if (trade.getType().equals("offer")) {
            if (requester.getBackPack().getItemByName(item.getName()) == null) {
                trade.setStatus("rejected");
                return "The requester no longer has the item: " + item.getName();
            }
        } else {
            if (target.getBackPack().getItemByName(item.getName()) == null) {
                trade.setStatus("rejected");
                return "You don't have the requested item: " + item.getName();
            }
        }

        if (trade.isMoneyTrade()) {
            int price = trade.getPrice();

            if (trade.getType().equals("offer")) {
                if (target.getMoney() < price) {
                    trade.setStatus("rejected");
                    return "You don't have enough money for this trade";
                }

                requester.getBackPack().decreaseItem(item, amount);
                if (target.getBackPack().getItemByName(item.getName()) != null) {
                    target.getBackPack().addItem(item, amount);
                } else {
                    target.getBackPack().addItem(new Item(item.getName(), item.getQuality(), item.getPrice()), amount);
                }

                target.decreaseMoney(price);
                requester.increaseMoney(price);
            } else {
                if (requester.getMoney() < price) {
                    trade.setStatus("rejected");
                    return "The requester doesn't have enough money for this trade";
                }
                target.getBackPack().decreaseItem(item, amount);
                if (requester.getBackPack().getItemByName(item.getName()) != null) {
                    requester.getBackPack().addItem(item, amount);
                } else {
                    requester.getBackPack().addItem(new Item(item.getName(), item.getQuality(), item.getPrice()), amount);
                }

                requester.decreaseMoney(price);
                target.increaseMoney(price);
            }
        } else {
            Item targetItem = trade.getTargetItem();
            int targetAmount = trade.getTargetAmount();

            if (trade.getType().equals("offer")) {
                if (target.getBackPack().getItemByName(targetItem.getName()) == null) {
                    trade.setStatus("rejected");
                    return "You don't have the requested item: " + targetItem.getName();
                }

                requester.getBackPack().decreaseItem(item, amount);
                if (target.getBackPack().getItemByName(item.getName()) != null) {
                    target.getBackPack().addItem(item, amount);
                } else {
                    target.getBackPack().addItem(new Item(item.getName(), item.getQuality(), item.getPrice()), amount);
                }

                target.getBackPack().decreaseItem(targetItem, targetAmount);
                if (requester.getBackPack().getItemByName(targetItem.getName()) != null) {
                    requester.getBackPack().addItem(targetItem, targetAmount);
                } else {
                    requester.getBackPack().addItem(new Item(item.getName(), item.getQuality(), item.getPrice()), targetAmount);
                }
            } else {
                if (requester.getBackPack().getItemByName(targetItem.getName()) == null) {
                    trade.setStatus("rejected");
                    return "The requester doesn't have the offered item: " + targetItem.getName();
                }

                target.getBackPack().decreaseItem(item, amount);
                if (requester.getBackPack().getItemByName(item.getName()) != null) {
                    requester.getBackPack().addItem(item, amount);
                } else {
                    requester.getBackPack().addItem(new Item(item.getName(), item.getQuality(), item.getPrice()), amount);
                }

                requester.getBackPack().decreaseItem(targetItem, targetAmount);
                if (target.getBackPack().getItemByName(targetItem.getName()) != null) {
                    target.getBackPack().addItem(targetItem, targetAmount);
                } else {
                    target.getBackPack().addItem(new Item(item.getName(), item.getQuality(), item.getPrice()), targetAmount);
                }
            }
        }

        trade.setStatus("accepted");

        RelationShip relationship = requester.findRelationShip(target);
        if (relationship != null) {
            relationship.increaseXP(50);
        }

        return "Trade accepted successfully!";
    }

    public static String rejectTrade(String id) {
        Trade trade = getTradeById(id);
        if (trade == null) {
            return "Trade not found with ID: " + id;
        }

        if (!trade.getStatus().equals("pending")) {
            return "This trade is already " + trade.getStatus();
        }

        trade.setStatus("rejected");

        Player requester = trade.getRequester();
        Player target = trade.getTarget();
        RelationShip relationship = requester.findRelationShip(target);
        if (relationship != null) {
            relationship.decreaseXP(30);
        }

        return "Trade rejected.";
    }

    public static void markTradesAsSeen(Player player) {
        getNewTradesForTarget(player).forEach(trade -> trade.setNew(false));
    }


    public static String getTradeHistory(Player player) {
        List<Trade> playerTrades = getTradesForPlayer(player);
        if (playerTrades.isEmpty()) {
            return "You have no trade history.";
        }

        StringBuilder history = new StringBuilder("Trade History:\n");
        for (Trade trade : playerTrades) {
            history.append("----------------------------\n");
            history.append(trade.toString()).append("\n");
        }

        return history.toString();
    }

    public static String getTradeList(Player player) {
        List<Trade> pendingTrades = getPendingTradesForTarget(player);
        if (pendingTrades.isEmpty()) {
            return "You have no pending trade requests.";
        }

        StringBuilder list = new StringBuilder("Pending Trade Requests:\n");
        for (Trade trade : pendingTrades) {
            list.append("----------------------------\n");
            list.append(trade.toString()).append("\n");
        }

        return list.toString();
    }

    public static String getTradeNotifications(Player player) {
        List<Trade> newTrades = getNewTradesForTarget(player);
        if (newTrades.isEmpty()) {
            return null;
        }

        StringBuilder notifications = new StringBuilder("You have new trade requests:\n");
        for (Trade trade : newTrades) {
            notifications.append("----------------------------\n");
            notifications.append(trade.toString()).append("\n");
        }

        markTradesAsSeen(player);
        return notifications.toString();
    }
}
