package org.example.Server.network;

import org.example.Common.models.Trade;
import org.example.Common.models.TradeHistory;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TradeManager {
    private static final Logger logger = LoggerFactory.getLogger(TradeManager.class);
    
    private final Map<String, Trade> activeTrades;
    private final Map<String, TradeHistory> playerTradeHistories;
    
    public TradeManager() {
        this.activeTrades = new ConcurrentHashMap<>();
        this.playerTradeHistories = new ConcurrentHashMap<>();
    }
    
    /**
     * Create a new trade between two players
     */
    public Trade createTrade(String initiatorUsername, String targetUsername) {
        Trade trade = new Trade(initiatorUsername, targetUsername);
        activeTrades.put(trade.getTradeId(), trade);
        
        // Initialize trade histories if they don't exist
        if (!playerTradeHistories.containsKey(initiatorUsername)) {
            playerTradeHistories.put(initiatorUsername, new TradeHistory(initiatorUsername));
        }
        if (!playerTradeHistories.containsKey(targetUsername)) {
            playerTradeHistories.put(targetUsername, new TradeHistory(targetUsername));
        }
        
        logger.info("Created trade {} between {} and {}", trade.getTradeId(), initiatorUsername, targetUsername);
        return trade;
    }
    
    /**
     * Get a trade by ID
     */
    public Trade getTradeById(String tradeId) {
        return activeTrades.get(tradeId);
    }
    
    /**
     * Get all trades for a player
     */
    public List<Trade> getTradesForPlayer(String username) {
        List<Trade> playerTrades = new ArrayList<>();
        for (Trade trade : activeTrades.values()) {
            if (trade.getInitiatorUsername().equals(username) || trade.getTargetUsername().equals(username)) {
                playerTrades.add(trade);
            }
        }
        return playerTrades;
    }
    
    /**
     * Accept a trade request
     */
    public boolean acceptTrade(String tradeId) {
        Trade trade = activeTrades.get(tradeId);
        if (trade != null && trade.getStatus() == Trade.TradeStatus.PENDING) {
            trade.setStatus(Trade.TradeStatus.ACCEPTED);
            logger.info("Trade {} accepted", tradeId);
            return true;
        }
        return false;
    }
    
    /**
     * Decline a trade request
     */
    public boolean declineTrade(String tradeId) {
        Trade trade = activeTrades.get(tradeId);
        if (trade != null && trade.getStatus() == Trade.TradeStatus.PENDING) {
            trade.setStatus(Trade.TradeStatus.DECLINED);
            logger.info("Trade {} declined", tradeId);
            return true;
        }
        return false;
    }
    
    /**
     * Cancel a trade
     */
    public boolean cancelTrade(String tradeId) {
        Trade trade = activeTrades.get(tradeId);
        if (trade != null) {
            trade.setStatus(Trade.TradeStatus.CANCELLED);
            logger.info("Trade {} cancelled", tradeId);
            return true;
        }
        return false;
    }
    
    /**
     * Complete a trade
     */
    public boolean completeTrade(String tradeId) {
        Trade trade = activeTrades.get(tradeId);
        if (trade != null && trade.getStatus() == Trade.TradeStatus.ACCEPTED) {
            trade.setStatus(Trade.TradeStatus.COMPLETED);
            trade.setCompletedAt(java.time.LocalDateTime.now());
            
            // Add to trade histories
            TradeHistory initiatorHistory = playerTradeHistories.get(trade.getInitiatorUsername());
            TradeHistory targetHistory = playerTradeHistories.get(trade.getTargetUsername());
            
            if (initiatorHistory != null) {
                initiatorHistory.addTrade(trade);
            }
            if (targetHistory != null) {
                targetHistory.addTrade(trade);
            }
            
            // Remove from active trades
            activeTrades.remove(tradeId);
            
            logger.info("Trade {} completed", tradeId);
            return true;
        }
        return false;
    }
    
    /**
     * Update trade items
     */
    public boolean updateTradeItems(String tradeId, String playerUsername, Map<String, Integer> items) {
        Trade trade = activeTrades.get(tradeId);
        if (trade != null && trade.isActive()) {
            if (trade.getInitiatorUsername().equals(playerUsername)) {
                // Update initiator items
                trade.getInitiatorItems().clear();
                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                    // Create a sample item (in real implementation, this would be a real Item object)
                    Item item = new Item(entry.getKey(), Quality.NORMAL, 100);
                    trade.getInitiatorItems().put(item, entry.getValue());
                }
                logger.info("Updated initiator items for trade {}", tradeId);
                return true;
            } else if (trade.getTargetUsername().equals(playerUsername)) {
                // Update target items
                trade.getTargetItems().clear();
                for (Map.Entry<String, Integer> entry : items.entrySet()) {
                    // Create a sample item (in real implementation, this would be a real Item object)
                    Item item = new Item(entry.getKey(), Quality.NORMAL, 100);
                    trade.getTargetItems().put(item, entry.getValue());
                }
                logger.info("Updated target items for trade {}", tradeId);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get trade history for a player
     */
    public TradeHistory getTradeHistory(String username) {
        return playerTradeHistories.getOrDefault(username, new TradeHistory(username));
    }
    
    /**
     * Get pending trade requests for a player
     */
    public List<Trade> getPendingTradeRequests(String username) {
        List<Trade> pendingTrades = new ArrayList<>();
        for (Trade trade : activeTrades.values()) {
            if (trade.getTargetUsername().equals(username) && trade.getStatus() == Trade.TradeStatus.PENDING) {
                pendingTrades.add(trade);
            }
        }
        return pendingTrades;
    }
    
    /**
     * Get active trades for a player
     */
    public List<Trade> getActiveTrades(String username) {
        List<Trade> activePlayerTrades = new ArrayList<>();
        for (Trade trade : activeTrades.values()) {
            if ((trade.getInitiatorUsername().equals(username) || trade.getTargetUsername().equals(username)) 
                && trade.isActive()) {
                activePlayerTrades.add(trade);
            }
        }
        return activePlayerTrades;
    }
} 