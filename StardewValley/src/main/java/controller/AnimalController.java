package controller;

import models.Animal.FarmAnimals;
import models.Fundementals.*;
import models.enums.*;

public class AnimalController {

    public boolean isLocationInPlace(Location location, LocationOfRectangle place){
        return location.getxAxis() >= place.getTopLeftCorner().getxAxis() &&
                location.getxAxis() <= place.getTopLeftCorner().getxAxis() + place.getWidth() &&
                location.getyAxis() >= place.getTopLeftCorner().getyAxis() &&
                location.getyAxis() <= place.getTopLeftCorner().getyAxis() + place.getLength();
    }

    public FarmAnimals animalNextToUs(Location location){
        for(FarmAnimals animals : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()){
            if(isNextToUs(animals.getPosition())){
                return animals;
            }
        }
        return null;
    }

    public boolean isNextToUs(Location location){
        Location ourLocation = App.getCurrentPlayerLazy().getUserLocation();
        return ((location.getyAxis() - ourLocation.getyAxis()) <= 1 && (location.getyAxis() - ourLocation.getyAxis()) >= -1)
                && ((location.getxAxis() - ourLocation.getxAxis()) <= 1 && (location.getxAxis() - ourLocation.getxAxis()) >= -1);
    }
}
