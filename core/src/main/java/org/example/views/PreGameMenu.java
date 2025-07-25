package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Main;
import org.example.models.Fundementals.*;
import org.example.models.Assets.GameAssetManager;
import org.example.models.MapDetails.GreenHouse;
import org.example.models.MapDetails.Lake;
import org.example.models.MapDetails.Quarry;
import org.example.models.MapDetails.Shack;
import org.example.models.Place.Farm;
import org.example.models.RelatedToUser.User;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.map;

import java.io.File;
import java.util.*;
import java.util.List;

public class PreGameMenu implements Screen {

    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private Image backgroundImage;
    private final Label menuLabel;
    private final TextButton newGame;
    private final TextButton loadLastGame;
    private final TextButton showInformation;
    private final TextButton exitGame;
    public Table table = new Table();
    private final Array<String> playerNames = new Array<>();

    public PreGameMenu() {
        this.menuLabel = new Label("pre_game menu", skin);

        this.newGame = new TextButton("new Game", skin);
        this.loadLastGame = new TextButton("Load last game", skin);
        this.showInformation = new TextButton("show information of current play", skin);
        this.exitGame = new TextButton("Exit and delete game", skin);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Texture backgroundTexture = new Texture(Gdx.files.internal("menu_background.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        table.setFillParent(true);
        table.center();
        table.add(menuLabel);

        table.row().pad(30, 0, 10, 0);
        table.add(newGame).width(300).height(60);

        table.row().pad(30, 0, 10, 0);
        table.add(loadLastGame).width(300).height(60);

        table.row().pad(30, 0, 10, 0);
        table.add(showInformation).width(300).height(60);

        table.row().pad(30, 0, 10, 0);
        table.add(exitGame).width(300).height(60);
        stage.addActor(backgroundImage);
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

                startGameButton.addListener(new ClickListener() {
                    @Override public void clicked(InputEvent event, float x, float y) {

                        if (playerNames.size == 0) {
                            new Dialog("Error", skin)
                                .text("At least one player is required!")
                                .button("OK").show(stage);
                            return;
                        }
                        App.loadAllUsersFromFiles();

                        Iterator<String> iterator = playerNames.iterator();
                        while (iterator.hasNext()) {
                            String playerName = iterator.next().trim();
                            User user = App.getUserByUsername(playerName);
                            if (user == null) {
                                new Dialog("Player Error", skin)
                                    .text("User not found: " + playerName + ". Removed from list.")
                                    .button("OK").show(stage);
                                iterator.remove(); // remove from list

                                // refresh the playersTable visually
                                playersTable.clear();
                                for (String validPlayer : playerNames) {
                                    playersTable.row().pad(5);
                                    playersTable.add(new Label(validPlayer, skin));
                                }
                                return;
                            }
                        }
                        // Correct conversion from LibGDX Array to java.util.List
                        List<String> playersList = new ArrayList<>();
                        playersList.add(App.getLoggedInUser().getUserName());
                        for (String name : playerNames) {
                            playersList.add(name);
                        }
                        Main.getMain().setScreen(new FarmGraphicSelectionMenu(playersList));
                    }
                });
            }
        });

        loadLastGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                loadGameById(1);
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
                Gdx.app.exit();
            }
        });
    }

    public void loadGameById(int gameIdToLoad) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("saves/saved_game" + gameIdToLoad + ".json");

        if (!file.exists())
            return;

        try {
            System.out.println("Reading JSON file...");
            Map<String, Object> saveData = mapper.readValue(file, new TypeReference<>() {});
            List<Map<String, Object>> playersData = (List<Map<String, Object>>) saveData.get("players");
            List<Map<String, Object>> mapData = (List<Map<String, Object>>) saveData.get("mainMap");

            System.out.println("Building map...");
            map mainMap = new map();
            for (Map<String, Object> tileMap : mapData) {
                int x = ((Number) tileMap.get("x")).intValue(); // safer casting
                int y = ((Number) tileMap.get("y")).intValue();
                String typeName = (String) tileMap.get("typeOfTile");

                TypeOfTile type = TypeOfTile.valueOf(typeName);
                Location loc = new Location(x, y);
                loc.setTypeOfTile(type);
                mainMap.getTilesOfMap().add(loc);
            }

            System.out.println("Rebuilding players...");
            ArrayList<Player> players = new ArrayList<>();
            for (Map<String, Object> playerMap : playersData) {
                String username = (String) playerMap.get("username");
                int x = ((Number) playerMap.get("x")).intValue();
                int y = ((Number) playerMap.get("y")).intValue();
                int energy = ((Number) playerMap.get("energy")).intValue();

                Location loc = new Location(x, y);
                User user = App.getUserByUsername(username);

                Player player = new Player(user, loc, false, null, null, null, null, false, false, null);
                player.setUserLocation(loc);
                player.setEnergy(energy);

                System.out.println("Restoring farm for player " + username);
                Map<String, Object> farmMap = (Map<String, Object>) playerMap.get("farm");
                Farm farm = new Farm(mapToRect((Map<String, Object>) farmMap.get("location")));

                farm.setFarmLocation(mapToRect((Map<String, Object>) farmMap.get("location")));
                farm.setLake1(new Lake(mapToRect((Map<String, Object>) farmMap.get("lake1"))));
                farm.setLake2(new Lake(mapToRect((Map<String, Object>) farmMap.get("lake2"))));
                farm.setGreenHouse(new GreenHouse(mapToRect((Map<String, Object>) farmMap.get("greenhouse1"))));
                farm.setGreenHouse2(new GreenHouse(mapToRect((Map<String, Object>) farmMap.get("greenhouse2"))));
                farm.setShack(new Shack(mapToRect((Map<String, Object>) farmMap.get("shack1"))));
                farm.setShack2(new Shack(mapToRect((Map<String, Object>) farmMap.get("shack2"))));
                farm.setQuarry(new Quarry(mapToRect((Map<String, Object>) farmMap.get("quarry1"))));
                farm.setQuarry2(new Quarry(mapToRect((Map<String, Object>) farmMap.get("quarry2"))));
                farm.setOwner(player);

                player.setOwnedFarm(farm);
                players.add(player);
            }

            System.out.println("Finalizing game setup...");
            Game loadedGame = new Game();
            loadedGame.setPlayers(players);
            loadedGame.setMainMap(mainMap);
            App.setCurrentGame(loadedGame);
            App.getCurrentGame().setCurrentPlayer(players.get(0));

            for (Player player : App.getCurrentGame().getPlayers()) {
                Location loc = player.getUserLocation();
                App.getCurrentGame().getMainMap().findLocation(loc.getxAxis(), loc.getyAxis()).setObjectInTile(player);
                Farm farm = player.getOwnedFarm();
                App.getCurrentGame().getMainMap().getFarms().add(farm);
            }

            System.out.println("Game loaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    private LocationOfRectangle mapToRect(Map<String, Object> map) {
        Map<String, Integer> topLeft = (Map<String, Integer>) map.get("topLeft");
        Map<String, Integer> bottomRight = (Map<String, Integer>) map.get("bottomRight");
        return new LocationOfRectangle(
            new Location(topLeft.get("x"), topLeft.get("y")),
            new Location(bottomRight.get("x"), bottomRight.get("y"))
        );
    }
}
