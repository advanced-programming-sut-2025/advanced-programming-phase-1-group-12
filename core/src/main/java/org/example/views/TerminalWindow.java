package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import org.example.controllers.MenusController.GameMenuController;
import org.example.models.Assets.GameAssetManager;
import org.example.models.enums.commands.GameMenuCommands;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;

public class TerminalWindow extends Window {

    private final GameMenuController controller;

    private final Label outputLabel;
    private final TextField inputField;
    private final ScrollPane outputPane;

    public TerminalWindow(GameMenuController controller) {
        super("Terminal", GameAssetManager.skin);
        this.controller = controller;

        setModal(true);
        setMovable(false);
        setResizable(false);
        padTop(42);

        float w = Gdx.graphics.getWidth() * 0.60f;
        float h = Gdx.graphics.getHeight() * 0.50f;
        setSize(w, h);
        setPosition((Gdx.graphics.getWidth() - w) / 2f, (Gdx.graphics.getHeight() - h) / 2f);

        Table root = new Table();
        root.defaults().expandX().fillX().pad(10);
        add(root).expand().fill();

        outputLabel = new Label("", getSkin());
        outputLabel.setWrap(true);
        outputLabel.setAlignment(Align.topLeft);

        outputPane = new ScrollPane(outputLabel, getSkin());
        outputPane.setFadeScrollBars(false);
        outputPane.setScrollingDisabled(true, false);

        inputField = new TextField("", getSkin());
        inputField.setMessageText("Enter command…");

        TextButton executeBtn = new TextButton("Execute", getSkin());

        root.add(outputPane).height(h * 0.55f).expandY().fill().row();
        root.add(inputField).height(40).row();
        root.add(executeBtn).height(40).width(w * 0.25f).center();

        inputField.setTextFieldListener((f, c) -> {
            if (c == '\n' || c == '\r') executeCommand();
        });
        executeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                executeCommand();
            }
        });
        addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent e, int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.GRAVE) {
                    remove();
                    return true;
                }
                return false;
            }
        });
    }

    public void attach(Stage stage) {
        stage.addActor(this);
        stage.setKeyboardFocus(inputField);
    }

    private void executeCommand() {
        String line = inputField.getText().trim();
        if (line.isEmpty()) return;

        ByteArrayOutputStream capture = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;

        PrintStream tee = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                oldOut.write(b);
                capture.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                oldOut.write(b, off, len);
                capture.write(b, off, len);
            }
        }, true);

        System.setOut(tee);

        String result;
        try {
            result = processLine(line);
        } finally {
            System.out.flush();
            System.setOut(oldOut);
        }

        StringBuilder toShow = new StringBuilder();
        String std = capture.toString().trim();
        if (!std.isEmpty()) toShow.append(std);
        if (toShow.length() > 0 && result != null && !result.isEmpty()) toShow.append('\n');
        if (result != null && !result.isEmpty()) toShow.append(result);

        outputLabel.setText(outputLabel.getText() + "\n> " + line + "\n" + toShow + "\n");

        inputField.setText("");
        outputPane.layout();
        outputPane.setScrollPercentY(1f);
    }

    private String processLine(String line) {
        Scanner scanner = new Scanner(line);
        String in = scanner.nextLine().trim();
        Matcher m;
        StringBuilder sb = new StringBuilder();

        if ((m = GameMenuCommands.LoadGame.getMather(in)) != null) {
            controller.loadGameById(Integer.parseInt(m.group("gameID")));
            sb.append("loaded game ").append(m.group("gameID"));

        } else if ((m = GameMenuCommands.PRINT.getMather(in)) != null) {
            controller.printMap(
                Integer.parseInt(m.group("X")),
                Integer.parseInt(m.group("Y")),
                Integer.parseInt(m.group("size")),
                scanner);
            sb.append("(map printed)");

        } else if ((m = GameMenuCommands.TIME.getMather(in)) != null) {
            sb.append(java.time.LocalTime.now());

        } else if ((m = GameMenuCommands.HELP.getMather(in)) != null) {
            controller.helpToReadMap();
            sb.append("(help printed)");

        } else if ((m = GameMenuCommands.SHOW_CURRENT_TYPE.getMather(in)) != null) {
            sb.append("current tile type is …");

        } else sb.append("unknown command – try 'help'");
        return sb.toString();
    }
}
