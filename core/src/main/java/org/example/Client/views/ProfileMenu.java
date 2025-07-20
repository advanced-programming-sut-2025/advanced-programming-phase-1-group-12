package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Client.Main;
import org.example.Server.controllers.MenusController.ProfileMenuController;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Assets.GameAssetManager;
public class ProfileMenu implements Screen {

    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private final Label menuLabel;
    private final TextField newPass;
    private final TextField newEmail;
    private final TextField newUserName;
    private final TextField newNickName;
    private final TextButton saveChanges;
    private final TextButton backButton;
    private final TextButton changeAvatarButton;
    private final Label errorLabel;

    private Texture avatarTexture;
    private Image avatarImage;

    private ProfileMenuController controller = new ProfileMenuController();

    public ProfileMenu() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.menuLabel = new Label("Profile Menu", skin);
        menuLabel.setFontScale(4f);
        this.newUserName = new TextField("", skin);
        newUserName.setMessageText(" Enter new username");
        this.newPass = new TextField("", skin);
        newPass.setMessageText(" Enter new password");
        this.newNickName = new TextField("", skin);
        newNickName.setMessageText(" Enter new nickname");
        this.newEmail = new TextField("", skin);
        newEmail.setMessageText(" Enter new email");
        this.saveChanges = new TextButton("Save Changes", skin);
        this.backButton = new TextButton("Back", skin);
        this.changeAvatarButton = new TextButton("Change Avatar", skin);
        this.errorLabel = new Label("", skin);
        this.errorLabel.setColor(1, 0, 0, 1);
        this.errorLabel.setVisible(false);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture backgroundTexture = new Texture(Gdx.files.internal("menu_background.png"));
        Image background = new Image(backgroundTexture);
        background.setFillParent(true);
        stage.addActor(background);

        String avatarPath = App.getLoggedInUser().getAvatarPath();
        avatarTexture = new Texture(Gdx.files.internal(avatarPath));
        avatarImage = new Image(avatarTexture);

        String currentUsername = App.getLoggedInUser().getUserName();
        String currentEmail = App.getLoggedInUser().getEmail();
        String currentNickName = App.getLoggedInUser().getNickname();
        String gender = App.getLoggedInUser().isFemale() ? "female" : "male";

        Table infoTable = new Table();
        infoTable.top().left().pad(20);
        infoTable.add(new Label("User Info", skin)).left().padBottom(20).row();
        infoTable.add(new Label("Username: " + currentUsername, skin)).left().row();
        infoTable.add(new Label("Email: " + currentEmail, skin)).left().row();
        infoTable.add(new Label("Nickname: " + currentNickName, skin)).left().row();
        infoTable.add(new Label("Gender: " + gender, skin)).left().row();
        infoTable.add(avatarImage).padTop(20).padBottom(20).row();

        Table inputTable = new Table();
        inputTable.top().pad(20);
        inputTable.add(menuLabel).padBottom(40).row();
        inputTable.add(newPass).width(300).padBottom(20).row();
        inputTable.add(newEmail).width(300).padBottom(20).row();
        inputTable.add(newNickName).width(300).padBottom(20).row();
        inputTable.add(newUserName).width(300).padBottom(20).row();
        inputTable.add(changeAvatarButton).width(300).padBottom(20).row();
        inputTable.add(saveChanges).width(300).padBottom(20).row();
        inputTable.add(backButton).width(300).padBottom(20).row();
        inputTable.add(errorLabel).width(300).padBottom(20).row();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.left().top().pad(30);

        rootTable.add(infoTable).left().padRight(50);
        rootTable.add(inputTable);

        stage.addActor(rootTable);

        saveChanges.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showError(controller.saveChanges(newUserName.getText(), newPass.getText(), newNickName.getText(), newEmail.getText()));
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new MainMenu());
            }
        });

        changeAvatarButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAvatarSelectionDialog();
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
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
        if (avatarTexture != null) {
            avatarTexture.dispose();
        }
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showAvatarSelectionDialog() {
        Dialog dialog = new Dialog("Choose Avatar", skin);
        Table avatarTable = new Table();

        String[] imagePaths = {
            "portrait/alex.png", "portrait/elliot.png", "portrait/haley.png", "portrait/jas.png",
            "portrait/jodi.png", "portrait/kent.png", "portrait/Leah.png", "portrait/marnie.png",
            "portrait/penny.png", "portrait/pierre.png", "portrait/robin.png", "portrait/sam.png"
        };

        String gender = "male";
        if (App.getLoggedInUser().isFemale()) {
            gender = "female";
        }

        if (gender.equals("male")) {
            imagePaths = new String[]{"portrait/sam.png", "portrait/alex.png", "portrait/kent.png",
                "portrait/pierre.png", "portrait/robin.png", "portrait/elliot.png"};
        } else if (gender.equals("female")) {
            imagePaths = new String[]{"portrait/haley.png", "portrait/jas.png", "portrait/jodi.png",
                "portrait/Leah.png", "portrait/marnie.png", "portrait/penny.png"};
        }

        for (String path : imagePaths) {
            final String selectedPath = path;
            final Texture texture = new Texture(Gdx.files.internal(path));
            final Image avatar = new Image(texture);
            avatar.setSize(64, 64);

            avatar.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    controller.changeAvatar(selectedPath);
                    controller.showInfo("Avatar changed to " + selectedPath, stage, skin);
                    avatarTexture.dispose();
                    avatarTexture = new Texture(Gdx.files.internal(selectedPath));
                    avatarImage.setDrawable(new Image(avatarTexture).getDrawable());
                    dialog.hide();
                    texture.dispose();
                }
            });

            avatarTable.add(avatar).pad(5);
        }

        dialog.getContentTable().add(avatarTable).pad(10);
        dialog.button("Cancel", false);
        dialog.show(stage);
    }
}
