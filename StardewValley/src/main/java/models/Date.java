package models;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Place.Farm;
import models.enums.Season;
import models.enums.Types.TypeOfTile;
import models.enums.Weather;
import models.enums.foraging.Plant;
import models.enums.foraging.Seed;

import java.util.*;

public class Date {
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
    }

    public void changeAdvancedTime(int hour) {
        this.hour += hour;
        if (this.hour > 22) {
            this.hour -= 13;
            changeAdvancedDay(1);
            this.weather = this.tommorowWeather; // the day changes
        }
        updateAllSeeds();
        ThunderAndLightning();
        foragingAdd();
    }

    public void ThunderAndLightning() {
        ArrayList<Location> availableLocation = getLocations();

        List<Location> shuffled = new ArrayList<>(availableLocation);
        Collections.shuffle(shuffled);

        for (int i = 0; i < 3; i++) {
            Location location = shuffled.get(i);
            location.setTypeOfTile(TypeOfTile.BURNED_GROUND);
        }
    }

    public void updateAllSeeds() {
        for (Farm farm : App.getCurrentGame().getMainMap().getFarms()) {
            for (Plant plant : farm.getPlantOfFarm()) {
                if (!plant.isForaging()) {
                    if (plant.getSeed().getType().getDay() > plant.getAge()) {
                        int newAge = plant.getAge() + 1;
                        plant.setAge(newAge);
                    } else {
                        Location currentLocation = plant.getLocation();
                        currentLocation.setTypeOfTile(TypeOfTile.PLANT);
                        currentLocation.setObjectInTile(plant);
                    }
                }
            }
        }
    }

    public void foragingAdd() {
        for(Farm farm : App.getCurrentGame().getMainMap().getFarms()) {
            ArrayList<Location> availableLocation = getGroundLocation();
        }
        List<Location> shuffled = new ArrayList<>(availableLocation);
        Collections.shuffle(shuffled);

        for (int i = 0; i < 3; i++) {
            Location location = shuffled.get(i);
            location.setTypeOfTile(TypeOfTile.BURNED_GROUND);
        }

    }

//    public void changesDayAnimal(){
//        for(FarmAnimals animals : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()){
//            if(!animals.isHasBeenFedToday()){
//                animals.setFriendShip(animals.getFriendShip() - 10);
//            }
//            if(!animals.isHasBeenFedToday()){
//                animals.setFriendShip(animals.getFriendShip() - 20);
//            } if(!App.isLocationInPlace(animals.getPosition(), animals.getHome().getLocation())){
//                animals.setFriendShip(animals.getFriendShip() - 20);
//            }
//            animals.setHasBeenFedToday(false);
//            animals.setHasBeenPettedToday(false);
//        }
//    }

    public void changeAdvancedDay(int day) {
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
            if (location.getTypeOfTile().equals(TypeOfTile.GROUND) || location.getTypeOfTile().equals(TypeOfTile.BURNED_GROUND)) {
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
}
