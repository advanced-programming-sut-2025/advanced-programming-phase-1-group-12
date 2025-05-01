package models.Fundementals;

import models.MapDetails.Shack;
import models.Refrigrator;
import models.RelatedToUser.Ability;
import models.RelatedToUser.User;
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
    private boolean isEnergyUmlimited;
    private boolean hasCollapsed;
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
        Refrigrator = refrigrator;
        this.abilitis = abilitis;
        this.relationShips = relationShips;
        this.trade = trade;
        this.energy = 200;
        this.isEnergyUmlimited = false;
        this.hasCollapsed = false;
    }

    public User getUser() {
        return user;
    }

    public void setEnergy(int amount){
        this.energy = amount;
    }

    public void increaseEnergy(int amount){
        if(energy + amount > 200 && !isEnergyUmlimited){
            energy = 200;
        }else {
            energy += amount;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void setUnlimited(){
        this.isEnergyUmlimited = true;
    }

    public void collapse(){
        if(energy == 0){
            this.hasCollapsed = true;
        }
    }
}
