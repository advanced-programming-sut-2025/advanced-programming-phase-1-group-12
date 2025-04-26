package models.Fundementals;

import models.MapDetails.Shack;
import models.Refrigrator;
import models.RelatedToUser.*;
import models.RelationShip;
import models.map;

import java.util.ArrayList;

public class Player {
    private User user;

    private Location userLocation;

    private Shack shack;

    private map map;

    private boolean isMarried;

    private int energy;

    public Refrigrator Refrigrator = new Refrigrator();

    private ArrayList<Ability> abilitis = new ArrayList<Ability>();

    private ArrayList<RelationShip> relationShips = new ArrayList<>();

    private ArrayList<RelationShip.Trade> trade = new ArrayList<>();


    public Player(User user, Location userLocation, Shack shack, map map, boolean isMarried,
                  Refrigrator refrigrator, ArrayList<Ability> abilitis, ArrayList<RelationShip> relationShips,
                  ArrayList<RelationShip.Trade> trade) {
        this.user = user;
        this.userLocation = userLocation;
        this.shack = shack;
        this.map = map;
        this.isMarried = isMarried;
        this.energy = 200;
        Refrigrator = refrigrator;
        this.abilitis = abilitis;
        this.relationShips = relationShips;
        this.trade = trade;
    }

    public void increaseEnergy(int amount) {
        if(energy ==200){
            return;
        }
        else if(energy <200){
            energy += amount;
        }
    }

    public void setEnergy(int amount) {
        energy = amount;
    }

    public int getEnergy() {
        return energy;
    }


    public User getUser() {
        return user;
    }
}
