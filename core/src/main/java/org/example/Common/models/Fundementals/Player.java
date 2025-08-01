package org.example.Common.models.Fundementals;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.example.Common.models.*;
import org.example.Server.controllers.movingPlayer.PlayerController;
import org.example.Common.models.NPC.NPC;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.ProductsPackage.ArtisanItem;
import org.example.Common.models.RelatedToUser.Ability;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.RelationShips.RelationShip;
import org.example.Common.models.RelationShips.Trade;
import org.example.Common.models.ToolsPackage.Tools;
import org.example.Common.models.enums.Types.Cooking;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.network.events.EnergyUpdateEvent;
import org.example.Server.network.GameSessionManager;
import org.example.Client.network.NetworkCommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private Texture playerTexture;
    private Sprite playerSprite;
    private float Speed = 1f;
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
    private Map<Cooking, Boolean> cookingRecepies ;
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
    private Texture portraitFrame;
    private ArrayList<Craft>crafts = new ArrayList<>();

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
        this.cookingRecepies = new HashMap<>();
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

        rect = new CollisionRect(playerSprite.getX(), playerSprite.getY(), playerSprite.getWidth(), playerSprite.getHeight());
    }

    public void updatePosition(int posX, int posY) {
        Location newLocation = App.getCurrentGame().getMainMap().findLocation(posX, posY);
        setUserLocation(newLocation);
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
        System.out.println("DEBUG: setEnergy called for player " + this.getUser().getUserName() + " with energy: " + energy);
        setEnergyInternal(energy);
        
        // Broadcast energy update to all players in multiplayer mode
        if (App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer()) {
            System.out.println("DEBUG: Game is multiplayer, attempting to broadcast energy update");
            try {
                // Send energy update via WebSocket client
                // This will be handled by the NetworkCommandSender
                if (App.getCurrentGame().getNetworkCommandSender() != null) {
                    System.out.println("DEBUG: NetworkCommandSender exists, sending energy update WebSocket");
                    App.getCurrentGame().getNetworkCommandSender().sendEnergyUpdateWebSocket(
                        this.getUser().getUserName(), 
                        this.energy, 
                        200 // max energy
                    );
                    System.out.println("DEBUG: Energy update WebSocket sent successfully");
                } else {
                    System.out.println("DEBUG: NetworkCommandSender is null! Trying to get from PlayerController");
                    // Fallback: try to get NetworkCommandSender from PlayerController
                    if (this.playerController != null && this.playerController instanceof org.example.Client.controllers.movingPlayer.ClientPlayerController) {
                        org.example.Client.controllers.movingPlayer.ClientPlayerController clientController = 
                            (org.example.Client.controllers.movingPlayer.ClientPlayerController) this.playerController;
                        NetworkCommandSender sender = clientController.getNetworkCommandSender();
                        if (sender != null) {
                            System.out.println("DEBUG: Got NetworkCommandSender from PlayerController, sending energy update");
                            sender.sendEnergyUpdateWebSocket(
                                this.getUser().getUserName(), 
                                this.energy, 
                                200 // max energy
                            );
                            System.out.println("DEBUG: Energy update WebSocket sent successfully via PlayerController");
                        } else {
                            System.out.println("DEBUG: NetworkCommandSender is null even from PlayerController");
                        }
                    } else {
                        System.out.println("DEBUG: PlayerController is null or not ClientPlayerController");
                    }
                }
            } catch (Exception e) {
                // Log error but don't break the game
                System.err.println("Failed to broadcast energy update: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("DEBUG: Game is not multiplayer or App.getCurrentGame() is null");
        }
    }

    /**
     * Internal method to set energy without broadcasting (used for receiving updates)
     */
    public void setEnergyInternal(int energy) {
        this.energy = energy;
    }

    public void increaseEnergy(int amount) {
        int newEnergy;
        if (App.getCurrentGame().getDate().getDaysPassed(getRejectDate()) <= 7) {
            if (energy + amount > 200 && !isEnergyUnlimited) {
                newEnergy = 200;
            } else {
                newEnergy = energy + (amount / 2);
            }
        } else {
            if (energy + amount > 200 && !isEnergyUnlimited) {
                newEnergy = 200;
            } else {
                newEnergy = energy + amount;
            }
        }
        setEnergy(newEnergy);
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
        recepies = new HashMap<>();
        for (CraftingRecipe craftingRecipe : CraftingRecipe.values()) {
            if(craftingRecipe.getSource().isEmpty()){
                recepies.put(craftingRecipe, true);
            } else{
                recepies.put(craftingRecipe, false);
            }
        }
        cookingRecepies = new HashMap<>();
        for (Cooking cooking : Cooking.values()) {
            if(cooking.getSource().isEmpty()){
                cookingRecepies.put(cooking, true);
            } else{
                cookingRecepies.put(cooking, false);
            }
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
        int newEnergy;
        if (energy - amount < 0) {
            newEnergy = 0;
        } else {
            newEnergy = energy - amount;
        }
        setEnergy(newEnergy);
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
        System.out.println("DEBUG: getPlayerController called for player " + this.getUser().getUserName() + " returning: " + (playerController != null ? playerController.getClass().getSimpleName() : "null"));
        return playerController;
    }

    public void setPlayerController(PlayerController playerController) {
        System.out.println("DEBUG: setPlayerController called for player " + this.getUser().getUserName() + " with controller type: " + (playerController != null ? playerController.getClass().getSimpleName() : "null"));
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

    public void setPlayerTexture(Texture playerTexture) {
        this.playerTexture = playerTexture;
    }

    public Texture getPlayerTexture() {
        return playerTexture;
    }

    public Texture getPortraitFrame() {
        return this.portraitFrame;
    }

    public void setPortraitFrame(Texture portraitFrame) {
        this.portraitFrame = portraitFrame;
    }

    public ArrayList<Craft> getCrafts() {
        return crafts;
    }

    public void setCrafts(ArrayList<Craft> crafts) {
        this.crafts = crafts;
    }

}
