package controller.MapSetUp;

import models.Fundementals.App;
import models.Fundementals.Game;
import models.Fundementals.Location;
import models.MapDetails.Lake;
import models.Place.Farm;
import models.enums.Types.TypeOfTile;
import java.util.ArrayList;

public class FarmSetUp {
    Game mainGame = App.getCurrentGame();

    public ArrayList<Location> randomTree(Farm farm){
        //int numberOfTree = rand.....
        //TODO: change the type in mapSetUp
        return null;
    }

    public ArrayList<Location> randomStone(Farm farm){
        //int numberOfTree = rand.....
        //TODO: change the type in mapSetUp
        return null;
    }

    public Farm makeFarm(Farm newFarm) {
        lakeSetUp(newFarm.getLake1());
        lakeSetUp(newFarm.getLake2());
        
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
}
