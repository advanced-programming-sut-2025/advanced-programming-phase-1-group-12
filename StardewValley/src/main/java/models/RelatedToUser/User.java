package models.RelatedToUser;

import models.*;
import models.Fundementals.Game;

import java.util.ArrayList;

public class User {
    private ArrayList<Game> games;
    private String userName;

    private String nickname;

    private String password;

    private String email;

    private String questionForSecurity;

    private String answerOfQuestionForSecurity;


    private boolean isFemale;

    public User(ArrayList<Game> games, String userName, String nickname, String password, String email,
                String questionForSecurity, String answerOfQuestionForSecurity, boolean isFemale) {
        this.games = games;
        this.userName = userName;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.questionForSecurity = questionForSecurity;
        this.answerOfQuestionForSecurity = answerOfQuestionForSecurity;
        this.isFemale = isFemale;
    }

    public void setQuestionForSecurity(String questionForSecurity) {
        this.questionForSecurity = questionForSecurity;
    }

    public void setAnswerOfQuestionForSecurity(String answerOfQuestionForSecurity) {
        this.answerOfQuestionForSecurity = answerOfQuestionForSecurity;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAnswerOfQuestionForSecurity() {
        return answerOfQuestionForSecurity;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ArrayList<Game> getGames() {
        return games;
    }
}