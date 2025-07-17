package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Player;
import org.example.models.Item;
import org.example.models.enums.foraging.Seed;
import org.example.models.BackPack;

import java.util.List;
import java.util.Map;

public class FarmingMenuView implements Screen {

    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private Table table;
    private TextButton backButton;
    private List<String> players;
    private final List<Player> playerList;

    public FarmingMenuView(List<String> players, List<Player> playerList) {
        this.players = players;
        this.playerList = playerList;
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
        System.out.println("hhhrhr");

        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            System.out.println(item.getName());
            if (item instanceof Seed) {
                Seed seed = (Seed) item;
                TextButton seedButton = new TextButton(seed.getName(), skin);
                seedButton.addListener(event -> {
                    plantSeed(seed);
                    return true;
                });
                table.add(seedButton).pad(10).width(200).height(50); // Define size for buttons
                table.row().padBottom(10); // Move to the next row with bottom padding
            }
        }

        backButton = new TextButton("Back", skin);
        backButton.addListener(event -> {
            Main.getMain().setScreen(new GameMenu(players)); // Transition to the game menu
            return true;
        });

        // Set back button size and alignment
        table.add(backButton).width(200).height(50).center().pad(10);
        table.row().padTop(20); // Give some space above the back button

        stage.addActor(table); // Add the table to the stage
    }

    private void plantSeed(Seed seed) {
        System.out.println("Planting seed: " + seed.getName());
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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
