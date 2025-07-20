package org.example.Server.controllers;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Result;
import org.example.Client.Main;
import org.example.Common.models.Animal.FarmAnimals;
import org.example.Common.models.Item;
import org.example.Common.models.ItemBuilder;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.RelatedToUser.Ability;
import org.example.Common.models.enums.Animal;
import org.example.Common.models.enums.FishDetails;
import org.example.Common.models.enums.Season;
import org.example.Common.models.enums.Types.AnimalProduct;
import org.example.Common.models.enums.Types.TypeOfTile;
import org.example.Common.models.enums.Weather;
import org.example.Client.views.FishingScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;

public class AnimalController {

    public FarmAnimals findAnimalByName(String animalName) {
        for (FarmAnimals animals : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()) {
            if (animals.getName().equals(animalName)) {
                return animals;
            }
        }
        return null;
    }

    public Result pet(String animalName) {
        FarmAnimals animal = findAnimalByName(animalName);
        if (animal == null) {
            return new Result(false, "You do not own an animal with such name!");
        }//if the animal is not next to us
        else if (!App.isNextToUs(animal.getPosition())) {
            return new Result(false, "You are not standing next to " + animalName);
        }
        animal.setFriendShip(animal.getFriendShip() + 15);
        animal.setHasBeenPettedToday(true);
        return new Result(true, "You just petted " + animalName);
    }

    public Result cheatFriendship(Matcher matcher) {
        String animalName = matcher.group("animalName");
        int amount = Integer.parseInt(matcher.group("amount"));
        FarmAnimals animal = findAnimalByName(animalName);

        if (animal == null) {
            return new Result(false, "You do not own an animal with such name!");
        }
        animal.setFriendShip(amount);
        return new Result(true, animalName + "friendship set to " + amount);
    }

    public void animalsList() {

        for (FarmAnimals animal : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()) {
            if (animal == null) {
                System.out.println("You do not own any animals!");
                return;
            }
            System.out.print(animal.getAnimal().name().toLowerCase() + " " + animal.getName() + " " + animal.getFriendShip());
            if (animal.isHasBeenPettedToday()) {
                System.out.println(" it has been petted");
            } else {
                System.out.println(" it has not been petted");
            }
        }
    }

