package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.MenusController.LoginRegisterMenuController;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Result;
import org.example.models.Assets.GameAssetManager;

import java.util.Scanner;

public class RegisterMenuView implements Screen {

    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private final Label menuLabel;
    private final TextButton goToLogin;
    private final TextButton exitButton;
    private final TextButton registerButton;
    private final SelectBox<String> selectSecurityQuestion = new SelectBox<>(skin);
    private final TextField usernameField;
    private final TextField passwordField;
    private final TextButton randomPasswordButton;
    private final TextField passwordConfirmField;
    private final TextField answerSecurityQuestionField;
    private final TextField confirmSecurityQuestionField;
    private final TextField nickNameField;
    private final TextField emailField;
    private final TextField genderField;
    private final Label errorLabel;
    public Table table = new Table();
    LoginRegisterMenuController controller = new LoginRegisterMenuController();
    private int question = 0;

    public RegisterMenuView() {

        Array<String> questions = new Array<>(new String[]{
            "what is your favorite color?", "what is your favorite country?"
        });
        selectSecurityQuestion.setItems(questions);

        this.menuLabel = new Label("register menu", skin);
        this.goToLogin = new TextButton("go to login menu", skin);
        this.exitButton = new TextButton("Exit", skin);
        this.registerButton = new TextButton("Register", skin);
        this.usernameField = new TextField("", skin);
        usernameField.setMessageText("Enter username");
        this.randomPasswordButton = new TextButton("generate Random password", skin);
        this.passwordField = new TextField("", skin);
        passwordField.setMessageText("Enter password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        this.passwordConfirmField = new TextField("", skin);
        passwordConfirmField.setMessageText("Confirm password");
        passwordConfirmField.setPasswordMode(true);
        passwordConfirmField.setPasswordCharacter('*');
        this.answerSecurityQuestionField = new TextField("", skin);
        answerSecurityQuestionField.setMessageText("Answer security question");
        this.confirmSecurityQuestionField = new TextField("", skin);
        confirmSecurityQuestionField.setMessageText("Confirm answer");
        this.nickNameField = new TextField("", skin);
        nickNameField.setMessageText("Enter nickname");
        this.emailField = new TextField("", skin);
        emailField.setMessageText("Enter email");
        this.genderField = new TextField("", skin);
        genderField.setMessageText("Enter gender");
        this.errorLabel = new Label("", skin);
        this.errorLabel.setColor(1, 0, 0, 1);
        this.errorLabel.setVisible(false);
        setScale();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        float width = 400f;

        table.setFillParent(true);
        table.center();

        table.right().add(menuLabel);  // 👈 moved to the right

        table.row().pad(40, 0, 0, 0);
        table.add(goToLogin).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(selectSecurityQuestion).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(usernameField).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(randomPasswordButton).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(passwordField).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(passwordConfirmField).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(answerSecurityQuestionField).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(confirmSecurityQuestionField).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(nickNameField).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(emailField).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(genderField).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(registerButton).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(exitButton).width(width);
        table.row().pad(26, 0, 26, 0);
        table.add(errorLabel).width(width).expandX().center().fillX();

        stage.addActor(table);  // 👈 No ScrollPane now

        selectSecurityQuestion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectSecurityQuestion.getSelected()
                    .equalsIgnoreCase("what is your favorite country?")) {
                    question = 1;
                }
            }
        });

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Result result = controller.register(
                    usernameField.getText(),
                    passwordField.getText(),
                    passwordConfirmField.getText(),
                    nickNameField.getText(),
                    emailField.getText(),
                    genderField.getText(),
                    answerSecurityQuestionField.getText(),
                    confirmSecurityQuestionField.getText(),
                    question
                );
                if (result.isSuccessful()) {
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

        goToLogin.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new LoginMenuView());
            }
        });

        randomPasswordButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                showError(controller.RandomPassword());
            }
        });
    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 26f));
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
        errorLabel.invalidateHierarchy();
        table.invalidateHierarchy();
    }

    public void hideError() {
        errorLabel.setVisible(false);
    }

    public void setScale() {
        menuLabel.setFontScale(2f);
        goToLogin.getLabel().setFontScale(2f);
        exitButton.getLabel().setFontScale(2f);
        registerButton.getLabel().setFontScale(2f);
        randomPasswordButton.getLabel().setFontScale(2f);
        selectSecurityQuestion.getStyle().font.getData().setScale(2f);
        selectSecurityQuestion.getStyle().listStyle.font.getData().setScale(2f);
        errorLabel.setFontScale(2f);
    }
}
