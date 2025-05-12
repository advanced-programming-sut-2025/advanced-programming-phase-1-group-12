package models.Fundementals;

import models.BackPack;
import models.Place.Farm;
import models.Refrigrator;
import models.RelatedToUser.Ability;
import models.RelatedToUser.User;
import models.RelationShips.RelationShip;
import models.RelationShips.Trade;
import models.ToolsPackage.Tools;

import java.util.ArrayList;

public class Player {
    private User user;
    private Location userLocation;
    private boolean isMarried;
    private int energy;
    public Refrigrator Refrigrator;
    private ArrayList<Ability> abilitis = new ArrayList<Ability>();
    private ArrayList<RelationShip> relationShips = new ArrayList<>();
    private Farm ownedFarm;
    private BackPack backPack;
    private boolean isEnergyUnlimited;
    private boolean hasCollapsed;
    private int money;
    private Player partner;
    private Map<CraftingRecipe, Boolean> recepies;
    private ArrayList<Ability> abilities;
    private ArrayList<Trade> trades = new ArrayList<>();
    private Tools currentTool;
    private Map<NPC, Date> metDates;

    public Player(User user, Location userLocation, boolean isMarried, Refrigrator refrigrator,
                  ArrayList<RelationShip> relationShips, Farm ownedFarm, BackPack backPack, boolean isEnergyUnlimited,
                  boolean hasCollapsed, ArrayList<Ability> abilitis) {
        this.user = user;
        this.userLocation = userLocation;
        this.isMarried = isMarried;
        this.energy = 200;
        this.Refrigrator = refrigrator;
        this.abilitis = abilitis;
        this.relationShips = relationShips;
        this.ownedFarm = ownedFarm;
        this.backPack = backPack;
        this.money = 1_000_000;
        this.partner = null;
        this.recepies = new HashMap<>();
        this.trades = new ArrayList<>();
        this.currentTool = null;
        this.isEnergyUnlimited = false;
        this.hasCollapsed = false;
        this.metDates = new HashMap<>();
        initializeAbilities();
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


    public ArrayList<Ability> getAbilitis() {
        return abilitis;
    }

    private void initializeAbilities() {
        abilitis.add(new Ability("Fishing", 0));
        abilitis.add(new Ability("Mining", 0));
        abilitis.add(new Ability("Farming", 0));
        abilitis.add(new Ability("Foraging", 0));
    }
    public Ability getAbilityByName(String name){
        for(Ability ability : abilitis){
            if(ability.getName().equals(name)){
                return ability;
            }
        }
        return null;
    }

    public void reduceEnergy(int amount){
        if(energy - amount < 0){
            energy = 0;
        }
        else{
            energy -= amount;
        }
    }

    public boolean isEnergyUnlimited() {
        return isEnergyUnlimited;
    }

    public void setEnergyUnlimited(boolean energyUnlimited) {
        isEnergyUnlimited = energyUnlimited;
    }

    public boolean isHasCollapsed() {
        return hasCollapsed;
    }

    public void setHasCollapsed(boolean hasCollapsed) {
        this.hasCollapsed = hasCollapsed;
    }

    public Refrigrator getRefrigrator() {
        return Refrigrator;
    }

    public void addMetDates(NPC npc){
        this.metDates.put(npc, App.getCurrentGame().getDate());
    }

    public Date getMetDate(NPC npc){
        return metDates.get(npc);
    }


}
