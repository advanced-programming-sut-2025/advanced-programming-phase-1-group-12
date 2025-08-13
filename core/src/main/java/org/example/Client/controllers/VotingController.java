package org.example.Client.controllers;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Game;
import org.example.Client.network.NetworkCommandSender;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.StartVoteRequest;
import org.example.Common.network.requests.VoteRequest;
import org.example.Common.network.responses.VoteResponse;
import org.example.Common.network.events.VoteEvent;
import org.example.Client.views.VotingMenu;
import org.example.Client.Main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VotingController {
    private static VotingController instance;
    private VotingMenu votingMenu;
    private Map<String, VoteInfo> activeVotes = new ConcurrentHashMap<>();
    
    public static class VoteInfo {
        public String voteId;
        public String voteType;
        public String targetPlayerId;
        public String initiatorId;
        public String reason;
        public long startTime;
        public long endTime;
        public Map<String, Boolean> votes;
        public boolean isActive;
        public String result;
        
        public VoteInfo(String voteId, String voteType, String targetPlayerId, 
                       String initiatorId, String reason, long startTime, long endTime) {
            this.voteId = voteId;
            this.voteType = voteType;
            this.targetPlayerId = targetPlayerId;
            this.initiatorId = initiatorId;
            this.reason = reason;
            this.startTime = startTime;
            this.endTime = endTime;
            this.isActive = true;
        }
    }
    
    private VotingController() {
        // Network integration will be added later
    }
    
    public static VotingController getInstance() {
        if (instance == null) {
            instance = new VotingController();
        }
        return instance;
    }
    
    public void setVotingMenu(VotingMenu votingMenu) {
        this.votingMenu = votingMenu;
    }
    
    public void startKickVote(String targetPlayerId, String reason) {
        String gameId = getCurrentGameId();
        if (gameId == null) {
            return;
        }
        
        StartVoteRequest request = new StartVoteRequest(
            gameId,
            App.getLoggedInUser().getUserName(),
            targetPlayerId,
            "kick",
            reason
        );
        
        // Send the request via network
        sendVoteRequest(request);
    }
    
    public void startForceTerminateVote(String reason) {
        String gameId = getCurrentGameId();
        if (gameId == null) {
            return;
        }
        
        StartVoteRequest request = new StartVoteRequest(
            gameId,
            App.getLoggedInUser().getUserName(),
            null, // No target for force terminate
            "force_terminate",
            reason
        );
        
        // Send the request via network
        sendVoteRequest(request);
    }
    
    public void castVote(String voteId, boolean vote) {
        String gameId = getCurrentGameId();
        if (gameId == null) {
            return;
        }
        
        VoteRequest request = new VoteRequest(
            gameId,
            App.getLoggedInUser().getUserName(),
            voteId,
            vote
        );
        
        // Send the request via network
        sendVoteRequest(request);
    }
    
    private void sendVoteRequest(Object request) {
        // Network integration will be added later
        System.out.println("Vote request would be sent: " + request);
    }
    
    private String convertToJson(Object request) {
        // Simple JSON conversion for network requests
        if (request instanceof StartVoteRequest) {
            StartVoteRequest voteRequest = (StartVoteRequest) request;
            return String.format(
                "POST /vote/start {\"gameId\":\"%s\",\"initiatorId\":\"%s\",\"targetPlayerId\":\"%s\",\"voteType\":\"%s\",\"reason\":\"%s\"}",
                voteRequest.getGameId(),
                voteRequest.getInitiatorId(),
                voteRequest.getTargetPlayerId() != null ? voteRequest.getTargetPlayerId() : "",
                voteRequest.getVoteType(),
                voteRequest.getReason()
            );
        } else if (request instanceof VoteRequest) {
            VoteRequest voteRequest = (VoteRequest) request;
            return String.format(
                "POST /vote/cast {\"gameId\":\"%s\",\"voterId\":\"%s\",\"voteId\":\"%s\",\"vote\":%s}",
                voteRequest.getGameId(),
                voteRequest.getVoterId(),
                voteRequest.getVoteId(),
                voteRequest.getVote()
            );
        }
        return "";
    }
    
    public void handleVoteEvent(VoteEvent event) {
        String eventType = event.getEventType();
        
        switch (eventType) {
            case "vote_started":
                handleVoteStarted(event);
                break;
            case "vote_updated":
                handleVoteUpdated(event);
                break;
            case "vote_ended":
                handleVoteEnded(event);
                break;
            case "vote_result":
                handleVoteResult(event);
                break;
        }
    }
    
    private void handleVoteStarted(VoteEvent event) {
        VoteInfo voteInfo = new VoteInfo(
            event.getVoteId(),
            event.getVoteType(),
            event.getTargetPlayerId(),
            event.getInitiatorId(),
            event.getReason(),
            event.getStartTime(),
            event.getEndTime()
        );
        voteInfo.votes = event.getVotes();
        
        activeVotes.put(event.getVoteId(), voteInfo);
        
        if (votingMenu != null) {
            votingMenu.updateVoteStatus(
                event.getVoteId(),
                event.getVoteType(),
                event.getTargetPlayerId(),
                event.getStartTime(),
                event.getEndTime(),
                event.getVotes(),
                event.isActive()
            );
        }
    }
    
    private void handleVoteUpdated(VoteEvent event) {
        VoteInfo voteInfo = activeVotes.get(event.getVoteId());
        if (voteInfo != null) {
            voteInfo.votes = event.getVotes();
            
            if (votingMenu != null) {
                votingMenu.updateVoteStatus(
                    event.getVoteId(),
                    event.getVoteType(),
                    event.getTargetPlayerId(),
                    event.getStartTime(),
                    event.getEndTime(),
                    event.getVotes(),
                    event.isActive()
                );
            }
        }
    }
    
    private void handleVoteEnded(VoteEvent event) {
        VoteInfo voteInfo = activeVotes.get(event.getVoteId());
        if (voteInfo != null) {
            voteInfo.isActive = false;
            voteInfo.result = event.getResult();
            
            if (votingMenu != null) {
                votingMenu.updateVoteStatus(
                    event.getVoteId(),
                    event.getVoteType(),
                    event.getTargetPlayerId(),
                    event.getStartTime(),
                    event.getEndTime(),
                    event.getVotes(),
                    false
                );
            }
        }
    }
    
    private void handleVoteResult(VoteEvent event) {
        VoteInfo voteInfo = activeVotes.get(event.getVoteId());
        if (voteInfo != null) {
            voteInfo.isActive = false;
            voteInfo.result = event.getResult();
            
            // Handle the result
            if ("passed".equals(event.getResult())) {
                if ("kick".equals(event.getVoteType())) {
                    handlePlayerKicked(event.getTargetPlayerId());
                } else if ("force_terminate".equals(event.getVoteType())) {
                    handleGameTerminated();
                }
            }
            
            // Remove from active votes
            activeVotes.remove(event.getVoteId());
            
            if (votingMenu != null) {
                votingMenu.updateVoteStatus(
                    event.getVoteId(),
                    event.getVoteType(),
                    event.getTargetPlayerId(),
                    event.getStartTime(),
                    event.getEndTime(),
                    event.getVotes(),
                    false
                );
            }
        }
    }
    
    private void handlePlayerKicked(String playerId) {
        // Handle player being kicked from the game
        if (playerId.equals(App.getLoggedInUser().getUserName())) {
            // Current player was kicked
            // This would typically trigger a return to lobby or main menu
            System.out.println("You have been kicked from the game!");
        } else {
            // Another player was kicked
            System.out.println("Player " + playerId + " has been kicked from the game!");
        }
    }
    
    private void handleGameTerminated() {
        // Handle game being force terminated
        System.out.println("Game has been force terminated by vote!");
        // This would typically trigger a return to lobby or main menu
    }
    
    private String getCurrentGameId() {
        Game currentGame = App.getCurrentGame();
        if (currentGame != null) {
            // This would need to be implemented based on how game ID is stored
            // For now, return a placeholder
            return "current_game_id";
        }
        return null;
    }
    
    public boolean hasActiveVote() {
        return !activeVotes.isEmpty();
    }
    
    public VoteInfo getActiveVote() {
        if (!activeVotes.isEmpty()) {
            return activeVotes.values().iterator().next();
        }
        return null;
    }
    
    public void clearActiveVotes() {
        activeVotes.clear();
    }
}
