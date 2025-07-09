package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    public static final AssetManager manager = new AssetManager();
    public static Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

    public static void loadFarmTextures() {
        for (int i = 0; i < 4; i++) {
            manager.load("farms/farm" + i + ".png", Texture.class);
        }
        manager.finishLoading();
    }

    public static void dispose() {
        manager.dispose();
        skin.dispose();
    }
}
