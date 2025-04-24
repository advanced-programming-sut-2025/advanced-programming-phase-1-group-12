package controller;

import models.Fundementals.Location;
import models.Fundementals.Result;
import models.MapDetails.GreenHouse;

import java.util.ArrayList;

public class WeatherController {
    public Result showCurrentWeather() {
        return Result.success("");
    }
    public Result weatherForecast() {
        return Result.success("");
    }
    public Result cheatWeatherSet(){
        return Result.success("");
    }
    public GreenHouse greenhouseBuild(){
        return new GreenHouse();
    }

    public boolean checkGreenhouseCondition() {
        return false;
    }

    public void enterNextDay(){}
    public void strikeThunder(ArrayList<Location> locations) {}
    public void chooseLocation(){}
    public ArrayList<Location> randomThreeLocationsThunder(){return null;}
}
