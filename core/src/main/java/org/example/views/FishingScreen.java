package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.models.Assets.GameAssetManager;
import org.example.models.enums.FishDetails;

import java.util.List;

public class FishingScreen implements Screen {

    private Stage fishingStage;
    private SpriteBatch fishingBatch;
    private OrthographicCamera fishingCamera;

    private Texture waterTexture;
    private Texture fishTexture;
    private Texture bobberTexture;

    private float bobberYPosition;
    private boolean isReelingIn;
    private float reelProgress;
    private FishDetails fishDetails;
    private int fishY;
    List<String> players;
    ProgressBar progressBar;
    private float progressTimer = 0.0f;
    private int progress = 4;
    private FishMovementType movementType = FishMovementType.MIXED;
    private float fishYVelocity = 0;
    private float fishStateTimer = 0;
    private float timeSinceDirectionChange = 0;
    private int fishDirection = 0; // -1 = down, 0 = still, 1 = up


    public FishingScreen(FishDetails fishDetails, List<String> players) {
        this.fishDetails = fishDetails;
        this.fishTexture = fishDetails.getTexture();
        fishingStage = new Stage(new ScreenViewport());
        fishingBatch = new SpriteBatch();
        fishingCamera = new OrthographicCamera();
        fishingCamera.setToOrtho(false, 800, 600); // Adjust the screen size
        this.fishY = 350;
        this.players = players;
        ProgressBar.ProgressBarStyle style = GameAssetManager.skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class);
        progressBar = new ProgressBar(0, 1, 0.01f, false, style);
        initializeprogressBar(GameAssetManager.skin, fishingCamera);
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
        } System.out.println(movementType.name());
    }

    @Override
    public void show() {
        // Load textures
        waterTexture = GameAssetManager.getGameAssetManager().getLAKE_TEXTURE(); // Your water background texture
        //rodTexture = new Texture("fishing_rod.png");
        bobberTexture = new Texture("Flooring/Flooring_86.png");

        // Set the bobber starting position
        bobberYPosition = 500; // Adjust this based on your scene height
        isReelingIn = false;
        reelProgress = 0;
        fishingBatch = new SpriteBatch(); // Create new batch instead of using global one
        fishingStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(fishingStage);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        progressTimer += delta;
        if (progressTimer >= 0.5f) {
            float bobberTop = bobberYPosition + 50;
            float bobberBottom = bobberYPosition;

            if (fishY >= bobberBottom && fishY <= bobberTop) {
                progress += 4;
                progressTimer = 0;
            }
        }
        handleInput(delta);
        // Animate fish up and down
        updateFishMovement(delta);

        // Clamp bobber position before rendering
        bobberYPosition = Math.max(0, Math.min(bobberYPosition, 800 - 50));

        fishingBatch.setProjectionMatrix(fishingCamera.combined);
        fishingBatch.begin();

        fishingBatch.draw(waterTexture, 0, 0, 1000, 800);
        fishingBatch.draw(bobberTexture, 580, bobberYPosition, 15, 50);
        fishingBatch.draw(fishTexture, 380, fishY);

        if (isReelingIn) {
            reelProgress += delta * 0.5f;
            if (reelProgress >= 1) {
                reelProgress = 1;
                catchFish();
            }
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
        if (reelProgress >= 1) {
            // Display feedback on success
            System.out.println("You caught a fish!");
        } else {
            // Handle a failed catch (missed)
            System.out.println("You missed the fish.");
        }
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
        progressBar.setPosition(camRight - 260, camTop - 40); // Final position
        fishingStage.addActor(progressBar); // ✅ Add to stage!
    }


    private void drawprogressBar(OrthographicCamera cam) {
        float camRight = cam.position.x + cam.viewportWidth / 2;
        float camTop = cam.position.y + cam.viewportHeight / 2;

        float progress1 = (float) progress / 20;
        progress1 = Math.min(progress1, 1); // Ensure it does not exceed 100%

        progressBar.setValue(progress1); // Set the value of the XP progress bar

        progressBar.setPosition(camRight - 260, camTop - 40);

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

}

