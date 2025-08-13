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
import org.example.models.Fundementals.App;
import org.example.models.RelatedToUser.User;

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

        User currentUser = App.getLoggedInUser();
        String avatarPath = "default_avatar.png";
        String nickname = "Guest";

        if (currentUser != null) {
            if (currentUser.getAvatarPath() != null) {
                avatarPath = currentUser.getAvatarPath();
            }
            if (currentUser.getNickname() != null) {
                nickname = currentUser.getNickname();
            }
        }

        Texture avatarTexture = new Texture(Gdx.files.internal(avatarPath));
        Image avatarImage = new Image(avatarTexture);
        avatarImage.setSize(128, 128);

        Label nicknameLabel = new Label("Nickname: " + nickname, skin);
        nicknameLabel.setFontScale(2f);

        table.setFillParent(true);
        table.top().padTop(30);
        menuLabel.setFontScale(4f);
        menuLabel.setColor(1, 0.84f, 0, 1);
        table.add(menuLabel).padBottom(20).row();

        table.add(avatarImage).size(128, 128).padBottom(10).row();
        table.add(nicknameLabel).padBottom(40).row();

        table.add(profileButton).width(200).height(50).padBottom(20).row();
        table.add(preGameButton).width(200).height(50).padBottom(20).row();
        table.add(LogOut).width(200).height(50).padBottom(20).row();
        table.add(exitButton).width(200).height(50).padBottom(20).row();

        stage.addActor(backgroundImage);
        stage.addActor(table);

        profileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new ProfileMenu());
            }
        });

        preGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new PreGameMenu());
            }
        });

        LogOut.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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
