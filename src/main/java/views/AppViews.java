package views;

import models.enums.Menu;
import models.Fundementals.App;

import java.util.Scanner;

public class AppViews {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {
            App.getCurrentMenu().checkCommand(scanner);
        } while (App.getCurrentMenu() != Menu.Exit);
    }
}