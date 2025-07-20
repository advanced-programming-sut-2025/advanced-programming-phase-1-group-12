package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Client.Main;
import org.example.Server.controllers.MenusController.LoginRegisterMenuController;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Assets.GameAssetManager;

public class LoginMenuView implements Screen {
    private Skin skin = GameAssetManager.getSkin();
    private Image backgroundImage;
    private Stage stage;
    private final Label menuLabel;
    private final TextButton exitButton;
    private final SelectBox<String> selectFlag = new SelectBox<>(skin);
    private final TextField usernameField;
    private final TextField passwordField;
    private final TextButton forgetPassword;
    private final Label errorLabel;
    private final TextButton loginButton;
    public Table table = new Table();
    LoginRegisterMenuController controller = new LoginRegisterMenuController();
    private boolean stayLoggedIn = false;

    public LoginMenuView() {

        Array<String> questions = new Array<>(new String[]{
             "do not stay logged in", "stay logged in"
        });

        selectFlag.setItems(questions);

        this.menuLabel = new Label("login menu", skin);
        this.exitButton = new TextButton("Exit", skin);
        this.usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");
        this.forgetPassword = new TextButton("Forget password", skin);
        this.passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        this.errorLabel = new Label("", skin); // ✅ NEW
        this.errorLabel.setColor(1, 0, 0, 1);   // Red color
        this.errorLabel.setVisible(false);
        this.loginButton = new TextButton("Login", skin);

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
        table.add(menuLabel);

        table.row().pad(20, 0, 20, 0);
        table.add(selectFlag).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(usernameField).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(passwordField).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(forgetPassword).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(exitButton).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(loginButton).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(errorLabel);
        stage.addActor(backgroundImage);
        stage.addActor(table);

        selectFlag.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if(selectFlag.getSelected()
                    .equalsIgnoreCase("stay logged in")){
                    stayLoggedIn = true;
                }
            }
        });

        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result;
                if (!stayLoggedIn) {
                     result = controller.login(usernameField.getText(), passwordField.getText());
                } else {
                    result = controller.loginWithFlag(usernameField.getText(), passwordField.getText());
                }

                if(result.isSuccessful()){
                    Main.getMain().getScreen().dispose();
                    Main.getMain().setScreen(new MainMenu());
                } else {
                    showError(result.getMessage());
                }

            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });
        forgetPassword.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new forgetPass());
            }
        });
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        if (stage != null) {
            stage.getViewport().update(i, i1, true);
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
    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    public void hideError() {
        errorLabel.setVisible(false);
    }

    public void setScale() {
        menuLabel.setFontScale(2f);
        exitButton.getLabel().setFontScale(2f);
        errorLabel.setFontScale(2f);
    }
}
class forgetPass implements Screen {
    private Skin skin = GameAssetManager.getSkin();
    private Image backgroundImage;
    private Stage stage;
    private final Label menuLabel;
    private final TextButton back;
    private final TextField usernameField;
    private final TextField answerOfSecurity;
    private final TextField newPasswordField;
    private final TextButton generateRandomPassword;
    private final Label errorLabel;
    private final TextButton showPassword;
    public Table table = new Table();
    LoginRegisterMenuController controller = new LoginRegisterMenuController();

    public forgetPass() {

        this.menuLabel = new Label("you have forgotten your password", skin);
        this.back = new TextButton("back to login", skin);
        this.usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");
        this.answerOfSecurity = new TextField("", skin);
        answerOfSecurity.setMessageText("Enter answer of security question");
        this.newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("Enter new password");
        this.generateRandomPassword = new TextButton("generate", skin);
        this.showPassword = new TextButton("show password", skin);
        this.errorLabel = new Label("", skin);
        this.errorLabel.setColor(1, 0, 0, 1);   // Red color
        this.errorLabel.setVisible(false);
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
        table.add(menuLabel);

        table.row().pad(20, 0, 20, 0);
        table.add(usernameField).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(answerOfSecurity).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(newPasswordField).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(generateRandomPassword).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(showPassword).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(back).width(300);
        table.row().pad(20, 0, 20, 0);
        table.add(errorLabel);
        stage.addActor(backgroundImage);
        stage.addActor(table);

        showPassword.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                showError(controller.forgetPassword(usernameField.getText(), answerOfSecurity.getText(), newPasswordField.getText()).getMessage());
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new LoginMenuView());
            }
        });

        generateRandomPassword.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                showError(controller.RandomPassword());
            }
        });
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {
        if (stage != null) {
            stage.getViewport().update(i, i1, true);
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
    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
    public void hideError() {
        errorLabel.setVisible(false);
    }
}
