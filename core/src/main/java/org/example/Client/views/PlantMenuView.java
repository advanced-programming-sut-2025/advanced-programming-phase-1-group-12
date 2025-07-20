package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Client.Main;
import org.example.Server.controllers.FarmingController;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.ToolsPackage.ToolEnums.Tool;
import org.example.Common.models.enums.foraging.Plant;

import java.util.List;

public class PlantMenuView implements Screen {
    private final Skin skin = GameAssetManager.skin;
    private Stage stage;
    private Dialog plantDialog;

    private final FarmingController farmingController = new FarmingController();
    private final Location location;
    private final List<String> players;

    public PlantMenuView(List<String> players, Location location) {
        this.location = location;
        this.players = players;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        buildPlantDialog();
        plantDialog.show(stage);
        centreDialog();
    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        centreDialog();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (stage != null) stage.dispose();
    }

    private void buildPlantDialog() {
        plantDialog = new Dialog("Plant Information", skin) {
            @Override
            protected void result(Object obj) {
                if (Boolean.TRUE.equals(obj)) {
                    // Close dialog & return to previous screen (if any logic is required, add it here)
                    stage.getViewport().getCamera().update();
                    // Simply hide for now; actual screen-switch logic should live in your Game class.
                    hide();
                }
            }
        };

        plantDialog.getTitleLabel().setAlignment(1); // centre title text
        plantDialog.setMovable(false);

        Table content = plantDialog.getContentTable();
        content.pad(20);

        Plant plant = (Plant) location.getObjectInTile();
        if (plant == null) {
            content.add(new Label("No plant found at this location.", skin)).left();
        } else {
            Table pTable = new Table(skin);
            pTable.defaults().left();

            pTable.add(new Label("Name: " + plant.getName(), skin)).row();
            pTable.add(new Label("Age: " + plant.getAge(), skin)).row();
            pTable.add(new Label("Current stage: " + plant.getCurrentStage(), skin)).row();
            pTable.add(new Label("Days until harvest: " + plant.getDayPast(), skin)).row();
            pTable.add(new Label("Fertilized: " + plant.isHasBeenFertilized(), skin)).row();
            pTable.add(new Label("Watered: " + plant.isHasBeenWatering(), skin)).row();
            pTable.add(new Label("One‑time harvest: " + plant.isOneTime(), skin)).row();
            pTable.add(new Label("Regrowth time: " + plant.getRegrowthTime(), skin)).row();
            pTable.add(new Label("Giant plant: " + plant.isGiantPlant(), skin)).row();

            content.add(pTable).padBottom(15).row();
        }

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(event -> {
            Main.getMain().setScreen(new GameMenu(players));
            return true;
        });
        content.add(backButton).width(200).height(50).center().pad(15);
        content.row().padTop(15);

        TextButton wateringButton = new TextButton("Watering", skin);
        wateringButton.addListener(event -> {
            if (App.getCurrentPlayerLazy().getCurrentTool() != null &&
                App.getCurrentPlayerLazy().getCurrentTool().getToolType().equals(Tool.WATERING_CAN)) {
                App.getCurrentPlayerLazy().getCurrentTool().use(location,
                    App.getCurrentPlayerLazy().getAbilityByName("Farming").getLevel());
            }
            Main.getMain().setScreen(new GameMenu(players));
            return true;
        });
        content.add(wateringButton).width(200).height(50).center().pad(15);
        content.row().padTop(15);

        plantDialog.pack();
    }

    private void centreDialog() {
        if (plantDialog != null) {
            plantDialog.setPosition(
                (stage.getWidth() - plantDialog.getWidth()) * 0.5f,
                (stage.getHeight() - plantDialog.getHeight()) * 0.5f
            );
        }
    }
}
