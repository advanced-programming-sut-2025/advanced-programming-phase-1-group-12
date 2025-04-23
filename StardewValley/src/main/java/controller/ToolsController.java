package controller;

import models.Fundementals.Result;

public class ToolsController {

    public void checkBackPack(){}
    public void equipTools(){}
    public Result showCurrentTool(){
        return Result.success("");
    }
    public Result showToolsAvailable(){
        return Result.success("");
    }
    public Result updateToolsCheck(String name, boolean isInSmithing){
        if(!isInSmithing){
            return Result.failure("");
        }
        if(!checkUpdateToolMoney()){
            return Result.failure("");
        }
        return Result.success("");
    }
    public boolean checkIsInSmithing(){
        return false;
    }
    public boolean checkUpdateToolMoney(){
        return false;
    }
    public void updateTools(){}
    public void useTools(){}
    public void checkMoves(){}
    public boolean checkEnergy(){
        return false;
    }
    public Result checkToolUse(){
        return Result.success("");
    }
    public boolean isValidUse(){
        return false;
    }






}
