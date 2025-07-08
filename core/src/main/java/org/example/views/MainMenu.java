package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.MenusController.LoginRegisterMenuController;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Result;
import org.example.models.GameAssetManager;
import org.example.models.enums.Menu;

import java.io.File;
import java.util.Scanner;

public class MainMenu extends AppMenu implements Screen {
    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private final Label menuLabel;
    private final TextButton LogOut;
    private final TextButton exitButton;
    private final SelectBox<String> selectMenu = new SelectBox<>(skin);
    public Table table = new Table();

    public MainMenu() {

        Array<String> questions = new Array<>(new String[]{
            "profile menu", "pre_game menu"
        });
        selectMenu.setItems(questions);

        this.menuLabel = new Label("main menu", skin);
        this.LogOut = new TextButton("log out", skin);
        this.exitButton = new TextButton("Exit", skin);
    }

    @Override
    public void check(Scanner scanner) {
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();
        table.add(menuLabel);

        table.row().pad(50,0,30,0);  ;
        table.add(LogOut);
        table.row().pad(30,0,30,0);  ;
        table.add(selectMenu);
        table.row().pad(30,0,30,0);  ;
        table.add(exitButton);
        stage.addActor(table);

        selectMenu.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if(selectMenu.getSelected()
                    .equalsIgnoreCase("profile menu")) {
                    App.setCurrentMenu(Menu.profileMenu);
                    Main.getMain().getScreen().dispose();
                    System.out.println("profile menu selected");
                    Main.getMain().setScreen(new ProfileMenu());
                } else{
                    App.setCurrentMenu(Menu.GameMenu);
                    Main.getMain().getScreen().dispose();
                    Main.getMain().setScreen(new GameMenu());
                }
            }
        });

        LogOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //TODO:is this right?
                System.out.println("now you are logged out");
                File file = new File("StayLoggedIn.json");
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("Logged out successfully. StayLoggedIn.json removed.");
                    } else {
                        System.out.println("Failed to delete StayLoggedIn.json.");
                    }
                } else {
                    System.out.println("No StayLoggedIn.json file to delete.");
                }
                App.setCurrentMenu(Menu.RegisterMenu);
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new RegisterMenuView());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                App.setCurrentMenu(Menu.Exit);
                System.exit(0);
            }
        });
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        if (stage != null) {
            stage.getViewport().update(i, i1, true);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }
}
