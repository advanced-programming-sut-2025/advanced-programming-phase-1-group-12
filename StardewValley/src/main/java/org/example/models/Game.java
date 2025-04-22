package models;

import models.RelatedToUser.User;

import java.util.ArrayList;
import java.util.Map;

public class Game {
    private Date date;
    private map map;
    private ArrayList<User> gameUsers;
    private Map<User, Integer> score;

    public Game(){
        this.date = new Date();
    }

    public Date getDate(){
        return this.date;
    }




}
