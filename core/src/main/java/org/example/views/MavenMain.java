package org.example.views;

import com.google.gson.Gson;
import org.example.models.Fundementals.App;
import org.example.models.RelatedToUser.User;
import org.example.models.enums.Menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MavenMain {
    public static void main(String[] args) {
        File file = new File("StayLoggedIn.json");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Gson gson = new Gson();
                User user = gson.fromJson(reader, User.class);

                App.setLoggedInUser(user);
                App.getUsers().put(user.getUserName(), user);
                App.setCurrentMenu(Menu.MainMenu);

                System.out.println("Welcome back, " + user.getUserName() + "! (Auto-logged in)");
            } catch (IOException e) {
                System.out.println("Error loading StayLoggedIn.json: " + e.getMessage());
            }
        } else {
            System.out.println("No user is currently logged in.");
        }
        (new AppViews()).run();
    }
}