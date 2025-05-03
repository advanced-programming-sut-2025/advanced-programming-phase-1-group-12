package models.Fundementals;

import models.BackPack;
import models.Place.Farm;
import models.Refrigrator;
import models.RelatedToUser.Ability;
import models.RelatedToUser.User;
import models.RelationShip;

import java.util.ArrayList;

public class Player {
    private User user;
    private Location userLocation;
    private boolean isMarried;
    private int energy;
    public Refrigrator Refrigrator = new Refrigrator();
    private ArrayList<Ability> abilitis = new ArrayList<Ability>();
    private ArrayList<RelationShip> relationShips = new ArrayList<>();
    private Farm ownedFarm;
    private BackPack backPack;
    private boolean isEnergyUnlimited;
    private boolean hasCollapsed;
    private int money;
    private Player partner;


    public Player(User user, Location userLocation, boolean isMarried,Refrigrator refrigrator,
                  ArrayList<Ability> abilitis, ArrayList<RelationShip> relationShips, ArrayList<RelationShip.Trade> trade,
                  Farm ownedFarm, BackPack backPack, boolean isEnergyUnlimited, boolean hasCollapsed ) {
        this.user = user;
        this.userLocation = userLocation;
        this.isMarried = isMarried;
        this.energy = 200;
        this.Refrigrator = refrigrator;
        this.abilitis = abilitis;
        this.relationShips = relationShips;
        this.ownedFarm = ownedFarm;
        this.backPack = backPack;
        this.isEnergyUnlimited = isEnergyUnlimited;
        this.hasCollapsed = hasCollapsed;
        this.money = 0;
        this.partner = null;
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

    public BackPack getBackPack() {
        return backPack;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }


    public void setEnergy(int energy) {
        this.energy = energy;
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

    public RelationShip findRelationShip(Player player2){
        for(RelationShip relationShip : relationShips){
            if(relationShip.getPlayer1().equals(player2) || relationShip.getPlayer2().equals(player2)){
                return relationShip;
            }
        }
        return null;
    }

    public void setMarried(){
        isMarried = true;
    }

    public void decreaseMoney(int amount){
        if(isMarried){
            money -= amount / 2;
            partner.setMoney(partner.getMoney() - amount /2);
        }
        else {
            money -= amount;
        }
    }
    public int getMoney(){
        return money;
    }
    public void increaseMoney(int amount){
        if(isMarried){
            money += amount /2;
            partner.setMoney(partner.getMoney() + amount / 2);
        }
        else{
            money += amount;
        }
    }

    public void setPartner(Player partner){
        this.partner = partner;
    }

    public void setMoney(int money){
        this.money = money;
    }



}
