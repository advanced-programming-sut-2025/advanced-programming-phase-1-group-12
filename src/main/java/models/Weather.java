package models;

import models.enums.Season;
import models.enums.Types.WeatherType;

import java.util.List;

public class Weather {
    private String name;
    private List<Season> seasons;
    private WeatherType type;

    public Weather(String name, List<Season> season) {
        this.name = name;
        this.seasons = season;
    }

    public String getName() {
        return name;
    }
    public List<Season> getSeasons() {
        return seasons;
    }
    public void addSeason(Season season) {
        seasons.add(season);
    }
    public WeatherType getType() {
        return type;
    }
    public void setType(WeatherType type) {
        this.type = type;
    }

}
