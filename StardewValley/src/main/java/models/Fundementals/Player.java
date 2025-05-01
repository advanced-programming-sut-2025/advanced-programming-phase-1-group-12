package models.Fundementals;

import models.MapDetails.Shack;
import models.Place.Farm;
import models.Refrigrator;
import models.RelatedToUser.Ability;
import models.RelatedToUser.Energy;
import models.RelatedToUser.User;
import models.RelationShip;
import models.map;

import java.util.ArrayList;

public class Player {
    private User user;

    private Location userLocation;

    private map map;

    private boolean isMarried;

    private Energy energy;

    public Refrigrator Refrigrator = new Refrigrator();

    private ArrayList<Ability> abilitis = new ArrayList<Ability>();

    private ArrayList<RelationShip> relationShips = new ArrayList<>();

    private ArrayList<RelationShip.Trade> trade = new ArrayList<>();

    private Farm ownedFarm;

    public void collapse() {
    }

    public Player(User user, Location userLocation, map map, boolean isMarried, Energy energy,
                  Refrigrator refrigrator, ArrayList<Ability> abilitis, ArrayList<RelationShip> relationShips,
                  ArrayList<RelationShip.Trade> trade, Farm ownedFarm) {
        this.user = user;
        this.userLocation = userLocation;
        this.map = map;
        this.isMarried = isMarried;
        this.energy = energy;
        Refrigrator = refrigrator;
        this.abilitis = abilitis;
        this.relationShips = relationShips;
        this.trade = trade;
        this.ownedFarm = ownedFarm;
    }

    public User getUser() {
        return user;
    }

    public Farm getOwnedFarm() {
        return ownedFarm;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setOwnedFarm(Farm farm) {
        this.ownedFarm = farm;
    }

    public ArrayList<Ability> getAbilitis() {
        return abilitis;
    }

    public ArrayList<RelationShip.Trade> getTrade() {
        return trade;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public map getMap() {
        return map;
    }

    public Energy getEnergy() {
        return energy;
    }

    public ArrayList<RelationShip> getRelationShips() {
        return relationShips;
    }

    public Refrigrator getRefrigrator() {
        return Refrigrator;
    }

    public void setAbilitis(ArrayList<Ability> abilitis) {
        this.abilitis = abilitis;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public void setMap(map map) {
        this.map = map;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public void setRefrigrator(Refrigrator refrigrator) {
        Refrigrator = refrigrator;
    }

    public void setRelationShips(ArrayList<RelationShip> relationShips) {
        this.relationShips = relationShips;
    }

    public void setTrade(ArrayList<RelationShip.Trade> trade) {
        this.trade = trade;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }
}
