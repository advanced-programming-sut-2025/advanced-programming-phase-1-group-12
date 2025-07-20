package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Client.Main;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Item;
import org.example.Common.models.ItemBuilder;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.RelatedToUser.Ability;
import org.example.Common.models.enums.FishDetails;

import java.util.List;

public class FishingScreen extends InputAdapter implements Screen {

    private Stage fishingStage;
    private SpriteBatch fishingBatch;
    private OrthographicCamera fishingCamera;

    private Texture waterTexture;
    private Texture fishTexture;
    private Texture bobberTexture;

    private float bobberYPosition;
    private FishDetails fishDetail;
    private List<FishDetails> fishDetails;
    private int fishY;
    List<String> players;
    ProgressBar progressBar;
    private float progressTimer = 0.0f;
    private int progress = 4;
    private FishMovementType movementType = FishMovementType.MIXED;
    private float fishStateTimer = 0;
    private int fishDirection = 0; // -1 = down, 0 = still, 1 = up
    private Quality fishQuality;
    private boolean perfectFish = true;
    private int numberOfFishes;
    private boolean fishingOver = false;
    Label errorLabel;
    Skin skin = GameAssetManager.skin;
    private String message = "";
    private String bobberType;

    public FishingScreen(List<FishDetails> fishDetails, List<String> players, Quality fishQuality, int numberOfFishes, String bobberType) {
        this.bobberType = bobberType;
        this.fishDetails = fishDetails;
        this.fishDetail = fishDetails.get(numberOfFishes - 1);
        this.fishTexture = fishDetails.get(numberOfFishes - 1).getTexture();
        fishingStage = new Stage(new ScreenViewport());
        fishingBatch = new SpriteBatch();
        fishingCamera = new OrthographicCamera();
        fishingCamera.setToOrtho(false, 800, 600); // Adjust the screen size
        this.fishY = 350;
        this.players = players;
        this.numberOfFishes = numberOfFishes;
        errorLabel = new Label("", skin);
        ProgressBar.ProgressBarStyle style = skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class);
        progressBar = new ProgressBar(0, 1, 0.01f, false, style);
        this.fishQuality = fishQuality;
        initializeprogressBar(skin, fishingCamera);
        int rand = MathUtils.random(0, 5);
        switch (rand) {
            case 0:
                movementType = FishMovementType.MIXED;
                break;
            case 1:
                movementType = FishMovementType.DART;
                break;
            case 2:
                movementType = FishMovementType.FLOATER;
                break;
            case 3:
                movementType = FishMovementType.SINKER;
                break;
            case 4:
                movementType = FishMovementType.SMOOTH;
                break;
        }
        System.out.println(movementType.name());
    }

    @Override
    public void show() {
        // Load textures
        waterTexture = GameAssetManager.getGameAssetManager().getLAKE_TEXTURE(); // Your water background texture
        //rodTexture = new Texture("fishing_rod.png");
        bobberTexture = new Texture("Flooring/Flooring_86.png");

        // Set the bobber starting position
        bobberYPosition = 500; // Adjust this based on your scene height
        fishingBatch = new SpriteBatch(); // Create new batch instead of using global one
        fishingStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(fishingStage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        if (!fishingOver) {
            progressTimer += delta;
            if (progressTimer >= 0.5f) {
                float bobberTop = bobberYPosition + 50;
                float bobberBottom = bobberYPosition;

                if (fishY >= bobberBottom && fishY <= bobberTop) {
                    progress += 4;
                } else {
                    perfectFish = false;
                    progress--;
                }
                progressTimer = 0;
            }

            progress = MathUtils.clamp(progress, 0, 20);
            handleInput(delta);
            updateFishMovement(delta);
            bobberYPosition = Math.max(0, Math.min(bobberYPosition, 800 - 50));
        }

        fishingBatch.setProjectionMatrix(fishingCamera.combined);
        fishingBatch.begin();

        fishingBatch.draw(waterTexture, 0, 0, 1000, 800);
        fishingBatch.draw(bobberTexture, 580, bobberYPosition, 15, 50);
        fishingBatch.draw(fishTexture, 380, fishY);
        if ("Bamboo Pole".equals(bobberType)) {
            BitmapFont font = new BitmapFont();
            font.setColor(1, 0, 0, 1);  // Red color for the name
            font.draw(fishingBatch, fishDetail.getName(), 380, fishY + 100);
        }

        drawprogressBar(fishingCamera);

        fishingBatch.end();
        fishingStage.act(delta);
        fishingStage.draw();
    }


    private void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            bobberYPosition += 200 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            bobberYPosition -= 200 * delta;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            System.out.println("Exiting fishing screen...");
            GameMenu gameMenu = new GameMenu(players);
            Main.getMain().setScreen(gameMenu);
        }
    }


    private void catchFish() {
        Item item = ItemBuilder.builder(fishDetail.getName(), fishQuality, fishDetail.getBasePrice());
        ItemBuilder.addToBackPack(item, 1, fishQuality);
        message = "you caught it";
        if (perfectFish) {
            message = "you caught it perfect!!!";
        }
        endOneFish();
    }

    @Override
    public void resize(int width, int height) {
        fishingCamera.viewportWidth = width;
        fishingCamera.viewportHeight = height;
        fishingCamera.update();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        waterTexture.dispose();
        bobberTexture.dispose();
        fishTexture.dispose();
        fishingBatch.dispose();
    }

    public void initializeprogressBar(Skin skin, OrthographicCamera cam) {
        float camRight = cam.position.x + cam.viewportWidth / 2;
        float camTop = cam.position.y + cam.viewportHeight / 2;
        ProgressBar.ProgressBarStyle style = skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class);
        progressBar = new ProgressBar(0, 1, 0.01f, false, style);

        progressBar.setSize(250, 30);
        progressBar.setPosition(camRight - 760, camTop - 40);
        fishingStage.addActor(progressBar);
    }


    private void drawprogressBar(OrthographicCamera cam) {
        float camRight = cam.position.x + cam.viewportWidth / 2;
        float camTop = cam.position.y + cam.viewportHeight / 2;

        float progress1 = (float) progress / 20;
        progress1 = Math.min(progress1, 1);

        progressBar.setValue(progress1);
        if (!fishingOver) {
            if (progress1 <= 0) {
                message = "you lost it";
                numberOfFishes--;
                endOneFish();
            } else if (progress1 >= 1) {
                numberOfFishes--;
                if (perfectFish) {
                    applyPerfectFish();
                }
                catchFish();
            }
        }

        progressBar.setPosition(camRight - 760, camTop - 40);

        progressBar.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        progressBar.draw(fishingBatch, 1);
    }

    private void updateFishMovement(float delta) {
        fishStateTimer += delta;

        if (fishStateTimer > 0.3f) {
            fishStateTimer = 0;

            switch (movementType) {
                case MIXED:
                    // up/down/stay randomly every frame
                    fishDirection = MathUtils.random(-1, 1);
                    break;

                case SMOOTH:
                    if (MathUtils.random() < 0.7f) {
                        // keep same direction
                    } else {
                        fishDirection = MathUtils.random(-1, 1);
                    }
                    break;

                case SINKER:
                    if (fishDirection == 0) {
                        fishDirection = (MathUtils.random() < 0.7f) ? -1 : MathUtils.random(-1, 1);
                    } else {
                        fishDirection = MathUtils.random(-1, 1);
                    }
                    break;

                case FLOATER:
                    if (fishDirection == 0) {
                        fishDirection = (MathUtils.random() < 0.7f) ? 1 : MathUtils.random(-1, 1);
                    } else {
                        fishDirection = MathUtils.random(-1, 1);
                    }
                    break;

                case DART:
                    fishDirection = MathUtils.random(-1, 1);
                    break;
            }
        }

        if (movementType == FishMovementType.DART) {
            fishY += fishDirection * 400 * delta;
        } else {
            fishY += fishDirection * 200 * delta;
        }

        fishY = MathUtils.clamp(fishY, 100, 700); // keep in range
    }


    public enum FishMovementType {
        MIXED, SMOOTH, SINKER, FLOATER, DART
    }

    public void applyPerfectFish() {
        if (fishQuality.equals(Quality.SILVER)) {
            fishQuality = Quality.GOLDEN;
        } else if (fishQuality.equals(Quality.GOLDEN)) {
            fishQuality = Quality.IRIDIUM;
        }
        Ability fishing = App.getCurrentPlayerLazy().getAbilityByName("fishing");
        fishing.increaseAmount((int) (1.4 * fishing.getAmount()));
    }

    public void endOneFish() {
        if (fishingOver) {
            return; // Avoid double processing if the fishing is already over
        }

        fishingOver = true; // ⬅️ Important to stop the game loop and avoid repeated dialogs

        Dialog dialog = new Dialog("Fishing Finished", skin);

        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setVisible(false);

        TextButton exitButton = new TextButton("Exit", skin);
        TextButton nextFishButton = new TextButton("Next Fish", skin);
        Label messageLabel = new Label(message, skin);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new GameMenu(players));
            }
        });

        nextFishButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (numberOfFishes > 0) {
                    dialog.hide();
                    restartFishing();
                } else {
                    Main.getMain().setScreen(new GameMenu(players));
                }
            }
        });

        Table content = dialog.getContentTable();
        content.add(messageLabel).pad(10).width(400f).row();
        content.add(nextFishButton).colspan(2).pad(5).width(400f).row();
        content.add(exitButton).colspan(2).pad(5).width(400f).row();
        content.add(errorLabel).colspan(2).pad(10).width(600f).row();

        dialog.button(exitButton);
        dialog.button(nextFishButton);
        dialog.setModal(true);
        dialog.setMovable(false);
        dialog.pack();
        dialog.show(fishingStage);

        // After the dialog is dismissed, set input processor again to allow 'Q' to work
        fishingStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.Q) {
                    System.out.println("Exiting fishing screen...");
                    Main.getMain().setScreen(new GameMenu(players));
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });
    }


    public void restartFishing() {
        fishDetail = fishDetails.get(numberOfFishes - 1);
        fishTexture = fishDetail.getTexture();
        progressTimer = 0.0f;
        progress = 4;
        fishY = 350;
        fishStateTimer = 0;
        fishDirection = 0; // -1 = down, 0 = still, 1 = up
        perfectFish = true;
        fishingOver = false;
        int rand = MathUtils.random(0, 5);
        switch (rand) {
            case 0:
                movementType = FishMovementType.MIXED;
                break;
            case 1:
                movementType = FishMovementType.DART;
                break;
            case 2:
                movementType = FishMovementType.FLOATER;
                break;
            case 3:
                movementType = FishMovementType.SINKER;
                break;
            case 4:
                movementType = FishMovementType.SMOOTH;
                break;
        }
        System.out.println(movementType.name());
    }

}

