package controller;

import models.Fundementals.Result;

public class EnergyController {
    public Result showEnergy() {
        return Result.success("");
    }

    public void loseTurn(){}
    public void setEnergy(int energy) {}
    public void setUnlimitedEnergy() {}
    public Result showInventory(){
        return Result.success("");
    }
    public void trashInventory() {}
    public boolean shouldCollapse(){return false;}

}
