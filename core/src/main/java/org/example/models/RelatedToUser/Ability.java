package org.example.models.RelatedToUser;

import org.example.models.Fundementals.App;
import org.example.models.enums.Types.CraftingRecipe;

public class Ability {
    private final String name;
    private int level;
    private int amount;
    public Ability(String name, int amount) {
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
        }
    }
}
