package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.FarmingController;
import org.example.controllers.ToolsController;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Assets.PlantAssetsManager;
import org.example.models.BackPack;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Game;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Result;
import org.example.models.Item;
import org.example.models.ToolsPackage.ToolEnums.Tool;
import org.example.models.enums.foraging.Fertilize;
import org.example.models.enums.foraging.FertilizeType;
import org.example.models.enums.foraging.Plant;

import java.util.List;
import java.util.Map;

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
            pTable.add(new Label("One‑time harvest: " + plant.getTypeOfPlant().oneTime, skin)).row();
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

        TextButton FertilizeButton = new TextButton("Fertilizing", skin);
        FertilizeButton.addListener(event -> {
            showFertilize();
            return true;
        });
        content.add(FertilizeButton).width(200).height(50).center().pad(15);
        content.row().padTop(15);

        TextButton reapingButton = new TextButton("Reaping", skin);
        reapingButton.addListener(event -> {
            if (App.getCurrentPlayerLazy().getCurrentTool() != null &&
                App.getCurrentPlayerLazy().getCurrentTool().getToolType().equals(Tool.SCYTHE)) {
                App.getCurrentPlayerLazy().getCurrentTool().use(location,
                    App.getCurrentPlayerLazy().getAbilityByName("Farming").getLevel());
                assert plant != null;
                if(plant.getCurrentStage() == plant.getTypeOfPlant().stages.length){
                    farmingController.reaping(location);
                }
            }
            Main.getMain().setScreen(new GameMenu(players));
            return true;
        });
        content.add(reapingButton).width(200).height(50).center().pad(15);
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

    private void showFertilize() {
        Dialog dialog = new Dialog("Choose Fertilizer", skin);
        dialog.setMovable(false);

        Table content = dialog.getContentTable();
        content.defaults().pad(10);

        BackPack backpack = App.getCurrentPlayerLazy().getBackPack();
        Map<Item, Integer> items = backpack.getItems();

        boolean hasFertilizers = false;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            int count = entry.getValue();

            if (item instanceof Fertilize fertilize) {
                hasFertilizers = true;

                FertilizeType type = FertilizeType.stringToFertilize(fertilize.getName());
                System.out.println(type);
                Texture texture = PlantAssetsManager.getFertilizer(type);
                Image image = new Image(texture);

                String displayName = fertilize.getName().replace("GENERAL_STORE_", "");
                TextButton button = new TextButton(displayName + " (x" + count + ")", skin);

                Table fertTable = new Table();
                fertTable.add(image).size(32, 32).padRight(10);
                fertTable.add(button).left();

                content.add(fertTable).center().row();

                button.addListener(event -> {
                    Result result = farmingController.fertilize(fertilize.getName(), location);
                    System.out.println(result.getMessage());
                    dialog.hide();
                    Main.getMain().setScreen(new GameMenu(players));
                    return true;
                });
            }
        }

        if (!hasFertilizers) {
            content.add(new Label("You have no fertilizers!", skin)).padTop(20).row();
        }

        dialog.button("Cancel", false).padTop(20);
        dialog.show(stage);

        dialog.setPosition(
            (stage.getWidth() - dialog.getWidth()) * 0.5f,
            (stage.getHeight() - dialog.getHeight()) * 0.5f
        );
    }
}
