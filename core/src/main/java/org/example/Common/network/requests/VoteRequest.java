package org.example.Common.network.requests;

public class VoteRequest {
    private String gameId;
    private String voterId;
    private String voteId;
    private boolean vote; // true for yes, false for no

    public VoteRequest() {}

    public VoteRequest(String gameId, String voterId, String voteId, boolean vote) {
        this.gameId = gameId;
        this.voterId = voterId;
        this.voteId = voteId;
        this.vote = vote;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public String getVoteId() {
        return voteId;
    }

    public void setVoteId(String voteId) {
        this.voteId = voteId;
    }

    public boolean getVote() {
        return vote;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }
}

