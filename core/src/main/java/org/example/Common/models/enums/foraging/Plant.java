package org.example.Common.models.enums.foraging;

import org.example.Common.models.Assets.PlantAssetsManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import com.badlogic.gdx.graphics.Texture;

import static org.example.Common.models.Assets.PlantAssetsManager.buildTextureMap;


public class Plant extends Item {
    private Location location;
    private PlantType plantType;
    private TypeOfPlant typeOfPlant;
    private boolean hasBeenFertilized;
    private boolean hasBeenWatering;
    private int totalTimeNeeded;
    private int dayPast;
    private int currentStage ;
    private int age;
    private boolean isForaging;
    private boolean isOneTime;
    private int regrowthTime;
    private boolean isGiantPlant;

    public Plant(Location location, boolean isForaging, TypeOfPlant typeOfPlant) {
        super(typeOfPlant.getName(), Quality.NORMAL, typeOfPlant.baseSellPrice);
        this.location = location;
        this.hasBeenFertilized = false;
        this.dayPast = 30;
        this.currentStage = 0;
        this.age = 0;
        this.isOneTime = true;
        this.isForaging = isForaging;
        this.totalTimeNeeded = typeOfPlant.totalHarvestTime;
        this.typeOfPlant = typeOfPlant;
        this.hasBeenWatering = false;
        this.regrowthTime = 0;
    }

    public int getTotalTimeNeeded() {
        return totalTimeNeeded;
    }

    public boolean isOneTime() {
        return isOneTime;
    }

    public void setOneTime(boolean oneTime) {
        isOneTime = oneTime;
    }

    public int getRegrowthTime() {
        return regrowthTime;
    }

    public void setRegrowthTime(int regrowthTime) {
        this.regrowthTime = regrowthTime;
    }

    public boolean isGiantPlant() {
        return isGiantPlant;
    }

    public void setGiantPlant(boolean giantPlant) {
        isGiantPlant = giantPlant;
    }

    public boolean isHasBeenFertilized() {
        return hasBeenFertilized;
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isForaging() {
        return isForaging;
    }

    public void setForaging(boolean foraging) {
        isForaging = foraging;
    }

    public boolean isHasBeenWatering() {
        return hasBeenWatering;
    }

    public void setHasBeenWatering(boolean hasBeenWatering) {
        this.hasBeenWatering = hasBeenWatering;
    }

    public int getDayPast() {
        return dayPast;
    }

    public void setDayPast(int dayPast) {
        this.dayPast = dayPast;
    }

    public void setHasBeenFertilized(boolean hasBeenFertilized) {
        this.hasBeenFertilized = hasBeenFertilized;
    }

    public void decreaseDayPast() {
        dayPast--;
    }

    public void setPlantType(PlantType plantType) {
        this.plantType = plantType;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    public TypeOfPlant getTypeOfPlant() {
        return typeOfPlant;
    }

    public void setTypeOfPlant(TypeOfPlant typeOfPlant) {
        this.typeOfPlant = typeOfPlant;
    }

    public Texture getTexture() {
        if (PlantAssetsManager.PLANT_TEXTURES.isEmpty()) {
            System.out.println("PLANT_TEXTURES is empty!");
            buildTextureMap();
        }

        Texture[] textures = PlantAssetsManager.PLANT_TEXTURES.get(typeOfPlant);

        if (textures != null && textures.length > 0) {
            return textures[0];
        }
        if(typeOfPlant.getName().contains("Seeds")){
            Plant plant = new Plant(App.getCurrentGame().getMainMap().findLocation(0, 0), false, TypeOfPlant.AMARANTH);
            return plant.getTexture();
        } if(typeOfPlant.getName().contains("Starter")){
            Plant plant = new Plant(App.getCurrentGame().getMainMap().findLocation(0, 0), false, TypeOfPlant.HOPS);
            return plant.getTexture();
        }
        return null;
    }

}
