package org.example.Common.models.Animal;

import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.enums.Animal;

public class FarmAnimals extends Item {
    //Animal enum with its information:
    private Animal animal;

    private int friendShip;

    // handle it in tile type
    private AnimalHome home;

    private Location position;

    private int daysLeftToProduce;
    //booleans
    private boolean hasBeenPettedToday;
    private boolean hasBeenFedToday;
    private boolean willProduceToday;
    //TODO:not sure we need this
    private boolean hasCollectedProductToday;
    private boolean isMoving;
    private Location target;
    private Location previousPosition;

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public int getFriendShip() {return friendShip;}

    //the max value for friendship is 1000
    public void setFriendShip(int friendShip) {this.friendShip = Math.min(friendShip, 1000);}

    public AnimalHome getHome() {return home;}

    public void setHome(AnimalHome home) {this.home = home;}

    public Location getPosition() {return position;}

    public void setPosition(Location position) {this.position = position;}

    public boolean isHasBeenPettedToday() {
        return hasBeenPettedToday;
    }

    public void setHasBeenPettedToday(boolean hasBeenPettedToday) {
        this.hasBeenPettedToday = hasBeenPettedToday;
    }

    public boolean isHasBeenFedToday() {
        return hasBeenFedToday;
    }

    public void setHasBeenFedToday(boolean hasBeenFedToday) {
        this.hasBeenFedToday = hasBeenFedToday;
    }

    public FarmAnimals(Animal animal, int friendShip, AnimalHome home, String name, Location position) {
        super(name, Quality.NORMAL, animal.getPurchaseCost());
        this.animal = animal;
        this.friendShip = friendShip;
        this.home = home;
        this.position = position;
        this.hasBeenPettedToday = false;
        this.hasBeenFedToday = false;
        this.willProduceToday = true;
        //produce first day
        this.daysLeftToProduce = 0;
        this.hasCollectedProductToday = false;
    }

    public boolean isWillProduceToday() {
        return willProduceToday;
    }

    public void setWillProduceToday(boolean willProduceToday) {
        this.willProduceToday = willProduceToday;
    }

    public int getDaysLeftToProduce() {
        return daysLeftToProduce;
    }

    public void setDaysLeftToProduce(int daysLeftToProduce) {
        this.daysLeftToProduce = daysLeftToProduce;
    }

    public boolean isHasCollectedProductToday() {
        return hasCollectedProductToday;
    }

    public void setHasCollectedProductToday(boolean hasCollectedProductToday) {
        this.hasCollectedProductToday = hasCollectedProductToday;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public Location getTarget() {
        return target;
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public Texture getTexture() {
        return animal.getAnimalTexture();
    }

    public Location getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Location previousPosition) {
        this.previousPosition = previousPosition;
    }
}
