package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.Gson;
import org.example.models.Fundementals.App;
import org.example.models.RelatedToUser.User;
import org.example.models.enums.Menu;
import org.example.views.MainMenu;
import org.example.views.RegisterMenuView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private static Main main;
    private SpriteBatch batch;

    public static Main getMain() {
        return main;
    }

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();

        autoLoginIfPossible();

        if (App.getLoggedInUser() == null) {
            main.setScreen(new RegisterMenuView()); // Or LoginMenuView
        } else {
            System.out.println("Welcome back, " + App.getLoggedInUser().getUserName() + "! (Auto-logged in)");
            App.setCurrentMenu(Menu.MainMenu);
            main.setScreen(new MainMenu()); // Or whatever the main screen is
        }
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    private void autoLoginIfPossible() {
        File file = new File("StayLoggedIn.json");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Gson gson = new Gson();
                User user = gson.fromJson(reader, User.class);
                App.setLoggedInUser(user);
                App.getUsers().put(user.getUserName(), user);
            } catch (IOException e) {
                System.out.println("Error loading StayLoggedIn.json: " + e.getMessage());
            }
        }
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public static void setMain(Main main) {
        Main.main = main;
    }
}
