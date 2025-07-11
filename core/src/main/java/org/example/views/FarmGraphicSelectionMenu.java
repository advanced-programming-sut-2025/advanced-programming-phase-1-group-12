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
import org.example.controllers.MenusController.GameMenuController;
import org.example.models.Assets.GameAssetManager;
import java.util.*;
import java.util.List;

public class FarmGraphicSelectionMenu implements Screen {
    private Stage stage = new Stage(new ScreenViewport());
    private Skin skin = GameAssetManager.skin;
    private List<String> players;
    private Map<String, Integer> chosenFarms = new HashMap<>();
    private Map<Integer, Label> farmLabels = new HashMap<>();
    private int currentPlayerIndex = 0;
    private Label promptLabel;

    private Image farmImage;
    private int imgWidth, imgHeight;

    public FarmGraphicSelectionMenu(List<String> players) {
        this.players = players;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Texture collageTexture = new Texture(Gdx.files.internal("farm_collage.png"));
        imgWidth = collageTexture.getWidth();
        imgHeight = collageTexture.getHeight();

        farmImage = new Image(collageTexture);

        farmImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int selectedFarm = getFarmIdFromCoordinates(x, y);

                if (!chosenFarms.containsValue(selectedFarm)) {
                    String playerName = players.get(currentPlayerIndex);
                    chosenFarms.put(playerName, selectedFarm);
                    markFarmAsChosen(playerName, selectedFarm);
                    currentPlayerIndex++;
                    if (currentPlayerIndex >= players.size()) {
                        startGame();
                    } else {
                        updatePrompt();
                    }
                }
            }
        });

        Table table = new Table();
        table.setFillParent(true);

        promptLabel = new Label("", skin);
        table.add(promptLabel).padBottom(10);
        table.row();
        table.add(farmImage).size(imgWidth, imgHeight);

        stage.addActor(table);
        updatePrompt();
    }

    private int getFarmIdFromCoordinates(float x, float y) {
        boolean top = y > imgHeight / 2;
        boolean left = x < imgWidth / 2;

        if (top && left) return 0;
        if (top && !left) return 1;
        if (!top && left) return 2;
        return 3;
    }

    private void markFarmAsChosen(String playerName, int farmId) {
        float labelX = (farmId % 2 == 0) ? imgWidth / 4f : 3f * imgWidth / 4f;
        float labelY = (farmId < 2) ? 3f * imgHeight / 4f : imgHeight / 4f;

        Label nameLabel = new Label(playerName, skin);
        nameLabel.setColor(Color.WHITE);
        nameLabel.setPosition(farmImage.getX() + labelX - nameLabel.getWidth() / 2,
            farmImage.getY() + labelY - nameLabel.getHeight() / 2);

        farmLabels.put(farmId, nameLabel);
        stage.addActor(nameLabel);

        Image overlay = new Image(skin.newDrawable("white", new Color(0, 0, 0, 0.5f)));
        overlay.setBounds((farmId % 2 == 0) ? farmImage.getX() : farmImage.getX() + imgWidth / 2f,
            (farmId < 2) ? farmImage.getY() + imgHeight / 2f : farmImage.getY(),
            imgWidth / 2f, imgHeight / 2f);
        stage.addActor(overlay);
    }

    private void updatePrompt() {
        promptLabel.setText("Player " + players.get(currentPlayerIndex) + ", choose your farm:");
    }

    private void startGame() {
        GameMenuController controller = new GameMenuController();
        controller.Play(players, chosenFarms);
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
