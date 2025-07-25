package org.example.models.RelatedToUser;

import org.example.models.Fundementals.Game;

import java.util.ArrayList;

public class User {
    private ArrayList<Game> games;
    private String userName;

    private String avatarPath;

    private String nickname;

    private String password;

    private String email;

    private String questionForSecurity;

    private String answerOfQuestionForSecurity;


    private boolean isFemale;

    private transient String token;

    private transient long tokenExpiration;

    public User() {}

    public User(ArrayList<Game> games, String userName, String nickname, String password, String email,
                String questionForSecurity, String answerOfQuestionForSecurity, boolean isFemale, String avatarPath) {
        this.games = games;
        this.userName = userName;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.questionForSecurity = questionForSecurity;
        this.answerOfQuestionForSecurity = answerOfQuestionForSecurity;
        this.isFemale = isFemale;
        this.avatarPath = avatarPath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public boolean isTokenExpired() {
        return System.currentTimeMillis() > tokenExpiration;
    }

    public String getPassword() {
        return password;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public String getUserName() {
        return userName;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestionForSecurity() {
        return questionForSecurity;
    }

    public void setQuestionForSecurity(String questionForSecurity) {
        this.questionForSecurity = questionForSecurity;
    }

    public String getAnswerOfQuestionForSecurity() {
        return answerOfQuestionForSecurity;
    }

    public void setAnswerOfQuestionForSecurity(String answerOfQuestionForSecurity) {
        this.answerOfQuestionForSecurity = answerOfQuestionForSecurity;
    }

    public boolean isFemale() {
        return isFemale;
    }

    public void setFemale(boolean female) {
        isFemale = female;
    }
}
