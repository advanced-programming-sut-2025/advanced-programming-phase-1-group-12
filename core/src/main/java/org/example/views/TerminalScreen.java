package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.MenusController.GameMenuController;
import org.example.models.Assets.GameAssetManager;
import org.example.models.enums.commands.GameMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class TerminalScreen extends InputAdapter implements Screen {

    /**
     * the game or menu screen to return to
     */
    private final Screen backScreen;
    private final GameMenuController controller;

    private Stage stage;
    private Skin skin = GameAssetManager.skin;
    private TextArea output;
    private TextField input;

    public TerminalScreen(Screen backScreen, GameMenuController controller) {
        this.backScreen = backScreen;
        this.controller = controller;
    }

    /* ────────────────────── Screen lifecycle ────────────────────── */

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());

        /* 1️⃣  dim the background slightly */
        Table dim = new Table();
        dim.setFillParent(true);
        dim.setColor(0, 0, 0, 0.55f);          // 55 % black
        dim.setTouchable(Touchable.disabled);  // mouse clicks fall through
        stage.addActor(dim);

        /* 2️⃣  build the console window */
        Window console = new Window("Console", skin);
        console.setMovable(false);
        console.pad(10);

        output = new TextArea("", skin);
        output.setDisabled(true);
        output.setPrefRows(15);

        input = new TextField("", skin);
        input.setMessageText("type command …");
        input.setTextFieldListener((f, c) -> {
            if (c == '\n' || c == '\r') {              // Enter pressed
                String cmd = f.getText().trim();
                f.setText("");
                handleCommand(cmd);
            }
        });

        console.add(output).grow().row();
        console.add(input).growX();

        float w = Gdx.graphics.getWidth() * 0.60f;
        float h = Gdx.graphics.getHeight() * 0.40f;
        console.setSize(w, h);
        console.setPosition((Gdx.graphics.getWidth() - w) / 2f, (Gdx.graphics.getHeight() - h) / 2f);

        stage.addActor(console);
        stage.setKeyboardFocus(input);
        input.selectAll();

        InputMultiplexer mux = new InputMultiplexer(this, stage);
        Gdx.input.setInputProcessor(mux);
    }

    @Override
    public void render(float delta) {
        backScreen.render(delta);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.GRAVE) {
            Main.getMain().setScreen(backScreen);
            return true;
        }
        return false;
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
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
        stage.dispose();
    }

    /* ────────────────────── command handling ────────────────────── */

    private void handleCommand(String line) {
        Scanner scanner = new Scanner(line);
        String in = scanner.nextLine().trim();
        Matcher m;

        if ((m = GameMenuCommands.LoadGame.getMather(in)) != null) {
            controller.loadGameById(Integer.parseInt(m.group("gameID")));
            append(in, "loaded game " + m.group("gameID"));

        } else if ((m = GameMenuCommands.PRINT.getMather(in)) != null) {
            controller.printMap(
                Integer.parseInt(m.group("X")),
                Integer.parseInt(m.group("Y")),
                Integer.parseInt(m.group("size")),
                scanner);
            append(in, "(map printed to stdout)");
        } else if ((m = GameMenuCommands.TIME.getMather(in)) != null) {
            append(in, showCurrentTime());

        } else if ((m = GameMenuCommands.HELP.getMather(in)) != null) {
            controller.helpToReadMap();
            append(in, "(help printed to stdout)");

        } else if ((m = GameMenuCommands.SHOW_CURRENT_TYPE.getMather(in)) != null) {
            // add your logic here
            append(in, "current tile type is …");

        } else {
            append(in, "unknown command – try 'help'");
        }
    }

    private void append(String cmd, String result) {
        output.appendText("> " + cmd + '\n');
        output.appendText(result + "\n\n");
        output.setCursorPosition(output.getText().length());
    }

    /* you already have this somewhere in your project – just call it */
    private String showCurrentTime() {
        return java.time.LocalTime.now().toString();
    }
}
