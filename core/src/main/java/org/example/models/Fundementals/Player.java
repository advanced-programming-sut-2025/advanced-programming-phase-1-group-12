package org.example.models.Fundementals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.example.controllers.movingPlayer.PlayerController;
import org.example.models.*;
import org.example.models.NPC.NPC;
import org.example.models.Place.Farm;
import org.example.models.ProductsPackage.ArtisanItem;
import org.example.models.Refrigrator;
import org.example.models.RelatedToUser.Ability;
import org.example.models.RelatedToUser.User;
import org.example.models.RelationShips.RelationShip;
import org.example.models.RelationShips.Trade;
import org.example.models.ToolsPackage.Tools;
import org.example.models.enums.Types.Cooking;
import org.example.models.enums.Types.CraftingRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private Texture playerTexture;
    private Sprite playerSprite;
    private float Speed = 100f;
    private CollisionRect rect;
    private User user;
    private Location userLocation;
    private boolean isMarried;
    private int energy;
    public Refrigrator Refrigrator;
    private ArrayList<Ability> abilitis = new ArrayList<Ability>();
    private ArrayList<RelationShip> relationShips;
    private Farm ownedFarm;
    private BackPack backPack;
    private boolean isEnergyUnlimited;
    private boolean hasCollapsed;
    private int money;
    private Player partner;
    private Map<CraftingRecipe, Boolean> recepies;
    private Map<Cooking, Boolean> cookingRecepies = new HashMap<>();
    private ArrayList<Trade> trades = new ArrayList<>();
    private Tools currentTool;
    private Map<NPC, Date> metDates;
    private ShippingBin shippingBin;
    private ArrayList<ArtisanItem> artisansGettingProcessed = new ArrayList<>();
    private Date rejectDate;
    private int shippingMoney;
    private boolean isMaxEnergyBuffEaten = false;
    private boolean isSkillBuffEaten = false;
    private PlayerController playerController;

    public Player(User user, Location userLocation, boolean isMarried, Refrigrator refrigrator,
                  ArrayList<RelationShip> relationShips, Farm ownedFarm, BackPack backPack, boolean isEnergyUnlimited,
                  boolean hasCollapsed, ArrayList<Ability> abilitis) {
        this.playerTexture = new Texture("Emoji/Emojis000.png");
        this.playerSprite = new Sprite(playerTexture);
        this.user = user;
        this.userLocation = userLocation;
        this.isMarried = isMarried;
        this.energy = 200;
        this.Refrigrator = refrigrator;
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
        this.shippingBin = null;
        this.rejectDate = null;
        this.shippingMoney = 0;
        this.relationShips = new ArrayList<>();
        initializeAbilities();
        initializerecepies();

//        centerPlayerOnScreen();
        rect = new CollisionRect(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight());
    }

    private void centerPlayerOnScreen() {
        float centerX = (Gdx.graphics.getWidth() - playerSprite.getWidth()) / 2f;
        float centerY = (Gdx.graphics.getHeight() - playerSprite.getHeight()) / 2f;

        userLocation.setxAxis((int)centerX);
        userLocation.setyAxis((int)centerY);

        playerSprite.setPosition(centerX, centerY);
    }

    public void updatePosition(int posX, int posY) {
        userLocation.setxAxis(posX);
        userLocation.setyAxis(posY);
        rect.move(posX, posY);
        playerSprite.setPosition(posX, posY);
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
        if (App.getCurrentGame().getDate().getDaysPassed(getRejectDate()) <= 7) {
            if (energy + amount > 200 && !isEnergyUnlimited) {
                energy = 200;
            } else {
                energy += amount / 2;
            }
        } else {
            if (energy + amount > 200 && !isEnergyUnlimited) {
                energy = 200;
            } else {
                energy += amount;
            }
        }
    }

    public int getEnergy() {
        return energy;
    }

    public void setUnlimited() {
        this.isEnergyUnlimited = true;
    }


    public RelationShip findRelationShip(Player player2) {
        for (RelationShip relationShip : relationShips) {
            if (relationShip.getPlayer1().equals(player2) || relationShip.getPlayer2().equals(player2)) {
                return relationShip;
            }
        }
        return null;
    }

    public void setMarried() {
        isMarried = true;
    }

    public void decreaseMoney(int amount) {
        if (isMarried) {
            money -= amount / 2;
            partner.setMoney(partner.getMoney() - amount / 2);
        } else {
            money -= amount;
        }
    }

    public int getMoney() {
        return money;
    }

    public void increaseMoney(int amount) {
        if (isMarried) {
            money += amount / 2;
            partner.setMoney(partner.getMoney() + amount / 2);
        } else {
            money += amount;
        }
    }

    public void setPartner(Player partner) {
        this.partner = partner;
    }

    public void setMoney(int money) {
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

    private void initializerecepies() {
        recepies = new HashMap<>();  // Initialize the class field, not a local variable
        for (CraftingRecipe craftingRecipe : CraftingRecipe.values()) {
            recepies.put(craftingRecipe, false);
        }
        cookingRecepies = new HashMap<>();  // Initialize the class field
        for (Cooking cooking : Cooking.values()) {
            cookingRecepies.put(cooking, false);
        }
    }

    public Ability getAbilityByName(String name) {
        for (Ability ability : abilitis) {
            if (ability.getName().equalsIgnoreCase(name)) {
                return ability;
            }
        }
        return null;
    }

    public void reduceEnergy(int amount) {
        if (isEnergyUnlimited) {
            return;
        }
        if (energy - amount < 0) {
            energy = 0;
        } else {
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

    public void addMetDates(NPC npc) {
        this.metDates.put(npc, App.getCurrentGame().getDate());
    }

    public Date getMetDate(NPC npc) {
        return metDates.get(npc);
    }

    public ShippingBin getShippingBin() {
        return shippingBin;
    }

    public Map<CraftingRecipe, Boolean> getRecepies() {
        return recepies;
    }

    public void setRecepies(Map<CraftingRecipe, Boolean> recepies) {
        this.recepies = recepies;
    }

    public ArrayList<ArtisanItem> getArtisansGettingProcessed() {
        return artisansGettingProcessed;
    }

    public void setArtisansGettingProcessed(ArrayList<ArtisanItem> artisansGettingProcessed) {
        this.artisansGettingProcessed = artisansGettingProcessed;
    }

    public Date getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(Date rejectDate) {
        this.rejectDate = rejectDate;
    }

    public Map<Cooking, Boolean> getCookingRecepies() {
        return cookingRecepies;
    }

    public void setCookingRecepies(Map<Cooking, Boolean> cookingRecepies) {
        this.cookingRecepies = cookingRecepies;
    }

    public void setAbilitis(ArrayList<Ability> abilitis) {
        this.abilitis = abilitis;
    }

    public void setRefrigrator(Refrigrator refrigrator) {
        Refrigrator = refrigrator;
    }

    public void setBackPack(BackPack backPack) {
        this.backPack = backPack;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public float getSpeed(){
        return this.Speed;
    }

    public Tools getCurrentTool() {
        return currentTool;
    }

    public void addRelationShip(RelationShip relationShip) {
        this.relationShips.add(relationShip);
    }

    public ArrayList<RelationShip> getRelationShips() {
        return relationShips;
    }

    public void setCurrentTool(Tools currentTool) {
        this.currentTool = currentTool;
    }

    public void setShippingBin(ShippingBin shippingBin) {
        this.shippingBin = shippingBin;
    }

    public void increaseShippingMoney(int amount) {
        this.shippingMoney += amount;
    }

    public int getShippingMoney() {
        return shippingMoney;
    }

    public void setShippingMoney(int shippingMoney) {
        this.shippingMoney = shippingMoney;
    }

    public boolean isMaxEnergyBuffEaten() {
        return isMaxEnergyBuffEaten;
    }

    public void setMaxEnergyBuffEaten(boolean maxEnergyBuffEaten) {
        isMaxEnergyBuffEaten = maxEnergyBuffEaten;
    }

    public boolean isSkillBuffEaten() {
        return isSkillBuffEaten;
    }

    public void setSkillBuffEaten(boolean skillBuffEaten) {
        isSkillBuffEaten = skillBuffEaten;
    }

    public Sprite getPlayerSprite() {
        return playerSprite;
    }

}

