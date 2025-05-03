package models.Fundementals;

import models.BackPack;
import models.MapDetails.Shack;
import models.Place.Farm;
import models.Refrigrator;
import models.RelatedToUser.Ability;
import models.RelatedToUser.Energy;
import models.RelatedToUser.User;
import models.RelationShip;
import models.enums.Types.SeedSeason;
import models.map;

import java.util.ArrayList;

public class Player {

    private User user;
    private Location userLocation;
    private boolean isMarried;
    private int energy;
    public Refrigrator Refrigrator = new Refrigrator();
    private ArrayList<Ability> abilitis = new ArrayList<Ability>();
    private ArrayList<RelationShip> relationShips = new ArrayList<>();
    private ArrayList<RelationShip.Trade> trade = new ArrayList<>();
    private Farm ownedFarm;
    private BackPack backPack;
    private boolean isEnergyUnlimited;
    private boolean hasCollapsed;


    public Player(User user, Location userLocation, boolean isMarried, Refrigrator refrigrator,
                  ArrayList<Ability> abilitis, ArrayList<RelationShip> relationShips, ArrayList<RelationShip.Trade> trade,
                  Farm ownedFarm, BackPack backPack, boolean isEnergyUnlimited, boolean hasCollapsed) {
        this.user = user;
        this.userLocation = userLocation;
        this.isMarried = isMarried;
        this.energy = 200;
        Refrigrator = refrigrator;
        this.abilitis = abilitis;
        this.relationShips = relationShips;
        this.trade = trade;
        this.ownedFarm = ownedFarm;
        this.backPack = backPack;
        this.isEnergyUnlimited = isEnergyUnlimited;
        this.hasCollapsed = hasCollapsed;
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


    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }


    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void increaseEnergy(int amount) {
        if (energy + amount > 200 && !isEnergyUnlimited) {
            energy = 200;
        } else {
            energy += amount;
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Refrigrator getRefrigrator() {
        return Refrigrator;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public void setRelationShips(ArrayList<RelationShip> relationShips) {
        this.relationShips = relationShips;
    }

    public void setRefrigrator(Refrigrator refrigrator) {
        Refrigrator = refrigrator;
    }

    public void setAbilitis(ArrayList<Ability> abilitis) {
        this.abilitis = abilitis;
    }

    public void setTrade(ArrayList<RelationShip.Trade> trade) {
        this.trade = trade;
    }

    public ArrayList<RelationShip.Trade> getTrade() {
        return trade;
    }

    public ArrayList<Ability> getAbilitis() {
        return abilitis;
    }

    public ArrayList<RelationShip> getRelationShips() {
        return relationShips;
    }

    public boolean isEnergyUnlimited() {
        return isEnergyUnlimited;
    }

    public boolean isHasCollapsed() {
        return hasCollapsed;
    }

    public void setBackPack(BackPack backPack) {
        this.backPack = backPack;
    }

    public void setEnergyUnlimited(boolean energyUnlimited) {
        isEnergyUnlimited = energyUnlimited;
    }


    public void setHasCollapsed(boolean hasCollapsed) {
        this.hasCollapsed = hasCollapsed;
    }

    public void setUnlimited() {
        this.isEnergyUnlimited = true;
    }

    public void collapse() {
        if (energy == 0) {
            this.hasCollapsed = true;
        }
    }
}
