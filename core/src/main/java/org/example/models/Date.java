package org.example.models;

import com.badlogic.gdx.Gdx;
import org.example.models.Animal.FarmAnimals;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.LocationOfRectangle;
import org.example.models.Fundementals.Player;
import org.example.models.NPC.NPC;
import org.example.models.Place.Farm;
import org.example.models.Place.Store;
import org.example.models.ProductsPackage.ArtisanItem;
import org.example.models.ProductsPackage.Quality;
import org.example.models.ProductsPackage.StoreProducts;
import org.example.models.enums.Season;
import org.example.models.enums.Types.CraftingRecipe;
import org.example.models.enums.foraging.SeedTypes;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.enums.Weather;
import org.example.models.enums.foraging.*;

import java.util.*;

public class Date implements Runnable {
    private int hour;
    private int year;
    private int dayOfMonth;
    private int dayOfWeek;
    private Season season;
    private Weather weather;
    private Weather tommorowWeather;
    private Map<Season, List<Weather>> weatherOfSeason;
    private int currentSeason;
    private boolean threadRunning = true;
    private Thread timeThread;

    public Date() {
        System.out.println("kaftar");
        timeThread = new Thread(this);
        timeThread.setDaemon(true);
        timeThread.start();
        this.hour = 9; // the game starts at 9 AM
        this.dayOfMonth = 1;
        this.dayOfWeek = 1;
        this.season = Season.SPRING;
        this.currentSeason = season.getValue();
        this.year = 2025;
        this.weather = Weather.SUNNY;
        this.weatherOfSeason = initializeWeatherMap();
        this.tommorowWeather = weatherForecast(season);
    }

