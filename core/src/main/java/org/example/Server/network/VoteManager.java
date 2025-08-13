package org.example.Server.network;

import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.StartVoteRequest;
import org.example.Common.network.requests.VoteRequest;
import org.example.Common.network.responses.VoteResponse;
import org.example.Common.network.events.VoteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class VoteManager {
    private static final Logger logger = LoggerFactory.getLogger(VoteManager.class);

    private final ConcurrentHashMap<String, Vote> activeVotes; // voteId -> Vote
    private final ConcurrentHashMap<String, String> gameActiveVote; // gameId -> voteId
    private final ScheduledExecutorService scheduler;
    private final GameInstance gameInstance;

    private static final long VOTE_DURATION_SECONDS = 60; // 1 minute
    private static final double VOTE_PASS_THRESHOLD = 0.6; // 60% majority required

    public VoteManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        this.activeVotes = new ConcurrentHashMap<>();
        this.gameActiveVote = new ConcurrentHashMap<>();
        this.scheduler = java.util.concurrent.Executors.newScheduledThreadPool(1);
    }

    public NetworkResult<VoteResponse> startVote(StartVoteRequest request) {
        try {
            String gameId = request.getGameId();
            String initiatorId = request.getInitiatorId();
            String targetPlayerId = request.getTargetPlayerId();
            String voteType = request.getVoteType();
            String reason = request.getReason();

            // Check if there's already an active vote in this game
            if (gameActiveVote.containsKey(gameId)) {
                return NetworkResult.error("A vote is already in progress in this game");
            }

            // Validate vote type
            if (!voteType.equals("kick") && !voteType.equals("force_terminate")) {
                return NetworkResult.error("Invalid vote type. Must be 'kick' or 'force_terminate'");
            }

            // For kick votes, validate target player
            if (voteType.equals("kick")) {
                if (targetPlayerId == null || targetPlayerId.isEmpty()) {
                    return NetworkResult.error("Target player ID is required for kick votes");
                }
                if (targetPlayerId.equals(initiatorId)) {
                    return NetworkResult.error("Cannot vote to kick yourself");
                }
                if (!gameInstance.hasPlayer(targetPlayerId)) {
                    return NetworkResult.error("Target player is not in this game");
                }
            }

            // Get all players in the game
            Set<String> players = gameInstance.getAllPlayerIds();
            if (players.size() < 2) {
                return NetworkResult.error("Need at least 2 players to start a vote");
            }

            // Create vote
            String voteId = UUID.randomUUID().toString();
            long startTime = System.currentTimeMillis();
            long endTime = startTime + (VOTE_DURATION_SECONDS * 1000);

            Vote vote = new Vote(voteId, voteType, targetPlayerId, initiatorId, reason, 
                               startTime, endTime, players, gameId);

            // Store vote
            activeVotes.put(voteId, vote);
            gameActiveVote.put(gameId, voteId);

            // Schedule vote timeout
            ScheduledFuture<?> timeoutTask = scheduler.schedule(() -> {
                resolveVote(voteId, "timeout");
            }, VOTE_DURATION_SECONDS, TimeUnit.SECONDS);

            vote.setTimeoutTask(timeoutTask);

            logger.info("Vote started: {} for {} in game {}", voteType, 
                       targetPlayerId != null ? targetPlayerId : "force_terminate", gameId);

            // Create response
            VoteResponse response = createVoteResponse(vote);
            return NetworkResult.success("Vote started successfully", response);

        } catch (Exception e) {
            logger.error("Error starting vote", e);
            return NetworkResult.error("Failed to start vote: " + e.getMessage());
        }
    }

    public NetworkResult<VoteResponse> castVote(VoteRequest request) {
        try {
            String voteId = request.getVoteId();
            String voterId = request.getVoterId();
            boolean vote = request.getVote();

            Vote activeVote = activeVotes.get(voteId);
            if (activeVote == null) {
                return NetworkResult.error("Vote not found or already ended");
            }

            if (!activeVote.isActive()) {
                return NetworkResult.error("Vote has already ended");
            }

            if (!activeVote.hasPlayer(voterId)) {
                return NetworkResult.error("You are not eligible to vote in this game");
            }

            if (activeVote.hasVoted(voterId)) {
                return NetworkResult.error("You have already voted");
            }

            // Cast the vote
            activeVote.castVote(voterId, vote);

            logger.info("Vote cast: {} by {} for vote {}", vote ? "YES" : "NO", voterId, voteId);

            // Check if vote can be resolved early
            if (activeVote.canResolveEarly()) {
                resolveVote(voteId, activeVote.getResult());
            }

            // Create response
            VoteResponse response = createVoteResponse(activeVote);
            return NetworkResult.success("Vote cast successfully", response);

        } catch (Exception e) {
            logger.error("Error casting vote", e);
            return NetworkResult.error("Failed to cast vote: " + e.getMessage());
        }
    }

    private void resolveVote(String voteId, String result) {
        Vote vote = activeVotes.get(voteId);
        if (vote == null || !vote.isActive()) {
            return;
        }

        vote.setActive(false);
        vote.setResult(result);

        // Cancel timeout task if it exists
        if (vote.getTimeoutTask() != null) {
            vote.getTimeoutTask().cancel(false);
        }

        // Remove from active votes
        activeVotes.remove(voteId);
        gameActiveVote.remove(vote.getGameId());

        logger.info("Vote resolved: {} with result {}", voteId, result);

        // Handle vote result
        if (result.equals("passed")) {
            if (vote.getVoteType().equals("kick")) {
                handleKickVotePassed(vote);
            } else if (vote.getVoteType().equals("force_terminate")) {
                handleForceTerminateVotePassed(vote);
            }
        }

        // Broadcast vote result to all players
        broadcastVoteResult(vote);
    }

    private void handleKickVotePassed(Vote vote) {
        String targetPlayerId = vote.getTargetPlayerId();
        logger.info("Kick vote passed for player: {}", targetPlayerId);
        
        // Kick the player from the game
        gameInstance.kickPlayer(targetPlayerId);
    }

    private void handleForceTerminateVotePassed(Vote vote) {
        logger.info("Force terminate vote passed for game: {}", vote.getGameId());
        
        // Terminate the game
        gameInstance.forceTerminate();
    }

    private void broadcastVoteResult(Vote vote) {
        VoteEvent event = new VoteEvent(
            "vote_result",
            vote.getVoteId(),
            vote.getVoteType(),
            vote.getTargetPlayerId(),
            vote.getInitiatorId(),
            vote.getReason(),
            vote.getStartTime(),
            vote.getEndTime(),
            vote.getVotes(),
            vote.getYesVotes(),
            vote.getNoVotes(),
            vote.getTotalVotes(),
            vote.getRequiredVotes(),
            false,
            vote.getResult()
        );

        gameInstance.broadcastToAllPlayers(event);
    }

    private VoteResponse createVoteResponse(Vote vote) {
        return new VoteResponse(
            true,
            "Vote operation successful",
            vote.getVoteId(),
            vote.getVoteType(),
            vote.getTargetPlayerId(),
            vote.getInitiatorId(),
            vote.getReason(),
            vote.getStartTime(),
            vote.getEndTime(),
            vote.getVotes(),
            vote.getYesVotes(),
            vote.getNoVotes(),
            vote.getTotalVotes(),
            vote.getRequiredVotes(),
            vote.isActive()
        );
    }

    public VoteEvent createVoteEvent(Vote vote, String eventType) {
        return new VoteEvent(
            eventType,
            vote.getVoteId(),
            vote.getVoteType(),
            vote.getTargetPlayerId(),
            vote.getInitiatorId(),
            vote.getReason(),
            vote.getStartTime(),
            vote.getEndTime(),
            vote.getVotes(),
            vote.getYesVotes(),
            vote.getNoVotes(),
            vote.getTotalVotes(),
            vote.getRequiredVotes(),
            vote.isActive(),
            vote.getResult()
        );
    }

    public boolean hasActiveVote(String gameId) {
        return gameActiveVote.containsKey(gameId);
    }

    public Vote getActiveVote(String gameId) {
        String voteId = gameActiveVote.get(gameId);
        return voteId != null ? activeVotes.get(voteId) : null;
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Inner Vote class
    public static class Vote {
        private final String voteId;
        private final String voteType;
        private final String targetPlayerId;
        private final String initiatorId;
        private final String reason;
        private final long startTime;
        private final long endTime;
        private final Set<String> eligiblePlayers;
        private final String gameId;
        private final Map<String, Boolean> votes;
        private boolean isActive;
        private String result;
        private ScheduledFuture<?> timeoutTask;

        public Vote(String voteId, String voteType, String targetPlayerId, String initiatorId, 
                   String reason, long startTime, long endTime, Set<String> eligiblePlayers, String gameId) {
            this.voteId = voteId;
            this.voteType = voteType;
            this.targetPlayerId = targetPlayerId;
            this.initiatorId = initiatorId;
            this.reason = reason;
            this.startTime = startTime;
            this.endTime = endTime;
            this.eligiblePlayers = new HashSet<>(eligiblePlayers);
            this.gameId = gameId;
            this.votes = new ConcurrentHashMap<>();
            this.isActive = true;
        }

        public void castVote(String playerId, boolean vote) {
            votes.put(playerId, vote);
        }

        public boolean hasVoted(String playerId) {
            return votes.containsKey(playerId);
        }

        public boolean hasPlayer(String playerId) {
            return eligiblePlayers.contains(playerId);
        }

        public boolean canResolveEarly() {
            int totalVotes = votes.size();
            int yesVotes = (int) votes.values().stream().filter(v -> v).count();
            int noVotes = totalVotes - yesVotes;
            
            // Can resolve early if majority is reached or if no votes can change the outcome
            int remainingVotes = eligiblePlayers.size() - totalVotes;
            
            // If yes votes already exceed 60% of total eligible players
            if (yesVotes >= Math.ceil(eligiblePlayers.size() * VOTE_PASS_THRESHOLD)) {
                return true;
            }
            
            // If no votes already exceed 40% of total eligible players (making it impossible for yes to reach 60%)
            if (noVotes > eligiblePlayers.size() * (1 - VOTE_PASS_THRESHOLD)) {
                return true;
            }
            
            return false;
        }

        public String getResult() {
            if (!isActive) {
                return result;
            }
            
            int totalVotes = votes.size();
            int yesVotes = (int) votes.values().stream().filter(v -> v).count();
            
            if (yesVotes >= Math.ceil(eligiblePlayers.size() * VOTE_PASS_THRESHOLD)) {
                return "passed";
            } else if (totalVotes == eligiblePlayers.size()) {
                return "failed";
            }
            
            return null; // Still in progress
        }

        // Getters
        public String getVoteId() { return voteId; }
        public String getVoteType() { return voteType; }
        public String getTargetPlayerId() { return targetPlayerId; }
        public String getInitiatorId() { return initiatorId; }
        public String getReason() { return reason; }
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public Set<String> getEligiblePlayers() { return eligiblePlayers; }
        public String getGameId() { return gameId; }
        public Map<String, Boolean> getVotes() { return votes; }
        public boolean isActive() { return isActive; }

        public ScheduledFuture<?> getTimeoutTask() { return timeoutTask; }

        public int getYesVotes() {
            return (int) votes.values().stream().filter(v -> v).count();
        }

        public int getNoVotes() {
            return (int) votes.values().stream().filter(v -> !v).count();
        }

        public int getTotalVotes() {
            return votes.size();
        }

        public int getRequiredVotes() {
            return eligiblePlayers.size();
        }

        // Setters
        public void setActive(boolean active) { this.isActive = active; }
        public void setResult(String result) { this.result = result; }
        public void setTimeoutTask(ScheduledFuture<?> timeoutTask) { this.timeoutTask = timeoutTask; }
    }
}
