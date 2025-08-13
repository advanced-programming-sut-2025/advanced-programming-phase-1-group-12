package org.example.Common.network.requests;

public class StartVoteRequest {
    private String gameId;
    private String initiatorId;
    private String targetPlayerId; // null for force terminate vote
    private String voteType; // "kick" or "force_terminate"
    private String reason;

    public StartVoteRequest() {}

    public StartVoteRequest(String gameId, String initiatorId, String targetPlayerId, String voteType, String reason) {
        this.gameId = gameId;
        this.initiatorId = initiatorId;
        this.targetPlayerId = targetPlayerId;
        this.voteType = voteType;
        this.reason = reason;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public String getTargetPlayerId() {
        return targetPlayerId;
    }

    public void setTargetPlayerId(String targetPlayerId) {
        this.targetPlayerId = targetPlayerId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
