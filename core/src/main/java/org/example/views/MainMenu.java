package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.models.Assets.GameAssetManager;

public class MainMenu implements Screen {
    private Skin skin = GameAssetManager.skin;
    private Image backgroundImage;
    private Stage stage;
    private final Label menuLabel;
    private final TextButton LogOut;
    private final TextButton exitButton;
    private final TextButton profileButton;
    private final TextButton preGameButton;
    public Table table = new Table();

    public MainMenu() {
        this.menuLabel = new Label("main menu", skin);
        menuLabel.setFontScale(20f);
        this.LogOut = new TextButton("log out", skin);
        this.exitButton = new TextButton("Exit", skin);
        this.profileButton = new TextButton("Profile Menu", skin);
        this.preGameButton = new TextButton("Pre-Game Menu", skin);
        setScale();
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
        table.add(menuLabel).colspan(2).pad(10);

        table.row().pad(50, 0, 40, 0);
        table.add(profileButton).width(200).height(50);
        table.row().pad(40, 0, 40, 0);
        table.add(preGameButton).width(200).height(50);
        table.row().pad(40, 0, 40, 0);
        table.add(LogOut).width(200).height(50);
        table.row().pad(40, 0, 40, 0);
        table.add(exitButton).width(200).height(50);

        stage.addActor(backgroundImage);
        stage.addActor(table);

        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new ProfileMenu());
                System.out.println("Profile Menu selected");
            }
        });

        preGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new PreGameMenu());
                System.out.println("Pre-Game Menu selected");
            }
        });

        LogOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("now you are logged out");
                // Handle logout logic
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new RegisterMenuView());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 40f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
    }

    public void setScale() {
        menuLabel.setFontScale(2f);
        exitButton.getLabel().setFontScale(2f);
        LogOut.getLabel().setFontScale(2f);
        profileButton.getLabel().setFontScale(2f);
        preGameButton.getLabel().setFontScale(2f);
    }
}
