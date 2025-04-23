package models.RelatedToUser;

import models.*;
import models.Fundementals.Location;
import models.MapDetails.Shack;

import java.util.ArrayList;

public class User {
    private ArrayList<Game> games;
    private String userName;

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

    private ArrayList<Ability> abilitis = new ArrayList <Ability>();

    private int count =0;

    private ArrayList<RelationShip> relationShips = new ArrayList<>();

    private ArrayList<RelationShip.Trade>trade = new ArrayList<>();

    public void collapse(){}
}
