package controller;

import models.Animal.FarmAnimals;
import models.Fundementals.*;
import models.enums.Animal;

import java.util.regex.Matcher;

public class AnimalController {

    public boolean isLocationInPlace(Location location, LocationOfRectangle place){
        return location.getxAxis() >= place.getTopLeftCorner().getxAxis() &&
                location.getxAxis() <= place.getTopLeftCorner().getxAxis() + place.getWidth() &&
                location.getyAxis() >= place.getTopLeftCorner().getyAxis() &&
                location.getyAxis() <= place.getTopLeftCorner().getyAxis() + place.getLength();
    }

    public FarmAnimals findAnimalByName(String animalName){
        for(FarmAnimals animals : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()){
            if(animals.getName().equals(animalName)){
                return animals;
            }
        }
        return null;
    }

    public Result pet(String animalName){
        FarmAnimals animal = findAnimalByName(animalName);
        if(animal == null){
            return new Result(false, "You do not own an animal with such name!");
        }//if the animal is not next to us
        else if (!App.isNextToUs(animal.getPosition())) {
            return new Result(false, "You are not standing next to " + animalName);
        }
        animal.setFriendShip(animal.getFriendShip() + 15);
        return new Result(true, "You just petted " + animalName);
    }

    public Result cheatFriendship(Matcher matcher){
        String animalName = matcher.group("animalName");
        int amount = Integer.parseInt(matcher.group("amount"));
        FarmAnimals animal = findAnimalByName(animalName);

        if(animal == null){
            return new Result(false, "You do not own an animal with such name!");
        }
        animal.setFriendShip(amount);
        return new Result(true, animalName + "friendship set to " + amount);
    }

    public void animalsList(){
        //TODO: اینکه آن روز نوازش و تغذیه شده اند یا خیر رو نزدم

        for (FarmAnimals animal : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()) {
            if(animal == null){
                System.out.println("You do not own any animals!");
                return;
            }
            System.out.println( animal.getAnimal().name().toLowerCase() + animal.getName() + animal.getFriendShip());
        }
    }

    //    public boolean havePlaceTooKeep(Animal animal){
//
//    }
    public Result milking(String animalName){
        FarmAnimals animal = findAnimalByName(animalName);
        if(animal == null){
            return new Result(false, "You do not own an animal with such name!");
        }
        if(! (animal.getAnimal().equals(Animal.COW) || animal.getAnimal().equals(Animal.GOAT))){
            return new Result(false, "This animal is not a cow or goat!");
        }
        animal.setFriendShip(animal.getFriendShip() + 5);
        return new Result(true, "You just milked " + animalName);
    }

    public Result shear(String animalName){
        FarmAnimals animal = findAnimalByName(animalName);
        if(animal == null){
            return new Result(false, "You do not own an animal with such name!");
        }
        if( ! animal.getAnimal().equals(Animal.SHEEP) ){
            return new Result(false, "This animal is not a sheep!");
        }
        animal.setFriendShip(animal.getFriendShip() + 5);
        return new Result(true, "You just sheared " + animalName);
    }

}