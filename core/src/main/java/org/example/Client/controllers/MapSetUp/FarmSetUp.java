package org.example.Client.controllers.MapSetUp;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.MapDetails.GreenHouse;
import org.example.Common.models.MapDetails.Lake;
import org.example.Common.models.MapDetails.Quarry;
import org.example.Common.models.MapDetails.Shack;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.enums.Types.TypeOfTile;
import org.example.Common.models.enums.foraging.MineralTypes;
import org.example.Common.models.enums.foraging.Stone;

import java.util.*;


public class FarmSetUp {

    static Random rand = new Random();

    private ArrayList<Location> getLocations(Farm farm, int numberOfTrees) {
        ArrayList<Location> candidates = getAvailableLocationsInsideFarm(farm);
        Collections.shuffle(candidates);

        ArrayList<Location> result = new ArrayList<>();
        for (int i = 0; i < Math.min(numberOfTrees, candidates.size()); i++) {
            result.add(candidates.get(i));
        }
        return result;
    }

    public ArrayList<Location> randomStone(Farm farm){
        int numberOfStones = rand.nextInt(6) + 3;
        return getLocations(farm, numberOfStones);
    }

    private ArrayList<Location> getAvailableLocationsInsideFarm(Farm farm) {
        ArrayList<Location> allInFarm = new ArrayList<>();
        Set<Location> occupied = new HashSet<>();

        LocationOfRectangle farmRect = farm.getLocation();
        for (Location location : App.getCurrentGame().getMainMap().getTilesOfMap()) {
            if (location.getxAxis() >= farmRect.getTopLeftCorner().getxAxis() &&
                    location.getxAxis() <= farmRect.getDownRightCorner().getxAxis() &&
                    location.getyAxis() >= farmRect.getTopLeftCorner().getyAxis() &&
                    location.getyAxis() <= farmRect.getDownRightCorner().getyAxis()) {
                if(location.getTypeOfTile() == TypeOfTile.GROUND) {
                    allInFarm.add(location);
                }
            }
        }

        occupied.addAll(getLocationsOfRectangle(farm.getLake1().getLocation()));
        occupied.addAll(getLocationsOfRectangle(farm.getLake2().getLocation()));
        occupied.addAll(getLocationsOfRectangle(farm.getGreenHouse().getLocation()));
        occupied.addAll(getLocationsOfRectangle(farm.getShack().getLocation()));
        occupied.addAll(getLocationsOfRectangle(farm.getQuarry().getLocation()));

        ArrayList<Location> available = new ArrayList<>();
        for (Location loc : allInFarm) {
            if (!occupied.contains(loc)) {
                available.add(loc);
            }
        }

        return available;
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

    public void makeFarm(Farm newFarm, int id) {
        if(id == 0) {
            lakeSetUp(newFarm.getLake1());
            lakeSetUp(newFarm.getLake2());
            quarrySetUp(newFarm.getQuarry());
            GreenHouse(newFarm.getGreenHouse());
            ShackSetUp(newFarm.getShack());
        }else if(id == 1){
            lakeSetUp(newFarm.getLake1());
            quarrySetUp(newFarm.getQuarry());
            quarrySetUp(newFarm.getQuarry2());
            GreenHouse(newFarm.getGreenHouse());
            ShackSetUp(newFarm.getShack());
        } else if (id == 2) {
            lakeSetUp(newFarm.getLake1());
            quarrySetUp(newFarm.getQuarry());
            GreenHouse(newFarm.getGreenHouse());
            GreenHouse(newFarm.getGreenHouse2());
            ShackSetUp(newFarm.getShack());
        }else{
            lakeSetUp(newFarm.getLake1());
            quarrySetUp(newFarm.getQuarry());
            GreenHouse(newFarm.getGreenHouse());
            ShackSetUp(newFarm.getShack());
            ShackSetUp(newFarm.getShack2());
        }
        for(Location location : randomStone(newFarm)){
            List<MineralTypes> allMinerals = new ArrayList<>(Arrays.asList(MineralTypes.values()));
            Collections.shuffle(allMinerals);
            MineralTypes mineralTypes = allMinerals.get(0);
            Stone stone = new Stone(mineralTypes);
            location.setObjectInTile(stone);
        }
    }

    public void lakeSetUp(Lake lake){
        for(int x = 0; x < lake.getLocation().getLength(); x++){
            for(int y = 0 ; y < lake.getLocation().getWidth(); y++){
                for(Location location : App.getCurrentGame().getMainMap().getTilesOfMap()){
                    if(location.getyAxis() == lake.getLocation().getDownRightCorner().getyAxis() - y &&
                            location.getxAxis() == lake.getLocation().getTopLeftCorner().getxAxis() + x){
                        location.setTypeOfTile(TypeOfTile.LAKE);
                    }
                }
            }
        }
    }

    public void quarrySetUp(Quarry quarry){
        for(int x = 0; x < quarry.getLocation().getLength(); x++){
            for(int y = 0 ; y < quarry.getLocation().getWidth(); y++){
                for(Location location : App.getCurrentGame().getMainMap().getTilesOfMap()){
                    if(location.getyAxis() == quarry.getLocation().getDownRightCorner().getyAxis() - y &&
                            location.getxAxis() == quarry.getLocation().getTopLeftCorner().getxAxis() + x){
                        location.setTypeOfTile(TypeOfTile.QUARRY);
                    }
                }
            }
        }
    }

    public void ShackSetUp(Shack shack){
        for (int x = 0; x < shack.getLocation().getLength(); x++) {
            for (int y = 0; y < shack.getLocation().getWidth(); y++) {
                for (Location location : App.getCurrentGame().getMainMap().getTilesOfMap()) {
                    if (location.getyAxis() == shack.getLocation().getDownRightCorner().getyAxis() - y &&
                            location.getxAxis() == shack.getLocation().getTopLeftCorner().getxAxis() + x) {
                        location.setTypeOfTile(TypeOfTile.HOUSE);
                    }
                }
            }
        }
    }


    public void GreenHouse(GreenHouse greenHouse){
        for(int x = 0; x < greenHouse.getLocation().getLength(); x++){
            for(int y = 0 ; y < greenHouse.getLocation().getWidth(); y++){
                for(Location location : App.getCurrentGame().getMainMap().getTilesOfMap()){
                    if(location.getyAxis() == greenHouse.getLocation().getDownRightCorner().getyAxis() - y &&
                            location.getxAxis() == greenHouse.getLocation().getTopLeftCorner().getxAxis() + x){
                        location.setTypeOfTile(TypeOfTile.GREENHOUSE);
                    }
                }
            }
        }
    }
}
