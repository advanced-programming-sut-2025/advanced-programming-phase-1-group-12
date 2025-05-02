package models;

import models.enums.Season;
import models.enums.Weather;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Date(){
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

    public void changeAdvancedTime(int hour){
        this.hour += hour;
        if (this.hour > 22){
            this.hour -= 13;
            changeAdvancedDay(1);
            this.weather = this.tommorowWeather; // the day changes
        }
    }

    public void changeAdvancedDay(int day){
        this.dayOfWeek += day;
        if (this.dayOfWeek > 7){
            this.dayOfWeek -= 7;
        }
        this.dayOfMonth += day;
        if (this.dayOfMonth> 28){
            this.dayOfMonth -= 28;
            this.currentSeason = (this.currentSeason + 1)%4;
            this.season = Season.values()[this.currentSeason];
        }
    }

    public String dayName(int dayOfWeek){
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

    public String getDayName(int dayOfWeek){
        return dayName(dayOfWeek);
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    public Map<Season, List<Weather>> initializeWeatherMap() {
        weatherOfSeason = Map.of(
                Season.SPRING, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM),
                Season.SUMMER, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM),
                Season.AUTUMN, List.of(Weather.SUNNY, Weather.RAINY, Weather.STORM),
                Season.WINTER, List.of(Weather.SUNNY, Weather.SNOW)
        );
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

    public void setTommorowWeather(Weather weather){
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
}
