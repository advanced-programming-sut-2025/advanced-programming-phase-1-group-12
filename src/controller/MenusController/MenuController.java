package controller.MenusController;

import models.Fundementals.App;
import models.Fundementals.Result;
import models.enums.Menu;

public interface MenuController {
    public static void menuExit(){
        App.setCurrentMenu(Menu.Exit);
    }

    public static Result menuEnter(){
        return null;
    }

    public static Result showMenu(){
        return null;
    }
}