    public Result milking(String animalName) {
        FarmAnimals animal = findAnimalByName(animalName);
        if (animal == null) {
            return new Result(false, "You do not own an animal with such name!");
        }
        if (!(animal.getAnimal().equals(Animal.COW) || animal.getAnimal().equals(Animal.GOAT))) {
            return new Result(false, "This animal is not a cow or goat!");
        }
        boolean hasMilkPail = App.getCurrentPlayerLazy().getBackPack().getItemNames().containsKey("Milk Pail");

        if (!hasMilkPail) {
            return new Result(false, "You do not have a milk pail!");
        }
        Quality quality = findQulaity(animal.getFriendShip());

        Random rand = new Random();
        double randDouble = rand.nextDouble();
        randDouble += 0.5;
        double willProduceGood = (animal.getFriendShip() + randDouble * 150) / 1500;
        double number = rand.nextDouble();

        if (animal.getDaysLeftToProduce() == 0 && !animal.isHasCollectedProductToday() && animal.isWillProduceToday()) {
            if (animal.getFriendShip() < 100 || willProduceGood < number) {
                if (animal.getAnimal().equals(Animal.COW)) {
                    ItemBuilder.addToBackPack(ItemBuilder.builder(AnimalProduct.MILK.getName(), quality, AnimalProduct.MILK.getPrice()), 1, quality);
                }
                if (animal.getAnimal().equals(Animal.GOAT)) {
                    ItemBuilder.addToBackPack(ItemBuilder.builder(AnimalProduct.GOAT_MILK.getName(), quality, AnimalProduct.GOAT_MILK.getPrice()), 1, quality);
                }
            }
            else {
                if (animal.getAnimal().equals(Animal.COW)) {
                    ItemBuilder.addToBackPack(ItemBuilder.builder(AnimalProduct.LARGE_MILK.getName(), quality, AnimalProduct.LARGE_MILK.getPrice()), 1, quality);
                }
                if (animal.getAnimal().equals(Animal.GOAT)) {
                    ItemBuilder.addToBackPack(ItemBuilder.builder(AnimalProduct.LARGE_GOAT_MILK.getName(), quality, AnimalProduct.LARGE_GOAT_MILK.getPrice()), 1, quality);
                }
            }
            animal.setWillProduceToday(false);
            App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - 1);
            animal.setHasCollectedProductToday(true);
            return new Result(true, "You just milked " + animalName);
        } else {
            return new Result(false, "You can not milk " + animalName + " now");
        }
    }

    public Quality findQulaity(int friendShip) {
        Random rand = new Random();
        double randomNum = rand.nextDouble();
        double number = (double) friendShip / 1000 * (0.5 + 0.5 * randomNum);

        if (number > 0 && number < 0.5) {
            return Quality.NORMAL;
        } else if (number > 0.5 && number < 0.7) {
            return Quality.SILVER;
        } else if (number > 0.7 && number < 0.9) {
            return Quality.GOLDEN;
        } else {
            return Quality.IRIDIUM;
        }

    }

    //TODO:add pashm and milk to back pack
    public Result shear(String animalName) {
        FarmAnimals animal = findAnimalByName(animalName);
        if (animal == null) {
            return new Result(false, "You do not own an animal with such name!");
        }
        if (!animal.getAnimal().equals(Animal.SHEEP)) {
            return new Result(false, "This animal is not a sheep!");
        }
        boolean hasShear = App.getCurrentPlayerLazy().getBackPack().getItemNames().containsKey("Shear");

        if (!hasShear) {
            return new Result(false, "You do not have a shear!");
        }
        Quality quality = findQulaity(animal.getFriendShip());
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - 1);
        ItemBuilder.addToBackPack(ItemBuilder.builder(AnimalProduct.WOOL_SHEEP.getName(), quality, AnimalProduct.WOOL_SHEEP.getPrice()), 1, quality);
        return new Result(true, "You just sheared " + animalName);
    }

    public Result shepherd(FarmAnimals animal, int x, int y) {
        if (animal == null) {
            return new Result(false, "You do not own an animal with such name!");
        }
        Location destination = App.getCurrentGame().getMainMap().findLocation(x, y);

        if (destination == null || !App.getCurrentPlayerLazy().getOwnedFarm().getLocation().getLocationsInRectangle().contains(destination)) {
            return new Result(false, "Given location is not in your farm or not valid");
        }
        //returning home
        if (App.isLocationInPlace(destination, animal.getHome().getLocation())) {
            animal.setPosition(destination);
            return new Result(true, "You just shepherd " + animal.getName());
        }// is going out
        if (!destination.getTypeOfTile().equals(TypeOfTile.GROUND)) {
            return new Result(false, "Type of given destination makes it unavailable");
        }
        if ((App.getCurrentGame().getDate().getWeather().equals(Weather.RAINY) ||
                App.getCurrentGame().getDate().getWeather().equals(Weather.STORMY) ||
                App.getCurrentGame().getDate().getWeather().equals(Weather.SNOWY))) {
            return new Result(false, "Weather is bad for getting " + animal.getName() + " out");
        } if(!isDestinationNear(destination, animal.getPosition())){
            return new Result(false, "Destination is not near destination(it should be less than 5");
        }
        animal.setMoving(true);
        animal.setPreviousPosition(animal.getPosition());
        animal.setTarget(destination);
        return new Result(true, "You just shepherd " + animal.getName());
    }

    public boolean isDestinationNear(Location destination, Location origin) {
        return (destination.getyAxis() - origin.getyAxis())* (destination.getyAxis() - origin.getyAxis()) +
            (destination.getxAxis() - origin.getxAxis()) * (destination.getxAxis() - origin.getxAxis()) <= 25;
    }

    public Result fishing(String fishingPole, List<String>players) {
        double M = switch (App.getCurrentGame().getDate().getWeather()) {
            case RAINY -> 1.2;
            case STORMY -> 0.5;
            case SUNNY -> 1.5;
            default -> 1.0;
        };
        Random random = new Random();
        double randomNum = random.nextDouble();

        double pole = switch (fishingPole) {
            case "Training Rod" -> 0.1;
            case "Bamboo Pole" -> 0.5;
            case "Fiberglass Rod" -> 0.9;
            default -> 1.2;
        };
        Ability fishing = null;
        for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
            if (ability.getName().equalsIgnoreCase("fishing")) {
                fishing = ability;
                break;
            }
        }
        int numberOfCaught = Math.min(6, (int) (randomNum * M * (fishing.getLevel() + 2)));

        double quality = (int) ((fishing.getLevel() + 2) * randomNum * pole / (7 - M));
        Quality fishQuality;

        if (quality > 0 && quality < 0.5) {
            fishQuality = Quality.NORMAL;
        } else if (quality > 0.5 && quality < 0.7) {
            fishQuality = Quality.SILVER;
        } else if (quality > 0.7 && quality < 0.9) {
            fishQuality = Quality.GOLDEN;
        } else {
            fishQuality = Quality.IRIDIUM;
        }
        List<FishDetails> fishTypes = new ArrayList<>();

        //possible fish types
        for (FishDetails types : FishDetails.values()) {
            if (types.getSeason().equals(App.getCurrentGame().getDate().getSeason())) {
                if (!(types.isLegendary() && fishing.getLevel() != 4)) {
                    fishTypes.add(types);
                }
            }
        }

        List<FishDetails> randomItems = new ArrayList<>();
        for (int i = 0; i < numberOfCaught; i++) {
            int randomIndex = random.nextInt(fishTypes.size());
            randomItems.add(fishTypes.get(randomIndex));
        }

        for (FishDetails fishDetails : randomItems) {
            Item item = ItemBuilder.builder(fishDetails.getName(),fishQuality, fishDetails.getBasePrice());
            ItemBuilder.addToBackPack(item, 1, fishQuality);
        }

        fishing.increaseAmount(5);
        if(randomItems.size() > 0) {
            Main.getMain().setScreen(new FishingScreen(randomItems, players, fishQuality, numberOfCaught, fishingPole));
        }
        return new Result(true, "You just caught " + numberOfCaught + " fishes");
    }

    public Result sellAnimal(String animalName) {
        FarmAnimals animal = findAnimalByName(animalName);
        if (animal == null) {
            return new Result(false, "Animal not found");
        }
        App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals().remove(animal);
        int gainedMoney = (int) (animal.getAnimal().getPurchaseCost() * (((double) animal.getFriendShip() / 1000) + 0.3));
        App.getCurrentPlayerLazy().setMoney(gainedMoney + App.getCurrentPlayerLazy().getMoney());

        return new Result(true, "You just sold " + animalName);
    }

    public Result feedHay(String animalName) {
        FarmAnimals animal = findAnimalByName(animalName);
        if (animal == null) {
            return new Result(false, "Animal not found");
        }
        boolean hasHay = App.getCurrentPlayerLazy().getBackPack().getItemNames().containsKey("Hay");
        if (!hasHay) {
            return new Result(false, "You do not have any hay");
        }
        animal.setHasBeenFedToday(true);
        return new Result(true, "You feed to " + animalName);
    }

    public Result collectProduce(String animalName) {
        FarmAnimals animal = findAnimalByName(animalName);
        if (animal == null) {
            return new Result(false, "Animal not found");
        }

        if (animal.getDaysLeftToProduce() == 0 && !animal.isHasCollectedProductToday() && animal.isWillProduceToday()) {
            Animal type = animal.getAnimal();
            Quality quality = findQulaity(animal.getFriendShip());

            Random rand = new Random();
            double randDouble = rand.nextDouble();
            randDouble += 0.5;
            double willProduceGood = (animal.getFriendShip() + randDouble * 150) / 1500;
            double number = rand.nextDouble();
            switch (type) {
                case COW, GOAT -> {
                    return milking(animalName);
                }
                case SHEEP -> {
                    return shear(animalName);
                }
                case CHICKEN -> {
                    AnimalProduct product = animal.getFriendShip() >= 100 && willProduceGood >= number
                            ? AnimalProduct.LARGE_EGG
                            : AnimalProduct.EGG;
                    ItemBuilder.addToBackPack(ItemBuilder.builder(product.getName(), quality, product.getPrice()), 1, quality);
                }
                case DUCK -> {
                    AnimalProduct product = animal.getFriendShip() >= 150 && willProduceGood >= number
                            ? AnimalProduct.DUCK_FEATHER
                            : AnimalProduct.DUCK_EGG;
                    ItemBuilder.addToBackPack(ItemBuilder.builder(product.getName(), quality, product.getPrice()), 1, quality);
                }
                case RABBIT -> {
                    AnimalProduct product = animal.getFriendShip() >= 200 && willProduceGood >= number
                            ? AnimalProduct.RABBITS_PIE
                            : AnimalProduct.WOOL_RABBIT;
                    ItemBuilder.addToBackPack(ItemBuilder.builder(product.getName(), quality, product.getPrice()), 1, quality);
                }
                case DINOSAUR -> {
                    ItemBuilder.addToBackPack(ItemBuilder.builder(AnimalProduct.DINOSAUR_EGG.getName(), quality, AnimalProduct.DINOSAUR_EGG.getPrice()), 1, quality);
                }
                case PIG -> {
                    if (App.getCurrentGame().getDate().getSeason().equals(Season.WINTER) &&
                            !App.isLocationInPlace(animal.getPosition(), animal.getHome().getLocation())) {
                        return new Result(false, "Pigs do not produce truffles in winter");
                    }
                    ItemBuilder.addToBackPack(ItemBuilder.builder(AnimalProduct.TRUFFLE.getName(), quality, AnimalProduct.TRUFFLE.getPrice()), 1, quality);
                }
            }

            animal.setFriendShip(animal.getFriendShip() + 5);
            animal.setWillProduceToday(false);
            animal.setHasCollectedProductToday(true);
            return new Result(true, "You just collected product from " + animalName);
        } else {
            return new Result(false, "This animal will not produce today");
        }
    }

    public String whatWillProduceToday(Animal type, FarmAnimals animal) {
        Quality quality = findQulaity(animal.getFriendShip());

        Random rand = new Random();
        double randDouble = rand.nextDouble();
        randDouble += 0.5;
        double willProduceGood = (animal.getFriendShip() + randDouble * 150) / 1500;
        double number = rand.nextDouble();
        switch (type) {
            case COW, GOAT -> {
                return "milk";
            }
            case SHEEP -> {
                return "wool";
            }
            case CHICKEN -> {
                if (willProduceGood >= number)
                    return "Large Egg";
                else return "egg";

            }
            case DUCK -> {
                AnimalProduct product = animal.getFriendShip() >= 150 && willProduceGood >= number
                        ? AnimalProduct.DUCK_FEATHER
                        : AnimalProduct.DUCK_EGG;
                ItemBuilder.addToBackPack(ItemBuilder.builder(product.getName(), quality, product.getPrice()), 1, quality);
                if (willProduceGood >= number)
                    return "duck feather";
                else return "duck egg";
            }
            case RABBIT -> {
                if (willProduceGood >= number)
                    return "pagshm";
                else return "rabbit pie";

            }
            case DINOSAUR -> {
                return "dinosaur egg";
            }
            case PIG -> {
                if (App.getCurrentGame().getDate().getSeason().equals(Season.WINTER) &&
                        !App.isLocationInPlace(animal.getPosition(), animal.getHome().getLocation())) {
                    return ("Pigs do not produce truffles in winter");
                }
                return "pig truffle";
            }
        }
        return "wrong animal name";
    }
    public void moveAnimalStep(FarmAnimals animal, Location target) {
        Location current = animal.getPosition();
        if (target == null) {
            animal.setMoving(false);
            return;
        }

        int dx = target.getxAxis() - current.getxAxis();
        int dy = target.getyAxis() - current.getyAxis();

        if (dx == 0 && dy == 0) {
            animal.setMoving(false);
            animal.setTarget(null);
            return;
        }

        if (Math.abs(dx) <= 1 && Math.abs(dy) <= 1) {
            // Close enough, snap
            current.setObjectInTile(null);
            animal.setPreviousPosition(current);
            animal.setPosition(target);
            target.setObjectInTile(animal);
            animal.setMoving(false);
            animal.setTarget(null);
            return;
        }

        int stepX = Integer.compare(dx, 0);
        int stepY = Integer.compare(dy, 0);

        int nextX = current.getxAxis() + stepX;
        int nextY = current.getyAxis() + stepY;

        Location nextLocation = App.getCurrentGame().getMainMap().findLocation(nextX, nextY);
        if (nextLocation != null) {
            current.setObjectInTile(null);
            animal.setPreviousPosition(current);
            animal.setPosition(nextLocation);
            nextLocation.setObjectInTile(animal);
        } else {
            animal.setMoving(false);
            animal.setTarget(null);
        }
    }


}
