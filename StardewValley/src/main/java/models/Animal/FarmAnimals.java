package models.Animal;

import models.Fundementals.Location;
import models.enums.Animal;

public class FarmAnimals implements Animals{
    //Animal enum with its information:
    private Animal animal;

    private int friendShip;

    // handle it in tile type
    private AnimalHome home;

    private String name;

    private Location position;

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


    public String getName() {return name;}

    public FarmAnimals(Animal animal, int friendShip, AnimalHome home, String name, Location position) {
        this.animal = animal;
        this.friendShip = friendShip;
        this.home = home;
        this.name = name;
        this.position = position;
    }
}
