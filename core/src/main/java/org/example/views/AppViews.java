package org.example.views;

import org.example.models.enums.Menu;
import org.example.models.Fundementals.App;

import java.util.Scanner;

public class AppViews {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {

            App.getCurrentMenu().checkCommand(scanner);
        } while (App.getCurrentMenu() != Menu.Exit);
    }
}