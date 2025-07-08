package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.MenusController.GameMenuController;
import org.example.models.GameAssetManager;
import java.util.*;
import java.util.List;

public class FarmGraphicSelectionMenu implements Screen {
    private Stage stage = new Stage(new ScreenViewport());
    private Skin skin = GameAssetManager.skin;
    private List<String> players;
    private Map<Integer, ImageButton> farmButtons = new HashMap<>();
    private Map<String, Integer> chosenFarms = new HashMap<>();
    private int currentPlayerIndex = 0;
    private Label promptLabel;

    public FarmGraphicSelectionMenu(List<String> players) {
        this.players = players;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        table.setFillParent(true);
        promptLabel = new Label("", skin, "title");
        table.add(promptLabel).colspan(2).padBottom(20);
        table.row();

        for (int i = 0; i < 4; i++) {
            final int farmId = i;
            Texture farmTexture = new Texture(Gdx.files.internal("farms/farm" + farmId + ".png"));
            ImageButton farmButton = new ImageButton(new Image(farmTexture).getDrawable());
            farmButtons.put(farmId, farmButton);

            farmButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    chosenFarms.put(players.get(currentPlayerIndex), farmId);
                    farmButtons.get(farmId).setColor(Color.DARK_GRAY);
                    farmButtons.get(farmId).setDisabled(true);
                    nextPlayerOrStart();
                }
            });

            table.add(farmButton).size(250, 250).pad(10);
            if (i % 2 == 1) table.row();
        }

        stage.addActor(table);
        updatePrompt();
    }

    private void updatePrompt() {
        promptLabel.setText("Player " + players.get(currentPlayerIndex) + ", choose your farm:");
    }

    private void nextPlayerOrStart() {
        currentPlayerIndex++;
        if (currentPlayerIndex >= players.size()) {
            startGame();
        } else {
            updatePrompt();
        }
    }

    private void startGame() {
        GameMenuController controller = new GameMenuController();
        controller.Play(null, players, chosenFarms);
        Main.getMain().setScreen(new GameMenu(players));
    }

    @Override public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int w, int h) { stage.getViewport().update(w, h, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
