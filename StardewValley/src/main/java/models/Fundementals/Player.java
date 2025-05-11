package models.Fundementals;

import models.BackPack;
import models.Place.Farm;
import models.Refrigrator;
import models.RelatedToUser.Ability;
import models.RelatedToUser.User;
import models.RelationShips.RelationShip;
import models.RelationShips.Trade;
import models.ToolsPackage.Tools;
import models.enums.Types.Cooking;

import java.util.ArrayList;

public class Player {
    private User user;
    private Location userLocation;
    private boolean isMarried;
    private int energy;
    private ArrayList<String> recepies;
    private ArrayList<Ability> abilities;
    private ArrayList<RelationShip> relationShips = new ArrayList<>();
    private ArrayList<Trade> trades = new ArrayList<>();
    private Farm ownedFarm;
    private BackPack backPack;
    private boolean isEnergyUnlimited;
    private boolean hasCollapsed;
    private int money;
    private Player partner;
    private Tools currentTool;


    public Player(User user, Location userLocation, boolean isMarried,
                   ArrayList<RelationShip> relationShips,
                  Farm ownedFarm, BackPack backPack, boolean isEnergyUnlimited, boolean hasCollapsed ) {
        this.user = user;
        this.userLocation = userLocation;
        this.isMarried = isMarried;
        this.energy = 200;
        this.abilities = new ArrayList<>();
        initializeAbilities();
        this.relationShips = relationShips;
        this.ownedFarm = ownedFarm;
        this.backPack = backPack;
        this.isEnergyUnlimited = isEnergyUnlimited;
        this.hasCollapsed = hasCollapsed;
        this.trades = new ArrayList<>();
        this.money = 0;
        this.partner = null;
        this.recepies = new ArrayList<>();
        this.currentTool = null;
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

    public void reduceEnergy(int amount){
        if(energy - amount < 0){
            energy = 0;
        }
        else{
            energy -= amount;
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
        return abilities;
    }

    public void addRecipes(Cooking cookingTypes){
        recepies.add(cookingTypes.getIngredient().toString());
    }

    public String showRecipes(){
        StringBuilder result = new StringBuilder("Recipes: \n");
        for(String recipes : recepies){
            result.append(recipes).append("\n");
        }
        return result.toString();
    }


    private void initializeAbilities() {
        abilities.add(new Ability("Fishing", 0));
        abilities.add(new Ability("Mining", 0));
        abilities.add(new Ability("Farming", 0));
        abilities.add(new Ability("Foraging", 0));
    }

    public Ability getAbilityByName(String name){
        for(Ability ability : abilities){
            if(ability.getName().equals(name)){
                return ability;
            }
        }
        return null;
    }

    public ArrayList<String> getRecepies() {
        return recepies;
    }

    public ArrayList<Ability> getAbilities() {
        return abilities;
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

    public Player getPartner() {
        return partner;
    }

    public Tools getCurrentTool() {
        return currentTool;
    }

    public void setCurrentTool(Tools currentTool) {
        this.currentTool = currentTool;
    }
}
