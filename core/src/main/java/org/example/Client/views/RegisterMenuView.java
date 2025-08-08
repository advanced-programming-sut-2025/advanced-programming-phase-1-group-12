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
import org.example.Client.controllers.MenusController.LoginRegisterMenuController;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Assets.GameAssetManager;

public class RegisterMenuView implements Screen {

    private Skin skin = GameAssetManager.getSkin();
    private Image backgroundImage;
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
    private final TextButton chooseAvatarButton;
    private String selectedAvatar = "";
    public Table table = new Table();
    LoginRegisterMenuController controller = new LoginRegisterMenuController();
    private int question = 0;

    public RegisterMenuView() {

        Array<String> questions = new Array<>(new String[]{
            "what is your favorite color?", "what is your favorite country?"
        });
        selectSecurityQuestion.setItems(questions);

        this.menuLabel = new Label("register menu", skin);
        this.goToLogin = new TextButton(" go to login menu", skin);
        this.exitButton = new TextButton("Exit", skin);
        this.registerButton = new TextButton("Register", skin);
        this.usernameField = new TextField("", skin);
        usernameField.setMessageText(" Enter username");
        this.randomPasswordButton = new TextButton("generate Random password", skin);
        this.passwordField = new TextField("", skin);
        passwordField.setMessageText(" Enter password");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        this.passwordConfirmField = new TextField("", skin);
        passwordConfirmField.setMessageText(" Confirm password");
        passwordConfirmField.setPasswordMode(true);
        passwordConfirmField.setPasswordCharacter('*');
        this.answerSecurityQuestionField = new TextField("", skin);
        answerSecurityQuestionField.setMessageText(" Answer security question");
        this.confirmSecurityQuestionField = new TextField("", skin);
        confirmSecurityQuestionField.setMessageText(" Confirm answer");
        this.nickNameField = new TextField("", skin);
        nickNameField.setMessageText(" Enter nickname");
        this.emailField = new TextField("", skin);
        emailField.setMessageText(" Enter email");
        this.genderField = new TextField("", skin);
        genderField.setMessageText(" Enter gender");
        this.errorLabel = new Label("", skin);
        this.errorLabel.setColor(1, 0, 0, 1);
        this.errorLabel.setVisible(false);
        this.chooseAvatarButton = new TextButton("Choose Avatar", skin);
        setScale();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Texture backgroundTexture = new Texture(Gdx.files.internal("menu_background.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float width = 400f;
        float height = 40f;
        table.setFillParent(true);
        table.center();

        table.row().colspan(2).pad(40, 0, 0, 0);
        table.add(menuLabel).colspan(2).center();

        table.row().pad(10, 0, 10, 0);
        table.add(goToLogin).colspan(2).center().width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(usernameField).colspan(2).width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(randomPasswordButton).colspan(2).center().width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(passwordField).width(width).height(height);
        table.add(passwordConfirmField).width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(selectSecurityQuestion).colspan(2).center().width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(answerSecurityQuestionField).width(width).height(height);
        table.add(confirmSecurityQuestionField).width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(nickNameField).width(width).height(height);
        table.add(emailField).width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(genderField).width(width).height(height);
        table.add(chooseAvatarButton).width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(registerButton).colspan(2).width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(exitButton).colspan(2).width(width).height(height);

        table.row().pad(10, 0, 10, 0);
        table.add(errorLabel).colspan(2).width(width).expandX().center().fillX();

        stage.addActor(backgroundImage);
        stage.addActor(table);

        chooseAvatarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAvatarSelectionDialog();
            }
        });

        selectSecurityQuestion.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (selectSecurityQuestion.getSelected().equalsIgnoreCase("what is your favorite country?")) {
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
                    question, selectedAvatar
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

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

    public void setScale() {
        menuLabel.setFontScale(4f);
        goToLogin.getLabel().setFontScale(2f);
        exitButton.getLabel().setFontScale(2f);
        registerButton.getLabel().setFontScale(2f);
        randomPasswordButton.getLabel().setFontScale(2f);
        selectSecurityQuestion.getStyle().font.getData().setScale(2f);
        selectSecurityQuestion.getStyle().listStyle.font.getData().setScale(2f);
        errorLabel.setFontScale(2f);
    }

    private void showAvatarSelectionDialog() {
        Dialog dialog = new Dialog("Choose Avatar", skin);
        Table avatarTable = new Table();

        String[] imagePaths = {
            "portrait/alex.png", "portrait/elliot.png", "portrait/haley.png", "portrait/jas.png",
            "portrait/jodi.png", "portrait/kent.png", "portrait/Leah.png", "portrait/marnie.png",
            "portrait/penny.png", "portrait/pierre.png", "portrait/robin.png", "portrait/sam.png"
        };

        String gender = genderField.getText().toLowerCase();
        if (gender.equals("male")) {
            imagePaths = new String[]{"portrait/sam.png", "portrait/alex.png", "portrait/kent.png",
                "portrait/pierre.png", "portrait/robin.png", "portrait/elliot.png"};
        } else if (gender.equals("female")) {
            imagePaths = new String[]{"portrait/haley.png", "portrait/jas.png", "portrait/jodi.png",
                "portrait/Leah.png", "portrait/marnie.png", "portrait/penny.png"};
        }

        for (int i = 0; i < imagePaths.length; i++) {
            final String path = imagePaths[i];
            final Texture texture = new Texture(Gdx.files.internal(path));
            final Image avatar = new Image(texture);
            avatar.setSize(64, 64);

            avatar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    controller.changeAvatar(path);
                    controller.showInfo("Avatar changed to " + path, stage, skin);
                    selectedAvatar = path;
                    dialog.hide();
                    texture.dispose();
                }
            });

            avatarTable.add(avatar).pad(5);
            if ((i + 1) % 4 == 0) avatarTable.row();
        }

        dialog.getContentTable().add(avatarTable).pad(10);
        dialog.button("Cancel", false);
        dialog.show(stage);
    }
}
