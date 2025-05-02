package models.Fundementals;

import models.BackPack;
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
    private int energy;
    private boolean isEnergyUnlimited;
    private boolean hasCollapsed;
    private BackPack backPack;

    public Refrigrator Refrigrator = new Refrigrator();
    private ArrayList<Ability> ability;
    private ArrayList<RelationShip> relationShips = new ArrayList<>();
    private ArrayList<RelationShip.Trade> trade = new ArrayList<>();
    private Farm ownedFarm;


    public Player(User user, Location userLocation, map map, boolean isMarried,
                  Refrigrator refrigrator, ArrayList<Ability> abilitis, ArrayList<RelationShip> relationShips,
                  ArrayList<RelationShip.Trade> trade, Farm ownedFarm) {
        this.user = user;
        this.userLocation = userLocation;
        this.map = map;
        this.isMarried = isMarried;
        this.energy = 200;
        this.isEnergyUnlimited = false;
        this.hasCollapsed = false;
        this.Refrigrator = refrigrator;
        this.ability = new ArrayList<>();
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


    public ArrayList<RelationShip.Trade> getTrade() {
        return trade;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public map getMap() {
        return map;
    }

    public ArrayList<RelationShip> getRelationShips() {
        return relationShips;
    }

    public Refrigrator getRefrigrator() {
        return Refrigrator;
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

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public Ability getAbilityByName(String name){
        for(Ability ability : ability){
            if(ability.getName().equals(name)){
                return ability;
            }
        }
        return null;
    }

    public void farming(){
        Ability ability = getAbilityByName("farming");
        if(ability != null){
            ability.increaseAmount(5);
        }
    }

    public void mining(){
        Ability ability = getAbilityByName("mining");
        if(ability != null){
            ability.increaseAmount(10);
        }
    }

    public void natureSurfing(){
        Ability ability = getAbilityByName("nature");
        if(ability != null){
            ability.increaseAmount(10);
        }
    }

    public void fishing(){
        Ability ability = getAbilityByName("fishing");
        if(ability != null){
            ability.increaseAmount(5);
        }
    }
}
