package models;

import controller.MenusController.GameMenuController;
import models.Animal.FarmAnimals;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Fundementals.Player;
import models.Place.Farm;
import models.enums.Season;
import models.enums.Types.SeedTypes;
import models.enums.Types.TypeOfTile;
import models.enums.Weather;
import models.enums.foraging.*;

import java.util.*;

public class Date {
    GameMenuController gameMenuController;
    int hour;
    int year;
    int dayOfMonth; // Max : 28 days
    int dayOfWeek; //Max : 7 days
    Season season; // season changes after 28 days
    Weather weather;
    Weather tommorowWeather;
    Map<Season, List<Weather>> weatherOfSeason;
    int currentSeason; // value of each season

    //Date setUp
    public Date() {
        this.hour = 9; // the game starts at 9 AM
        this.dayOfMonth = 1;
        this.dayOfWeek = 1;
        this.season = Season.SPRING;
        this.currentSeason = season.getValue();
        this.year = 1900;
        this.weather = Weather.SUNNY;
        this.tommorowWeather = Weather.SUNNY;
        this.weatherOfSeason = initializeWeatherMap();
        this.gameMenuController = new GameMenuController();
    }

    public void changeAdvancedTime(int hour) {
        this.hour += hour;
        if (this.hour > 22) {
            this.hour -= 13;
            changeAdvancedDay(1);
            gameMenuController.sellByShippingAllPlayers();
            this.weather = this.tommorowWeather; // the day changes

            updateAllPlants();
            ThunderAndLightning();
            foragingAdd();
            changesDayAnimal();
            attackingCrow();
        }
    }

    public void attackingCrow() {
        List<Plant> plants = App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm();
        List<Tree> trees = App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getTrees();

        if (plants.size() + trees.size() < 16) return;

        Random random = new Random();

        for (Plant plant : new ArrayList<>(plants)) {
            Location location = plant.getLocation();
            if(App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getGreenHouse().getLocation().getLocationsInRectangle().contains(location)) {
                if (location == null) continue;
//                if (location.isProtectedByScarecrow()) continue;

                if (random.nextInt(100) < 1) {
                    if (plant.isGiantPlant()) {
                        removeGiantPlant(plant);
                    } else {
                        location.setObjectInTile(null);
                        location.setTypeOfTile(TypeOfTile.PLOUGHED_LAND);
                        plants.remove(plant);
                        System.out.println("crows attacked to the farm and damage plant in location: "+ location.getxAxis()+ ", " + location.getyAxis());
                    }
                }
            }
        }
        for (Tree tree : trees) {
            Location location = tree.getLocation();
            if(App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getGreenHouse().
                    getLocation().getLocationsInRectangle().contains(location)) {
                if (location == null) continue;
//                if (location.isProtectedByScarecrow()) continue;

                if (random.nextInt(100) < 1) {
                    location.setObjectInTile(null);
                    location.setTypeOfTile(TypeOfTile.PLOUGHED_LAND);
                    trees.remove(tree);
                    System.out.println("crows attacked to the farm and damage tree in location: "+ location.getxAxis()+ ", " + location.getyAxis());
                }
            }
        }
    }

