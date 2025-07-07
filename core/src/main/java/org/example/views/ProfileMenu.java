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
import org.example.controllers.MenusController.ProfileMenuController;
import org.example.models.Fundementals.App;
import org.example.models.GameAssetManager;
import org.example.models.enums.Menu;

import java.io.File;
import java.util.Scanner;

public class ProfileMenu extends AppMenu implements Screen {

    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private final Label menuLabel;
    private final TextField newPass;
    private final TextField newEmail;
    private final TextField newUserName;
    private final TextField newNickName;
    private final TextButton saveChanges;
    private final TextButton backButton;
    public Table table = new Table();
    private final Label errorLabel;

    ProfileMenuController controller = new ProfileMenuController();

    public ProfileMenu() {
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.menuLabel = new Label("profile menu", skin);
        this.newUserName = new TextField("", skin);
        newUserName.setMessageText("Enter new username");
        this.newPass = new TextField("", skin);
        newPass.setMessageText("Enter new password");
        this.newNickName = new TextField("", skin);
        newNickName.setMessageText("Enter new nickname");
        this.newEmail = new TextField("", skin);
        newEmail.setMessageText("Enter new email");
        this.saveChanges = new TextButton("Save changes", skin);
        this.backButton = new TextButton("Back", skin);
        this.errorLabel = new Label("", skin); // ✅ NEW
        this.errorLabel.setColor(1, 0, 0, 1);   // Red color
        this.errorLabel.setVisible(false);

    }

    @Override
    public void check(Scanner scanner) {
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // === Get current user information ===
        String currentUsername = App.getLoggedInUser().getUserName();
        String currentEmail = App.getLoggedInUser().getEmail();
        String currentNickName = App.getLoggedInUser().getNickname();

        // === Info Table on the left ===
        Table infoTable = new Table();
        infoTable.top().left().pad(20);

        Label titleLabel = new Label("User Info", skin);
        Label usernameLabel = new Label("Username: " + currentUsername, skin);
        Label emailLabel = new Label("Email: " + currentEmail, skin);
        Label nicknameLabel = new Label("Nickname: " + currentNickName, skin);
        String gender = "male";
        if(App.getLoggedInUser().isFemale()){
            gender = "female";
        }
        Label genderLabel = new Label("Gender: " + gender, skin);
        infoTable.add(titleLabel).left().padBottom(20).row();
        infoTable.add(usernameLabel).left().row();
        infoTable.add(emailLabel).left().row();
        infoTable.add(nicknameLabel).left().row();
        infoTable.add(genderLabel).left().row();

        table = new Table();
        table.top().pad(20);
        table.add(menuLabel).padBottom(40).row();
        table.add(newPass).width(300).padBottom(20).row();
        table.add(newEmail).width(300).padBottom(20).row();
        table.add(newNickName).width(300).padBottom(20).row();
        table.add(newUserName).width(300).padBottom(20).row();
        table.add(saveChanges).width(300).padBottom(20).row();
        table.add(backButton).width(300).padBottom(20).row();
        table.add(errorLabel).width(300).padBottom(20).row();

        Table root = new Table();
        root.setFillParent(true);
        root.left().top().pad(30);

        root.add(infoTable).left().padRight(200);
        root.add(table);

        stage.addActor(root);

        saveChanges.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                showError(controller.saveChanges(newUserName.getText(), newPass.getText(), newNickName.getText(), newEmail.getText()));
            }
        });
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                App.setCurrentMenu(Menu.MainMenu);
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new MainMenu());
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
}
