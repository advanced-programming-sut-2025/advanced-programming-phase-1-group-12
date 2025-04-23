package org.example.controller.MenusController;

import org.example.models.RelatedToUser.User;
import org.example.models.Fundementals.Result;
import org.example.models.*;
import org.example.models.enums.Season;
import org.example.models.enums.Weather;

import java.util.Map;

public class GameMenuController implements MenuController {
    public Result newGame() { return null;}

    public map choosingMap(int MapId){ return null;}

    public Result loadGame(){ return null;}

    public void savingMap(Map<map, User>userAndTheirMap){}

    public Result deleteGame(int MapId){ return null;}

    public void nextTurn(){ }

    //TODO:sper chiz ro bezanim baraye geragten babash

    public void readingMap(){ }

    public void energyUnlimited(){}

    public Result sellProducts(String productName, int Count){
        return null;
    }

    public void tradeHistory(User user){}


    // Samin: date comands are here

    public Result showHour(){
        int currentHour = App.getCurrentGame().getDate().getHour();
        return new Result(String.format("%s", currentHour), true);
    }

    public Result showDate(){
        int currentDay = App.getCurrentGame().getDate().getDayOfMonth();
        int currentYear = App.getCurrentGame().getDate().getYear();
        return new Result(String.format("%d/%d", currentYear, currentDay), true);
    }

    public Result showDateTime(){
        int currentDay = App.getCurrentGame().getDate().getDayOfMonth();
        int currentYear = App.getCurrentGame().getDate().getYear();
        int currentHour = App.getCurrentGame().getDate().getHour();
        return new Result(String.format("%d/%d - Time: %d", currentYear,
                            currentDay, currentHour), true);
    }

    public Result showDayOfTheWeek(){
        int currentDay = App.getCurrentGame().getDate().getDayOfWeek();
        String StringDay = App.getCurrentGame().getDate().getDayName(currentDay);
        return new Result(StringDay, true);
    }

    public Result cheatAdvancedTime(String time){
        int hour;
        try{
            hour = Integer.parseInt(time);
        }catch(NumberFormatException e){
            return new Result("Wrong Hour!", false);
        }
        App.getCurrentGame().getDate().changeAdvancedTime(hour);
        return new Result("Time changed successfully!", true);
    }

    public Result cheatAdvancedDay(String day){
        int days;
        try{
            days = Integer.parseInt(day);
        }catch(NumberFormatException e){
            return new Result("Wrong Day!", false);
        }
        App.getCurrentGame().getDate().changeAdvancedDay(days);
        return new Result("Day changed successfully!", true);
    }

    public Result showSeason(){
        Season season = App.getCurrentGame().getDate().getSeason();
        switch(season){
            case SPRING -> {
                return new Result("Spring", true);
            }
            case SUMMER -> {
                return new Result("Summer", true);
            }
            case AUTUMN -> {
                return new Result("Autumn", true);
            }
            case WINTER -> {
                return new Result("Winter", true);
            }
            default -> {
                return new Result("Wrong Season!", false);
            }
        }
    }

    public Result showWeather(){
        return new Result(App.getCurrentGame().getDate().getWeather().name(), true);
    }

    public Result ShowWeatherForecast(Season season){
        Weather weather = App.getCurrentGame().getDate().weatherForecast(season);
        return new Result(weather.name(), true);
    }

    public Result cheatWeather(String type){
        Weather weather = Weather.fromString(type);
        App.getCurrentGame().getDate().setTommorowWeather(weather);
        String result = "Weather cheated successfully!";
        return new Result(result, true);
    }
}
