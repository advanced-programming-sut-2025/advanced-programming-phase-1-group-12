package models.RelatedToUser;

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

    public void increaseAmount(int amount) {
        this.amount += amount;
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
