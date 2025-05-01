package models.Fundementals;

import models.BackPack;
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
    private boolean isEnergyUnlimited;
    private boolean hasCollapsed;
    private BackPack backPack;
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
        this.isEnergyUnlimited = false;
        this.hasCollapsed = false;
        this.backPack = null;
    }

    public User getUser() {
        return user;
    }

    public void setEnergy(int amount){
        this.energy = amount;
    }

    public void increaseEnergy(int amount){
        if(energy + amount > 200 && !isEnergyUnlimited){
            energy = 200;
        }else {
            energy += amount;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void setUnlimited(){
        this.isEnergyUnlimited = true;
    }

    public void collapse(){
        if(energy == 0){
            this.hasCollapsed = true;
        }
    }

    public BackPack getBackPack() {
        return backPack;
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