    public void removeGiantPlant(Plant plant) {
        Location baseLocation = plant.getLocation();
        int x = baseLocation.getxAxis();
        int y = baseLocation.getyAxis();
        SeedTypes type = plant.getSeed().getType();

        int[][][] cornerOffsets = {
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, 0}, {0, 0}, {-1, 1}, {0, 1}},
                {{0, -1}, {1, -1}, {0, 0}, {1, 0}},
                {{-1, -1}, {0, -1}, {-1, 0}, {0, 0}}
        };

        for (int[][] squareOffsets : cornerOffsets) {
            boolean match = true;
            List<Location> toRemove = new ArrayList<>();

            for (int[] offset : squareOffsets) {
                int checkX = x + offset[0];
                int checkY = y + offset[1];
                Location loc = App.getCurrentGame().getMainMap().findLocation(checkX, checkY);

                if (loc == null || !(loc.getObjectInTile() instanceof Plant p) ||
                        !p.getSeed().getType().equals(type) || !p.isGiantPlant()) {
                    match = false;
                    break;
                }
                toRemove.add(loc);
            }

            if (match) {
                for (Location loc : toRemove) {
                    loc.setObjectInTile(null);
                    loc.setTypeOfTile(TypeOfTile.PLOUGHED_LAND);
                    App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().remove(loc.getObjectInTile());
                }
                return;
            }
        }
    }

    public void ThunderAndLightning () {
        ArrayList<Location> availableLocation = getLocations();

        List<Location> shuffled = new ArrayList<>(availableLocation);
        Collections.shuffle(shuffled);

        for (int i = 0; i < 3; i++) {
            Location location = shuffled.get(i);
            location.setTypeOfTile(TypeOfTile.BURNED_GROUND);
        }
    }

    public void updateAllPlants () {
        for (Farm farm : App.getCurrentGame().getMainMap().getFarms()) {
            for (Plant plant : farm.getPlantOfFarm()) {
                if (!plant.isHasBeenFertilized()) {
                    plant.setDayPast(plant.getDayPast() - 1);
                    if (plant.getDayPast() <= 0) {
                        System.out.println("you lost plant at location: " + plant.getLocation().getxAxis() + ", " + plant.getLocation().getyAxis());
                        Location currentLocation = plant.getLocation();
                        currentLocation.setTypeOfTile(TypeOfTile.PLOUGHED_LAND);
                        currentLocation.setObjectInTile(null);
                    }
                }
                plant.setHasBeenFertilized(false);
                plant.setAge(plant.getAge() + 1);
                int currentStage = plant.getCurrentStage();
                int dayNeed = 0;
                for (int i = 0; i < currentStage; i++) {
                    dayNeed += plant.getAllCrops().stages[i];
                }
                if (plant.getAge() >= dayNeed) {
                    plant.setCurrentStage(plant.getCurrentStage() + 1);
                }
            }

            for (Tree tree : farm.getTrees()) {
                if (!tree.isHasBeenFertilized()) {
                    tree.setDayPast(tree.getDayPast() - 1);
                    if (tree.getDayPast() <= 0) {
                        System.out.println("you lost plant at location: " + tree.getLocation().getxAxis() + ", " + tree.getLocation().getyAxis());
                        Location currentLocation = tree.getLocation();
                        currentLocation.setTypeOfTile(TypeOfTile.GROUND);
                        currentLocation.setObjectInTile(null);
                    }
                }
                tree.setHasBeenFertilized(false);
                tree.setAge(tree.getAge() + 1);
                int currentStage = tree.getCurrentStage();
                int dayNeed = 0;
                for (int i = 0; i < currentStage; i++) {
                    dayNeed += tree.getType().stages[i];
                }
                if (tree.getAge() >= dayNeed) {
                    tree.setCurrentStage(tree.getCurrentStage() + 1);
                }
            }
        }
    }

    public void foragingAdd () {
        for (Farm farm : App.getCurrentGame().getMainMap().getFarms()) {
            ArrayList<Location> availableLocation = getGroundLocation(farm);
            if (availableLocation.size() < 3) continue;

            Collections.shuffle(availableLocation);

            List<MineralTypes> allMinerals = new ArrayList<>(Arrays.asList(MineralTypes.values()));
            List<TreeType> foragingTrees = new ArrayList<>(
                    Arrays.stream(TreeType.values())
                            .filter(TreeType::isCanBeForaging)
                            .toList()
            );

            List<SeedTypes> allSeeds = new ArrayList<>(Arrays.asList(SeedTypes.values()));
            Collections.shuffle(allMinerals);
            Collections.shuffle(foragingTrees);
            Collections.shuffle(allSeeds);

            int count = Math.min(3, availableLocation.size());

            for (int i = 0; i < count; i++) {
                Location location = availableLocation.get(i);
                MineralTypes mineral = allMinerals.get(i);
                Stone newStone = new Stone(mineral);

                location.setTypeOfTile(TypeOfTile.STONE);
                location.setObjectInTile(newStone);
            }

            for (int i = 0; i < count; i++) {
                Location location = availableLocation.get(i);
                TreeType type = foragingTrees.get(i);
                Tree newTree = new Tree(location, type, true, type.getProduct());
                farm.getTrees().add(newTree);
                System.out.println("new Tree with type: " + newTree.getType().name + " add to location" +
                        location.getxAxis() + ", " + location.getyAxis());

                location.setTypeOfTile(TypeOfTile.TREE);
                location.setObjectInTile(newTree);
            }

            ArrayList<Location> seedPlacing = getLocationForSeeding(farm);
            Collections.shuffle(seedPlacing);
            int seedCount = Math.min(3, seedPlacing.size());

            for (int i = 0; i < seedCount; i++) {
                Location location = seedPlacing.get(i);
                SeedTypes seedSeason = allSeeds.get(i);
                Seed newSeed = new Seed(seedSeason);
                AllCrops allCrops = AllCrops.sourceTypeToCraftType(seedSeason);
                Plant newPlant = new Plant(location, newSeed, true, allCrops);
                farm.getPlantOfFarm().add(newPlant);
                System.out.println("new Plant with type: " + newPlant.getAllCrops().name() + " add to location" +
                        location.getxAxis() + ", " + location.getyAxis());

                location.setTypeOfTile(TypeOfTile.PLANT);
                location.setObjectInTile(newPlant);
            }
        }
    }


    public void changesDayAnimal(){
        for(Player player : App.getCurrentGame().getPlayers()){
            for(FarmAnimals animals : player.getOwnedFarm().getFarmAnimals()){
                if(!animals.isHasBeenFedToday()){
                    animals.setWillProduceToday(false);
                    animals.setFriendShip(animals.getFriendShip() - 10);
                }//he was fed in the passed day
                else {
                    animals.setWillProduceToday(true);
                }
                if(!animals.isHasBeenFedToday()){
                    animals.setFriendShip(animals.getFriendShip() - 20);
                } if(!App.isLocationInPlace(animals.getPosition(), animals.getHome().getLocation())){
                    animals.setFriendShip(animals.getFriendShip() - 20);
                }
                animals.setHasBeenFedToday(false);
                animals.setHasBeenPettedToday(false);
                animals.setDaysLeftToProduce(animals.getDaysLeftToProduce() + 1);
                if(animals.getDaysLeftToProduce() == animals.getAnimal().getProducingCycle()){
                    animals.setDaysLeftToProduce(0);
                }
            }
        }
    }

    public void changeAdvancedDay ( int day){
        this.dayOfWeek += day;
        if (this.dayOfWeek > 7) {
            this.dayOfWeek -= 7;
        }
        this.dayOfMonth += day;
        if (this.dayOfMonth > 28) {
            this.dayOfMonth -= 28;
            this.currentSeason = (this.currentSeason + 1) % 4;
            this.season = Season.values()[this.currentSeason];
        }
    }

    public String dayName ( int dayOfWeek){
        return switch (dayOfWeek) {
            case 1 -> "Sunday";
            case 2 -> "Monday";
            case 3 -> "Tuesday";
            case 4 -> "Wednesday";
            case 5 -> "Thursday";
            case 6 -> "Friday";
            case 7 -> "Saturday";
            default -> throw new IllegalStateException("Unexpected value: " + dayOfWeek);
        };
    }

    public int getHour () {
        return hour;
    }

    public int getYear () {
        return year;
    }

    public int getDayOfMonth () {
        return dayOfMonth;
    }

    public int getDayOfWeek () {
        return dayOfWeek;
    }

    public Season getSeason () {
        return season;
    }

    public int getCurrentSeason () {
        return currentSeason;
    }

    public String getDayName ( int dayOfWeek){
        return dayName(dayOfWeek);
    }

    public Weather getWeather () {
        return weather;
    }

    public void setWeather (Weather weather){
        this.weather = weather;
    }

    public Map<Season, List<Weather>> initializeWeatherMap () {
        weatherOfSeason = Map.of(Season.SPRING, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM), Season.SUMMER, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM), Season.AUTUMN, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM), Season.WINTER, List.of(Weather.SUNNY, Weather.SNOW));
        return weatherOfSeason;
    }

    public Weather weatherForecast (Season season){
        if (weatherOfSeason == null) {
            initializeWeatherMap();
        }

        List<Weather> possibleWeathers = weatherOfSeason.get(season);
        int randomIndex = (int) (Math.random() * possibleWeathers.size());
        return possibleWeathers.get(randomIndex);
    }

    public void setTommorowWeather (Weather weather){
        this.tommorowWeather = weather;
    }

    public void setDayOfMonth ( int dayOfMonth){
        this.dayOfMonth = dayOfMonth;
    }

    public void setDayOfWeek ( int dayOfWeek){
        this.dayOfWeek = dayOfWeek;
    }

    public void setSeason (Season season){
        this.season = season;
    }

    public void setHour ( int newHour){
        this.hour = newHour;
    }

    public ArrayList<Location> getLocations () {
        ArrayList<Location> availableLocations = new ArrayList<>();
        for (Location location : App.getCurrentGame().getMainMap().getTilesOfMap()) {
            if (location.getTypeOfTile() != TypeOfTile.BARN || location.getTypeOfTile() != TypeOfTile.LAKE ||
                    location.getTypeOfTile() != TypeOfTile.COOP || location.getTypeOfTile() != TypeOfTile.QUARRY ||
                    location.getTypeOfTile() != TypeOfTile.STONE || location.getTypeOfTile() != TypeOfTile.NPC_VILLAGE) {
                availableLocations.add(location);
            }
        }
        return availableLocations;
    }

    public ArrayList<Location> getGroundLocation (Farm farm){
        ArrayList<Location> availableLocations = new ArrayList<>();
        ArrayList<Location> allLocationOfFarm = getLocationsOfRectangle(farm.getFarmLocation());
        for (Location location : allLocationOfFarm) {
            if (location.getTypeOfTile().equals(TypeOfTile.PLOUGHED_LAND) || location.getTypeOfTile().equals(TypeOfTile.GROUND)) {
                availableLocations.add(location);
            }
        }
        return availableLocations;
    }

    public ArrayList<Location> getLocationForSeeding (Farm farm){
        ArrayList<Location> availableLocations = new ArrayList<>();
        ArrayList<Location> allLocationOfFarm = getLocationsOfRectangle(farm.getFarmLocation());
        for (Location location : allLocationOfFarm) {
            if (location.getTypeOfTile().equals(TypeOfTile.PLOUGHED_LAND)) {
                availableLocations.add(location);
            }
        }
        return availableLocations;
    }

    private ArrayList<Location> getLocationsOfRectangle (LocationOfRectangle rect){
        ArrayList<Location> result = new ArrayList<>();
        for (Location loc : App.getCurrentGame().getMainMap().getTilesOfMap()) {
            if (loc.getxAxis() >= rect.getTopLeftCorner().getxAxis() &&
                    loc.getxAxis() <= rect.getDownRightCorner().getxAxis() &&
                    loc.getyAxis() >= rect.getTopLeftCorner().getyAxis() &&
                    loc.getyAxis() <= rect.getDownRightCorner().getyAxis()) {
                result.add(loc);
            }
        }
        return result;
    }

    public int getDaysPassed(Date date){
        if (date == null) {
            return 0;
        }

        int currentTotalDays = ((int) (year - 1) * 4 * 28) + (currentSeason + 28) + dayOfMonth;
        int dateTotalDays = ((int) (date.year - 1) * 4 * 28) + (date.currentSeason + 28) + date.dayOfMonth;
        return currentTotalDays - dateTotalDays;
    }
}
