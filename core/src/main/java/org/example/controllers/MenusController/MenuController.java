package org.example.controllers.MenusController;

import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Result;
import org.example.models.enums.Menu;

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
