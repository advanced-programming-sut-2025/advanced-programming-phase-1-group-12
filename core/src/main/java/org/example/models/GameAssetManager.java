package org.example.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameAssetManager {
    public static final AssetManager manager = new AssetManager();
    public static Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    private static GameAssetManager gameAssetManager;

    private final String character1_Emojis0 = "Stardew_Valley_Images-main/Emoji/Emojis000.png";
    private final String character1_Emojis1 = "Stardew_Valley_Images-main/Emoji/Emojis001.png";
    private final String character1_Emojis2 = "Stardew_Valley_Images-main/Emoji/Emojis002.png";
    private final String character1_Emojis3 = "Stardew_Valley_Images-main/Emoji/Emojis003.png";
    private final String character1_Emojis4 = "Stardew_Valley_Images-main/Emoji/Emojis004.png";
    private final String character1_Emojis5 = "Stardew_Valley_Images-main/Emoji/Emojis005.png";
    private final Texture character1_Emojis0_tex = new Texture(character1_Emojis0);
    private final Texture character1_Emojis1_tex = new Texture(character1_Emojis1);
    private final Texture character1_Emojis2_tex = new Texture(character1_Emojis2);
    private final Texture character1_Emojis3_tex = new Texture(character1_Emojis3);
    private final Texture character1_Emojis4_tex = new Texture(character1_Emojis4);
    private final Texture character1_Emojis5_tex = new Texture(character1_Emojis5);
    private final Animation<Texture> character1_Emojis_frames = new Animation<>(0.1f, character1_Emojis0_tex, character1_Emojis1_tex, character1_Emojis2_tex, character1_Emojis3_tex, character1_Emojis4_tex, character1_Emojis5_tex);

    private GameAssetManager() {

    }

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }

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

    public Animation<Texture> getCharacter1_Emojis_animation() {
        return character1_Emojis_frames;
    }

}
