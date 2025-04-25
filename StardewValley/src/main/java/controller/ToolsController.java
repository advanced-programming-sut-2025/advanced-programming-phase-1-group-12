package controller;

import models.Fundementals.Result;

public class ToolsController {

    public void checkBackPack(){}
    public void equipTools(){}
    public Result showCurrentTool(){
        return null;
    }
    public Result showToolsAvailable(){
        return null;
    }
    public Result updateToolsCheck(String name, boolean isInSmithing){
        if(!isInSmithing){
            return null;
        }
        if(!checkUpdateToolMoney()){
            return null;
        }
        return null;
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
        return null;
    }
    public boolean isValidUse(){
        return false;
    }






}