    public void run(){
        while(threadRunning){
            try {
                Thread.sleep(6000); //every 6 seconds 1 hour passes
                changeAdvancedTime(1);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    public void stop(){
        threadRunning = false;
        if (timeThread != null) {
            timeThread.interrupt();
        }
    }

    public void changeAdvancedTime(int hour) {
        artisansUpdate(hour);
        this.hour += hour;
        if (this.hour >= 22) {
            changeAdvancedDay(1);
            this.hour -= 13;
        }
    }

    public void artisansUpdate(int hours) {
        for (Player player : App.getCurrentGame().getPlayers()) {
            Iterator<ArtisanItem> iterator = player.getArtisansGettingProcessed().iterator();
            while (iterator.hasNext()) {
                ArtisanItem item = iterator.next();
                // Reduce remaining processing time
                int newHoursRemaining = item.getHoursRemained() - hours;
                item.setHoursRemained(newHoursRemaining);

            }
        }
    }

//    public void attackingCrow() {
//        if (App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().size() +
//            App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getTrees().size() < 16) return;
//
//        Random random = new Random();
//        int randomInt = random.nextInt(100);
//
//        List<Plant> plants = App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm();
//        List<Plant> destroyablePlants = new ArrayList<>();
//        List<Tree> trees = App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getTrees();
//
//        if (randomInt < 26 && !plants.isEmpty()) {
//            for (Plant plant : new ArrayList<>(plants)) {
//                if (plant.isGiantPlant()) {
//                    removeGiantPlant(plant);
//                    System.out.println("crows attacked and damaged a GiantPlant at: " +
//                        plant.getLocation().getxAxis() + ", " + plant.getLocation().getyAxis());
//                    return;
//                } else {
//                    destroyablePlants.add(plant);
//                }
//            }
//
//            int plantsToDamage = Math.min(3, destroyablePlants.size());
//            Collections.shuffle(destroyablePlants);
//
//            for (int i = 0; i < plantsToDamage; i++) {
//                Plant plant = destroyablePlants.get(i);
//                Location location = plant.getLocation();
//                if(location.getObjectInTile() instanceof ArtisanItem){
//                    System.out.println("crows attacked but can't damaged a Tile cause it product with scareCrow!");
//                    break;
//                }
//                location.setObjectInTile(null);
//                location.setTypeOfTile(TypeOfTile.PLOUGHED_LAND);
//                plants.remove(plant);
//                System.out.println("crows attacked and damaged a plant at: " +
//                    location.getxAxis() + ", " + location.getyAxis());
//            }
//        }
//
//        if (randomInt > 74 && !trees.isEmpty()) {
//            int treesToDamage = Math.min(3, trees.size());
//            Collections.shuffle(trees);
//
//            for (int i = 0; i < treesToDamage; i++) {
//                Tree tree = trees.get(i);
//                Location location = tree.getLocation();
//                location.setObjectInTile(null);
//                location.setTypeOfTile(TypeOfTile.GROUND);
//                System.out.println("crows attacked and damaged tree at " + location.getxAxis() + ", " + location.getyAxis());
//            }
//
//            trees.subList(0, treesToDamage).clear();
//        }
//    }

    public void removeGiantPlant(Plant plant) {
        Location baseLocation = plant.getLocation();
        int x = baseLocation.getxAxis();
        int y = baseLocation.getyAxis();
        SeedTypes type = plant.getTypeOfPlant().getSource();

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
                    !p.getTypeOfPlant().getSource().equals(type) || !p.isGiantPlant()) {
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

    public void ThunderAndLightning() {
        ArrayList<Location> availableLocation = getLocations();

        List<Location> shuffled = new ArrayList<>(availableLocation);
        Collections.shuffle(shuffled);

        for (int i = 0; i < 3; i++) {
            Location location = shuffled.get(i);
            location.setTypeOfTile(TypeOfTile.BURNED_GROUND);

            Gdx.app.postRunnable(() -> {
                ItemBuilder.addToBackPack(ItemBuilder.builder("Coal", Quality.NORMAL, 0), 1, Quality.NORMAL);
                System.out.println("you take a coal and it add to your back pack!!");
            });
        }
    }

    public void updateAllPlants() {
//        for(Location location : App.getCurrentGame().getMainMap().getTilesOfMap()){
//            if(App.getCurrentGame().getDate().weather.name().equalsIgnoreCase("rainy") &&
//                !location.getTypeOfTile().equals(TypeOfTile.GREENHOUSE)){
//                if(location.getTypeOfTile().equals(TypeOfTile.PLANT)){
//                    Plant plant = (Plant) location.getObjectInTile();
//                    plant.setHasBeenWatering(true);
//                }
//            }
//            if(location.getTypeOfTile().equals(TypeOfTile.BURNED_GROUND)){
//                location.setTypeOfTile(TypeOfTile.GROUND);
//            }
//        }
        for (Farm farm : App.getCurrentGame().getMainMap().getFarms()) {
            for (Plant plant : farm.getPlantOfFarm()) {
                if (plant.isHasBeenWatering()) {
                    plant.setHasBeenWatering(false);
                    continue;
                }

                if (!plant.isHasBeenFertilized()) {
                    plant.setDayPast(plant.getDayPast() - 1);
                    if (plant.getDayPast() < 0) {
                        System.out.println("you lost plant at location: " + plant.getLocation().getxAxis() + ", " + plant.getLocation().getyAxis());
                        Location currentLocation = plant.getLocation();
                        currentLocation.setTypeOfTile(TypeOfTile.BURNED_GROUND);
                        currentLocation.setObjectInTile(null);
                        continue;
                    }
                }

                plant.setHasBeenFertilized(false);
                plant.setAge(plant.getAge() + 1);

                int currentStage = plant.getCurrentStage();
                int dayNeed = 0;
                for (int i = 0; i < currentStage; i++) {
                    dayNeed += plant.getTypeOfPlant().stages[i];
                }
                if (plant.getAge() >= dayNeed) {
                    plant.setCurrentStage(plant.getCurrentStage() + 1);
                }

                if (!plant.getTypeOfPlant().oneTime && !plant.isOneTime()) {
                    plant.setRegrowthTime(plant.getRegrowthTime() + 1);
                    if (plant.getRegrowthTime() >= plant.getTypeOfPlant().regrowthTime) {
                        plant.setRegrowthTime(0);
                        plant.setOneTime(true);
                    }
                }
            }
        }
    }

    public void foragingAdd() {
        for (Farm farm : App.getCurrentGame().getMainMap().getFarms()) {
            ArrayList<Location> availableLocation = getGroundLocation(farm);
            if (availableLocation.size() < 3) continue;

            Collections.shuffle(availableLocation);

            List<MineralTypes> allMinerals = new ArrayList<>(Arrays.asList(MineralTypes.values()));

            List<TypeOfPlant> foragingTrees = new ArrayList<>();
            for(TypeOfPlant typeOfPlant : TypeOfPlant.values()){
                if(typeOfPlant.getPlantType() == PlantType.ForagingTree)
                    foragingTrees.add(typeOfPlant);
            }

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

            ArrayList<Location> seedPlacing = getLocationForSeeding(farm);
            Collections.shuffle(seedPlacing);
            int seedCount = Math.min(3, seedPlacing.size());

            for (int i = 0; i < seedCount; i++) {
                Location location = seedPlacing.get(i);
                SeedTypes seedSeason = allSeeds.get(i);
                TypeOfPlant allCrops = TypeOfPlant.sourceTypeToCraftType(seedSeason);
                assert allCrops != null;
                Plant newPlant = new Plant(location, true, allCrops);
                farm.getPlantOfFarm().add(newPlant);
                if (newPlant.getTypeOfPlant() != null)
                    System.out.println("new Plant with type: " + newPlant.getTypeOfPlant().getName() + " add to location" +
                        location.getxAxis() + ", " + location.getyAxis());
                else
                    System.out.println("new Plant with type: " + newPlant.getTypeOfPlant().getSource().getName().toLowerCase() + " add to location" +
                        location.getxAxis() + ", " + location.getyAxis());
                location.setTypeOfTile(TypeOfTile.PLANT);
                location.setObjectInTile(newPlant);
            }
            for (int i = 0; i < 1; i++) {
                ArrayList<Location> quarryLocation = App.getCurrentPlayerLazy().getOwnedFarm().getQuarry().getLocation().getLocationsInRectangle();
                Collections.shuffle(quarryLocation);

                Location location = quarryLocation.get(i);
                MineralTypes mineral = allMinerals.get(i);
                Stone newStone = new Stone(mineral);
                location.setTypeOfTile(TypeOfTile.STONE);
                location.setObjectInTile(newStone);
            }
        }
    }

    public void changesDayAnimal() {
        for (Player player : App.getCurrentGame().getPlayers()) {
            for (FarmAnimals animals : player.getOwnedFarm().getFarmAnimals()) {
                if (!animals.isHasBeenFedToday()) {
                    animals.setWillProduceToday(false);
                    animals.setFriendShip(animals.getFriendShip() - 10);
                }//he was fed in the passed day
                else {
                    animals.setWillProduceToday(true);
                }
                if (!animals.isHasBeenFedToday()) {
                    animals.setFriendShip(animals.getFriendShip() - 20);
                }
                if (!App.isLocationInPlace(animals.getPosition(), animals.getHome().getLocation())) {
                    animals.setFriendShip(animals.getFriendShip() - 20);
                }
                animals.setHasBeenFedToday(false);
                animals.setHasBeenPettedToday(false);
                animals.setDaysLeftToProduce(animals.getDaysLeftToProduce() + 1);
                if (animals.getDaysLeftToProduce() == animals.getAnimal().getProducingCycle()) {
                    animals.setDaysLeftToProduce(0);
                }
            }
        }
    }

    public void changeAdvancedDay(int day) {

        if (day == 1) {
            this.weather = this.tommorowWeather;
        }
        this.dayOfWeek += day;
        if (this.dayOfWeek > 7) {
            this.dayOfWeek -= 7;
        }
        this.dayOfMonth += day;
        if (this.dayOfMonth > 28) {
            this.dayOfMonth -= 28;
            this.currentSeason = (this.currentSeason + 1) % 4;
            this.season = Season.values()[this.currentSeason];
            if (this.season.equals(Season.SUMMER)) {
                changeYear();
            }
        }
        sellByShippingAllPlayers();

        updateAllPlants();
//        ThunderAndLightning();
        foragingAdd();
        changesDayAnimal();
//        attackingCrow();
        resetNPCStatus();
        artisansUpdate(day * 13);
        buffUpdates();
        updateRecepies();
        resetDailyLimit();
    }

    public void resetDailyLimit() {
        for(Store store: App.getCurrentGame().getMainMap().getStores()){
            for(StoreProducts products : store.getStoreProducts()){
                products.setCurrentDailyLimit(products.getType().getDailyLimit());
            }
        }
    }

    public void buffUpdates(){
        if(App.getCurrentPlayerLazy().isMaxEnergyBuffEaten()){
            App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - 10000);
            App.getCurrentPlayerLazy().setMaxEnergyBuffEaten(false);
        }
        if(App.getCurrentPlayerLazy().isSkillBuffEaten()){
            App.getCurrentPlayerLazy().getAbilityByName("Farming").setLevel(App.getCurrentGame().getCurrentPlayer().getAbilityByName("Farming").getLevel() - 1);
            App.getCurrentPlayerLazy().setSkillBuffEaten(false);
        }
    }

    public String dayName(int dayOfWeek) {
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

    public int getHour() {
        return hour;
    }

    public int getYear() {
        return year;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public Season getSeason() {
        return season;
    }

    public int getCurrentSeason() {
        return currentSeason;
    }

    public String getDayName(int dayOfWeek) {
        return dayName(dayOfWeek);
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Map<Season, List<Weather>> initializeWeatherMap() {
        weatherOfSeason = Map.of(Season.SPRING, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM), Season.SUMMER, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM), Season.AUTUMN, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM), Season.WINTER, List.of(Weather.SUNNY, Weather.SNOW));
        return weatherOfSeason;
    }

    public Weather weatherForecast(Season season) {
        if (weatherOfSeason == null) {
            initializeWeatherMap();
        }

        List<Weather> possibleWeathers = weatherOfSeason.get(season);
        int randomIndex = (int) (Math.random() * possibleWeathers.size());
        return possibleWeathers.get(randomIndex);
    }

    public void setTommorowWeather(Weather weather) {
        this.tommorowWeather = weather;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    public void setHour(int newHour) {
        this.hour = newHour;
    }

    public ArrayList<Location> getLocations() {
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

    public ArrayList<Location> getGroundLocation(Farm farm) {
        ArrayList<Location> availableLocations = new ArrayList<>();
        ArrayList<Location> allLocationOfFarm = getLocationsOfRectangle(farm.getFarmLocation());
        for (Location location : allLocationOfFarm) {
            if (location.getTypeOfTile().equals(TypeOfTile.PLOUGHED_LAND) || location.getTypeOfTile().equals(TypeOfTile.GROUND)) {
                availableLocations.add(location);
            }
        }
        return availableLocations;
    }

    public ArrayList<Location> getLocationForSeeding(Farm farm) {
        ArrayList<Location> availableLocations = new ArrayList<>();
        ArrayList<Location> allLocationOfFarm = getLocationsOfRectangle(farm.getFarmLocation());
        for (Location location : allLocationOfFarm) {
            if (location.getTypeOfTile().equals(TypeOfTile.PLOUGHED_LAND)) {
                availableLocations.add(location);
            }
        }
        return availableLocations;
    }

    private ArrayList<Location> getLocationsOfRectangle(LocationOfRectangle rect) {
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

    public int getDaysPassed(Date date) {
        if (date == null) {
            return 0;
        }

        int currentTotalDays = ((int) (year - 1) * 4 * 28) + (currentSeason + 28) + dayOfMonth;
        int dateTotalDays = ((int) (date.year - 1) * 4 * 28) + (date.currentSeason + 28) + date.dayOfMonth;
        return currentTotalDays - dateTotalDays;
    }

    public void changeYear() {
        this.year = year + 1;
    }

    public void resetNPCStatus() {
        if (App.getCurrentGame().getNPCvillage() == null) {
            return;
        }
        for (NPC npc : App.getCurrentGame().getNPCvillage().getAllNPCs()) {
            npc.resetAllTalkedStatuses();
            npc.resetAllGiftedStatuses();
        }
    }

    public void sellByShippingAllPlayers() {
        for (Player player : App.getCurrentGame().getPlayers()) {
            System.out.println(player.getShippingMoney());
            player.increaseMoney(player.getShippingMoney());
            player.setShippingMoney(0);
        }
    }

    public void updateRecepies(){
        if(App.getCurrentPlayerLazy().getAbilityByName("Mining").getLevel() >= 2 && App.getCurrentPlayerLazy().getAbilityByName("Farming").getLevel() >= 2){
            for(CraftingRecipe recipe : CraftingRecipe.values()){
                if(!recipe.getName().equalsIgnoreCase("Fish Smoker") && !recipe.getName().equalsIgnoreCase("Dehydrator")){
                    App.getCurrentPlayerLazy().getRecepies().put(recipe, true);
                }
            }
        }
    }
}
