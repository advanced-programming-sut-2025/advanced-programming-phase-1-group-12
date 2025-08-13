package org.example.Common.network.events;

import java.util.Map;

public class VoteEvent {
    private String eventType; // "vote_started", "vote_updated", "vote_ended", "vote_result"
    private String voteId;
    private String voteType; // "kick" or "force_terminate"
    private String targetPlayerId;
    private String initiatorId;
    private String reason;
    private long startTime;
    private long endTime;
    private Map<String, Boolean> votes; // playerId -> vote (true for yes, false for no)
    private int yesVotes;
    private int noVotes;
    private int totalVotes;
    private int requiredVotes;
    private boolean isActive;
    private String result; // "passed", "failed", "timeout"

    public VoteEvent() {}

    public VoteEvent(String eventType, String voteId, String voteType, String targetPlayerId, 
                    String initiatorId, String reason, long startTime, long endTime, 
                    Map<String, Boolean> votes, int yesVotes, int noVotes, int totalVotes, 
                    int requiredVotes, boolean isActive, String result) {
        this.eventType = eventType;
        this.voteId = voteId;
        this.voteType = voteType;
        this.targetPlayerId = targetPlayerId;
        this.initiatorId = initiatorId;
        this.reason = reason;
        this.startTime = startTime;
        this.endTime = endTime;
        this.votes = votes;
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
        this.totalVotes = totalVotes;
        this.requiredVotes = requiredVotes;
        this.isActive = isActive;
        this.result = result;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }

    public String getTargetPlayerId() {
        return targetPlayerId;
    }

    public void setTargetPlayerId(String targetPlayerId) {
        this.targetPlayerId = targetPlayerId;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Map<String, Boolean> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, Boolean> votes) {
        this.votes = votes;
    }

    public int getYesVotes() {
        return yesVotes;
    }

    public void setYesVotes(int yesVotes) {
        this.yesVotes = yesVotes;
    }

    public int getNoVotes() {
        return noVotes;
    }

    public void setNoVotes(int noVotes) {
        this.noVotes = noVotes;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void setTotalVotes(int totalVotes) {
        this.totalVotes = totalVotes;
    }

    public int getRequiredVotes() {
        return requiredVotes;
    }

    public void setRequiredVotes(int requiredVotes) {
        this.requiredVotes = requiredVotes;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

