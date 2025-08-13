package org.example.Common.models.RelatedToUser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.enums.Types.CraftingRecipe;

public class Ability {
    private final String name;
    private int level;
    private int amount;
    @JsonCreator
    public Ability(@JsonProperty("name")String name,@JsonProperty("amount") int amount) {
        this.name = name;
        this.level = 0;
        this.amount = amount;
    }


    public void setLevel(int level) {
        this.level = level;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getAmount() {
        return amount;
    }

    public void increaseAmount(int amount1) {
        this.amount += amount1;
        if(amount >= 10){
            if(level <= 3){
                level++;
                for(CraftingRecipe craftingRecipe : CraftingRecipe.values()){
                    if(craftingRecipe.getSource().get(name) <= level){
                        App.getCurrentPlayerLazy().getRecepies().put(craftingRecipe, true);
                    }
                }
                amount = 0;
            }
        }
    }

    public void increaseLevel() {
        int newLevel = level + 1;
        int neededAmount = 100 * newLevel + 50;
        if(amount > neededAmount && newLevel <= 4){
            amount -= neededAmount;
            level = newLevel;
            
            // Update scoreboard if in multiplayer mode
            if (org.example.Common.models.Fundementals.App.getCurrentGame() != null && 
                org.example.Common.models.Fundementals.App.getCurrentGame().isMultiplayer() &&
                org.example.Common.models.Fundementals.App.getCurrentGame().getScoreboardManager() != null) {
                org.example.Common.models.Fundementals.App.getCurrentGame().getScoreboardManager().updatePlayerScore(org.example.Common.models.Fundementals.App.getCurrentPlayerLazy());
            }
        }
    }
}
