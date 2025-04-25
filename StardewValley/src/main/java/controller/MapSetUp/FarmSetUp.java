package controller.MapSetUp;

import models.Fundementals.App;
import models.Fundementals.Game;
import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.MapDetails.GreenHouse;
import models.MapDetails.Lake;
import models.MapDetails.Quarry;
import models.MapDetails.Shack;
import models.Place.Farm;
import models.enums.Types.TypeOfTile;

import java.util.*;


public class FarmSetUp {
    Game mainGame = App.getCurrentGame();

    static Random rand = new Random();
    public ArrayList<Location> randomTree(Farm farm){
        int numberOfTrees = rand.nextInt(10) + 5;

        return getLocations(farm, numberOfTrees);
    }

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

        LocationOfRectangle farmRect = farm.getFarmLocation();
        for (Location location : mainGame.getTilesOfMap()) {
            if (location.getxAxis() >= farmRect.getTopLeftCorner().getxAxis() &&
                    location.getxAxis() <= farmRect.getDownRightCorner().getxAxis() &&
                    location.getyAxis() >= farmRect.getTopLeftCorner().getyAxis() &&
                    location.getyAxis() <= farmRect.getDownRightCorner().getyAxis()) {
                allInFarm.add(location);
            }
        }

        occupied.addAll(getLocationsOfRectangle(farm.getLake1().locationOfRectangle()));
        occupied.addAll(getLocationsOfRectangle(farm.getLake2().locationOfRectangle()));
        occupied.addAll(getLocationsOfRectangle(farm.getGreenHouse().locationOfRectangle()));
        occupied.addAll(getLocationsOfRectangle(farm.getShack().locationOfRectangle()));
        occupied.addAll(getLocationsOfRectangle(farm.getQuarry().locationOfRectangle()));

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
        for (Location loc : mainGame.getTilesOfMap()) {
            if (loc.getxAxis() >= rect.getTopLeftCorner().getxAxis() &&
                    loc.getxAxis() <= rect.getDownRightCorner().getxAxis() &&
                    loc.getyAxis() >= rect.getTopLeftCorner().getyAxis() &&
                    loc.getyAxis() <= rect.getDownRightCorner().getyAxis()) {
                result.add(loc);
            }
        }
        return result;
    }

    public Farm makeFarm(Farm newFarm) {
        lakeSetUp(newFarm.getLake1());
        lakeSetUp(newFarm.getLake2());
        quarrySetUp(newFarm.getQuarry());
        ShackSetUp(newFarm.getShack());
        GreenHouse(newFarm.getGreenHouse());

        for(Location location : randomTree(newFarm)){
            location.setTypeOfTile(TypeOfTile.TREE);
        }
        for(Location location : randomStone(newFarm)){
            location.setTypeOfTile(TypeOfTile.STONE);
        }
        return newFarm;
    }

    public void lakeSetUp(Lake lake){
        for(int x = 0; x < lake.locationOfRectangle().getLength(); x++){
            for(int y = 0 ; y < lake.locationOfRectangle().getWidth(); y++){
                for(Location location : mainGame.getTilesOfMap()){
                    if(location.getyAxis() == lake.locationOfRectangle().getDownRightCorner().getyAxis() + y &&
                            location.getxAxis() == lake.locationOfRectangle().getTopLeftCorner().getxAxis() + x){
                        location.setTypeOfTile(TypeOfTile.LAKE);
                    }
                }
            }
        }
    }

    public void quarrySetUp(Quarry quarry){
        for(int x = 0; x < quarry.locationOfRectangle().getLength(); x++){
            for(int y = 0 ; y < quarry.locationOfRectangle().getWidth(); y++){
                for(Location location : mainGame.getTilesOfMap()){
                    if(location.getyAxis() == quarry.locationOfRectangle().getDownRightCorner().getyAxis() + y &&
                            location.getxAxis() == quarry.locationOfRectangle().getTopLeftCorner().getxAxis() + x){
                        location.setTypeOfTile(TypeOfTile.QUARRY);
                    }
                }
            }
        }
    }

    public void ShackSetUp(Shack shack){
        for(int x = 0; x < shack.locationOfRectangle().getLength(); x++){
            for(int y = 0 ; y < shack.locationOfRectangle().getWidth(); y++){
                for(Location location : mainGame.getTilesOfMap()){
                    if(location.getyAxis() == shack.locationOfRectangle().getDownRightCorner().getyAxis() + y &&
                            location.getxAxis() == shack.locationOfRectangle().getTopLeftCorner().getxAxis() + x){
                        location.setTypeOfTile(TypeOfTile.HOUSE);
                    }
                }
            }
        }
    }

    public void GreenHouse(GreenHouse greenHouse){
        for(int x = 0; x < greenHouse.locationOfRectangle().getLength(); x++){
            for(int y = 0 ; y < greenHouse.locationOfRectangle().getWidth(); y++){
                for(Location location : mainGame.getTilesOfMap()){
                    if(location.getyAxis() == greenHouse.locationOfRectangle().getDownRightCorner().getyAxis() + y &&
                            location.getxAxis() ==greenHouse.locationOfRectangle().getTopLeftCorner().getxAxis() + x){
                        location.setTypeOfTile(TypeOfTile.GREENHOUSE);
                    }
                }
            }
        }
    }
}
