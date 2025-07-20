package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import org.example.Common.models.Assets.GameAssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class TerminalWindow extends Window {

    private final GameConsoleCommandHandler commandHandler;

    private final Label outputLabel;
    private final TextField inputField;
    private final ScrollPane outputPane;

    public TerminalWindow(GameConsoleCommandHandler commandHandler) {
        super("Terminal", GameAssetManager.skin);
        this.commandHandler = commandHandler;

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
                if (keycode == Keys.GRAVE) {
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
        String result = captureOutput(() -> commandHandler.handle(line));

        if (!result.isBlank()) System.out.println(result);
        outputLabel.setText(outputLabel.getText() + "\n> " + line + "\n" + result.trim() + "\n");

        inputField.setText("");
        outputPane.layout();
        outputPane.setScrollPercentY(1f);
    }

    private String captureOutput(Runnable runnable) {
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

        try {
            runnable.run();
        } finally {
            System.out.flush();
            System.setOut(oldOut);
        }
        return capture.toString().trim();
    }
}
