package views;

import com.google.gson.Gson;
import models.RelatedToUser.User;
import models.enums.Menu;
import models.Fundementals.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AppViews {

    public void run() {
        Scanner scanner = new Scanner(System.in);
        do {

            App.getCurrentMenu().checkCommand(scanner);
        } while (App.getCurrentMenu() != Menu.Exit);
    }
}