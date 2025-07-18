package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.FarmingController;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Player;
import org.example.models.Item;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.enums.foraging.Plant;
import org.example.models.BackPack;
import org.example.models.enums.foraging.Seed;

import java.util.List;
import java.util.Map;

public class FarmingMenuView implements Screen {

    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private Table table;
    private TextButton backButton;
    private final List<String> players;
    private final List<Player> playerList;
    private final Location location;
    private FarmingController farmingController = new FarmingController();

    public FarmingMenuView(List<String> players, List<Player> playerList, Location location) {
        this.players = players;
        this.playerList = playerList;
        this.location = location;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.top().left();
        table.setFillParent(true);

        BackPack backpack = App.getCurrentPlayerLazy().getBackPack();
        Map<Item, Integer> items = backpack.getItems();

        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            if (item instanceof Seed seed) {
                TextButton seedButton = new TextButton(seed.getName(), skin);
                seedButton.addListener(event -> {
                    farmingController.plant(seed.getType().getName(), location);
                    return true;
                });
                table.add(seedButton).pad(10).width(200).height(50);
                table.row().padBottom(10);
            }
        }

        backButton = new TextButton("Back", skin);
        backButton.addListener(event -> {
            Main.getMain().setScreen(new GameMenu(players));
            return true;
        });
        table.add(backButton).width(200).height(50).center().pad(10);
        table.row().padTop(20);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }
}
