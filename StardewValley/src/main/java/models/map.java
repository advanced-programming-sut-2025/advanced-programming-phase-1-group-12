package models;

import controller.MapSetUp.FarmSetUp;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.MapDetails.GreenHouse;
import models.MapDetails.Lake;
import models.NPC.NPCvillage;
import models.Place.Farm;
import models.Place.Store;
import models.RelatedToUser.User;

import java.util.*;

public class map {

    private ArrayList<Farm> farms = new ArrayList<>();
    private ArrayList<Location> tilesOfMap = new ArrayList<>();
    public ArrayList<Farm> getFarms() {
        return farms;
    }

    public void setFarms(ArrayList<Farm> farms) {
        this.farms = farms;
    }

    public ArrayList<Location> getTilesOfMap() {
        return tilesOfMap;
    }

    public void setTilesOfMap(ArrayList<Location> tilesOfMap) {
        this.tilesOfMap = tilesOfMap;
    }

    public Location findLocation(int x, int y){
        for(Location location: App.getCurrentGame().getMainMap().getTilesOfMap()){
            if(location.getxAxis() == x){
                if(location.getyAxis() ==y)
                    return location;
            }
        }
        return null;
    }
}
