package models.RelatedToUser;

import models.*;
import models.Fundementals.Location;
import models.MapDetails.Shack;

import java.util.ArrayList;

public class User {
    private ArrayList<Game> games;
    private String userName;

    private String nickname;

    private String password;

    private String email;

    private String questionForSecurity;

    private String answerOfQuestionForSecurity;

    private Location userLocation;

    private Shack shack;

    private map map;

    private boolean isMarried;

    private Energy energy;

    public Refrigrator Refrigrator = new Refrigrator();

    private ArrayList<Ability> abilitis = new ArrayList<Ability>();

    private int count = 0;

    private boolean isFemale;

    private ArrayList<RelationShip> relationShips = new ArrayList<>();

    private ArrayList<RelationShip.Trade> trade = new ArrayList<>();

    public void collapse() {
    }

    public User(ArrayList<Game> games, String userName, String password, String email, String questionForSecurity,
                String answerOfQuestionForSecurity, Location userLocation, Shack shack, map map, boolean isMarried,
                Energy energy, Refrigrator refrigrator, ArrayList<Ability> abilitis, int count, boolean isFemale,
                ArrayList<RelationShip> relationShips, ArrayList<RelationShip.Trade> trade, String nickname) {
        this.games = games;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.questionForSecurity = questionForSecurity;
        this.answerOfQuestionForSecurity = answerOfQuestionForSecurity;
        this.userLocation = userLocation;
        this.shack = shack;
        this.map = map;
        this.isMarried = isMarried;
        this.energy = energy;
        Refrigrator = refrigrator;
        this.abilitis = abilitis;
        this.count = count;
        this.isFemale = isFemale;
        this.relationShips = relationShips;
        this.trade = trade;
        this.nickname = nickname;
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