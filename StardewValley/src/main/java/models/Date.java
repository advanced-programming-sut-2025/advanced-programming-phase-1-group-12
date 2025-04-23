package models;

import models.enums.Season;
import models.enums.Weather;

public class Date {
    int hour;
    int year;
    int dayOfMonth; // Max : 28 days
    int dayOfWeek; //Max : 7 days
    Season season; // season changes after 28 days
    Weather weather;
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
    }

    public void changeAdvancedTime(int hour){
        this.hour += hour;
        if (this.hour > 22){
            this.hour -= 13;
            changeAdvancedDay(1);
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
}
