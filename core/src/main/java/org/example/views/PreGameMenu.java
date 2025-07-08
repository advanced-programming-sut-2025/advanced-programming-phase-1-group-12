package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.models.Fundementals.App;
import org.example.models.GameAssetManager;
import org.example.models.enums.Menu;

import java.util.List;
import java.util.Scanner;

public class PreGameMenu extends AppMenu implements Screen {

    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private final TextButton newGame;
    private final TextButton loadLastGame;
    private final TextButton showInformation;
    private final TextButton exitGame;
    public Table table = new Table();
    private final Array<String> playerNames = new Array<>();

    public PreGameMenu() {
        this.newGame = new TextButton("new Game", skin);
        this.loadLastGame = new TextButton("Load last game", skin);
        this.showInformation = new TextButton("show information of current play", skin);
        this.exitGame = new TextButton("Exit and delete game", skin);
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

        table.row().pad(30, 0, 10, 0);
        table.add(newGame).width(300).height(60);

        table.row().pad(30, 0, 10, 0);
        table.add(loadLastGame).width(300).height(60);

        table.row().pad(30, 0, 10, 0);
        table.add(showInformation).width(300).height(60);

        table.row().pad(30, 0, 10, 0);
        table.add(exitGame).width(300).height(60);

        stage.addActor(table);

        addListeners();
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }

    private void addListeners() {
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                table.clear(); // Clear current buttons
                playerNames.clear(); // Clear previous names if any

                Label instruction = new Label("Enter player names:", skin);
                TextField nameField = new TextField("", skin);
                TextButton addPlayerButton = new TextButton("+", skin);
                TextButton startGameButton = new TextButton("Start Game", skin);
                Table playersTable = new Table(); // To show added names

                table.row().pad(10);
                table.add(instruction).colspan(2).center();

                table.row().pad(10);
                table.add(nameField).width(200);
                table.add(addPlayerButton).width(60);

                table.row().pad(10);
                table.add(playersTable).colspan(2);

                table.row().pad(30);
                table.add(startGameButton).colspan(2).width(200).height(50);

                // Add player button listener
                addPlayerButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        String name = nameField.getText().trim();
                        if (!name.isEmpty() && !playerNames.contains(name, false)) {
                            playerNames.add(name);
                            nameField.setText("");

                            playersTable.clear();
                            for (String player : playerNames) {
                                playersTable.row().pad(5);
                                playersTable.add(new Label(player, skin));
                            }
                        }
                    }
                });

                // Start game button listener
                startGameButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (playerNames.size > 0) {
                            Main.getMain().setScreen(new GameMenu((List<String>) playerNames));
                        } else {
                            System.out.println("Add at least one player to start.");
                        }
                    }
                });
            }
        });

        loadLastGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Load last game clicked!");
                // TODO: Load game logic
            }
        });

        showInformation.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Show information clicked!");
                // TODO: Display info popup or new screen
            }
        });

        exitGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Exit game clicked!");
                // TODO: Clean up if needed before exit
//                Gdx.app.exit();
            }
        });
    }
}
