package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    public static Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));


    public Skin getSkin() {
        return skin;
    }
}
