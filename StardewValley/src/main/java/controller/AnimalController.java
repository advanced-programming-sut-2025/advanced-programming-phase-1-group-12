package controller;

import models.Animal.FarmAnimals;
import models.Animal.Fish;
import models.Fundementals.*;
import models.ProductsPackage.Quality;
import models.RelatedToUser.Ability;
import models.enums.Animal;
import models.enums.FishDetails;
import models.enums.Types.TypeOfTile;
import models.enums.Weather;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class AnimalController {

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

    public Result shepherd(Matcher matcher){
        String animalName = matcher.group("animalName");
        FarmAnimals animal = findAnimalByName(animalName);
        if(animal == null){
            return new Result(false, "You do not own an animal with such name!");
        }
        Location destination = App.getCurrentGame().getMainMap().findLocation(Integer.parseInt(matcher.group("x")), Integer.parseInt(matcher.group("y")));

        if(destination == null || App.isLocationInPlace(destination, App.getCurrentPlayerLazy().getOwnedFarm().getLocation())){
            return new Result(false, "Given location is not in your farm or not valid");
        }
        //returning home
        if( App.isLocationInPlace(destination, animal.getHome().getLocation())){
            animal.setPosition(destination);
            return new Result(true, "You just shepherd " + animalName);
        }// is going out
        if(!destination.getTypeOfTile().equals(TypeOfTile.GROUND)){
            return new Result(false, "Type of given destination makes it unavailable");
        }
        if((App.getCurrentGame().getDate().getWeather().equals(Weather.RAINY) ||
                App.getCurrentGame().getDate().getWeather().equals(Weather.STORM) ||
        App.getCurrentGame().getDate().getWeather().equals(Weather.SNOW))){
            return new Result(false, "Weather is bad for getting "+ animalName + " out");
        }
        animal.setPosition(destination);
        destination.setObjectInTile(animal);
        return new Result(true, "You just shepherd " + animalName);
    }

    public Result fishing(String fishingPole){
        //TODO:aslan toor mahigiri va tabar chiz koja va chetor handle mishan?hich toori nazadam alan

        double M = switch (App.getCurrentGame().getDate().getWeather()) {
            case RAINY -> 1.2;
            case STORM -> 0.5;
            case SUNNY -> 1.5;
            default -> 1.0;
        };
        Random random = new Random();
        double randomNum = random.nextDouble();

        double pole = switch (fishingPole) {
            case "Training Rod" -> 0.1;
            case "Bamboo Pole" -> 0.5;
            case "Fiberglass Rod" -> 0.9;
            //Iriⅾiuⅿ Roⅾ:
            default -> 1.2;
        };
        Ability fishing = null;
        for(Ability ability : App.getCurrentPlayerLazy().getAbilitis()){
            if(ability.getName().equals(fishingPole)){
                fishing = ability;
                break;
            }
        }
        int numberOfCaught = Math.min(6, (int)(randomNum * M * (fishing.getLevel()+2)));

        double quality = (int)((fishing.getLevel()+2) * randomNum * pole / (7 - M));
        Quality fishQuality;

        if(quality > 0 && quality < 0.5){
            fishQuality = Quality.NORMAL;
        }
        else if(quality > 0.5 && quality < 0.7){
            fishQuality = Quality.SILVER;
        } else if (quality > 0.7 && quality < 0.9) {
            fishQuality = Quality.GOLDEN;
        } else {
            fishQuality = Quality.IRIDIUM;
        }
        List<FishDetails>fishTypes = List.of();
        //possible fish types
        for(FishDetails types : FishDetails.values()){
            if(types.getSeason().equals(App.getCurrentGame().getDate().getSeason())){
                if(!types.isLegendary() || fishing.getLevel() == 4){
                    fishTypes.add(types);
                }
            }
        }
        List<FishDetails> randomItems = new ArrayList<>();

        for (int i = 0; i < numberOfCaught; i++) {
            int randomIndex = random.nextInt(fishTypes.size()); // Random index [0, size-1]
            randomItems.add(fishTypes.get(randomIndex)); // May pick the same item multiple times
        }
        for(FishDetails fishDetails : randomItems){
            App.getCurrentPlayerLazy().getBackPack().getFishes().add(new Fish(fishDetails, fishQuality));
        }
        fishing.setAmount(fishing.getAmount() + 5);
        return new Result(true, "You just caught " + numberOfCaught + " fishes");
    }

    public Result sellAnimal(String animalName){
        FarmAnimals animal = findAnimalByName(animalName);
        if(animal == null){
            return new Result(false, "Animal not found");
        }
        App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals().remove(animal);
        int gainedMoney = (int) (animal.getAnimal().getPurchaseCost() * (((double) animal.getFriendShip() /1000) + 0.3));
        App.getCurrentPlayerLazy().setMoney(gainedMoney + App.getCurrentPlayerLazy().getMoney());

        return new Result(true, "You just sold " + animalName);
    }

}
