package org.example.models.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.enums.Types.TreeType;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.enums.foraging.Tree;

import java.util.ArrayList;
import java.util.List;

public class GameAssetManager {
    public static Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    private static GameAssetManager gameAssetManager;

    private final String character1_Emojis0 = "Emoji/Emojis000.png";
    private final String character1_Emojis1 = "Emoji/Emojis001.png";
    private final String character1_Emojis2 = "Emoji/Emojis002.png";
    private final String character1_Emojis3 = "Emoji/Emojis003.png";
    private final String character1_Emojis4 = "Emoji/Emojis004.png";
    private final String character1_Emojis5 = "Emoji/Emojis005.png";
    private final Texture character1_Emojis0_tex = new Texture(character1_Emojis0);
    private final Texture character1_Emojis1_tex = new Texture(character1_Emojis1);
    private final Texture character1_Emojis2_tex = new Texture(character1_Emojis2);
    private final Texture character1_Emojis3_tex = new Texture(character1_Emojis3);
    private final Texture character1_Emojis4_tex = new Texture(character1_Emojis4);
    private final Texture character1_Emojis5_tex = new Texture(character1_Emojis5);
    private final Animation<Texture> character1_Emojis_frames = new Animation<>(0.1f, character1_Emojis0_tex,
        character1_Emojis1_tex, character1_Emojis2_tex, character1_Emojis3_tex, character1_Emojis4_tex, character1_Emojis5_tex);

    //plants
    private final Texture LAKE_TEXTURE = new Texture("Flooring/Flooring_26.png");
    private final Texture PLANTS = new Texture("Flooring/Flooring_50.png");
    private final Texture STONE = new Texture("Rock/Farm_Boulder.png");
    private final Texture NPC_VILLAGE = new Texture("Flooring/Flooring_83.png");
    private final Texture GREEN_HOUSE = new Texture("Greenhouse/greenhouse.png");
    private final Texture QUARRY = new Texture("Flooring/Flooring_08.png");
    private final Texture BURNED_GROUND = new Texture("Flooring/Flooring_39.png");
    private final Texture GROUND = new Texture("Flooring/Flooring_53.png");
    private final Texture STORE = new Texture("Flooring/Flooring_02.png");
    private final Texture TREE = new Texture("Flooring/Flooring_29.png");
    private final Texture DARK_GREEN_FLOOR = new Texture("Flooring/Flooring_02.png");
    private final Texture HOUSE = new Texture("House_farmer.png");

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }

    public static void dispose() {
        skin.dispose();
    }

    public Animation<Texture> getCharacter1_Emojis_animation() {
        return character1_Emojis_frames;
    }

    public Texture getLAKE_TEXTURE() {
        return LAKE_TEXTURE;
    }

    public Texture getPLANTS() {
        return PLANTS;
    }

    public Texture getSTONE() {
        return STONE;
    }

    public Texture getNPC_VILLAGE() {
        return NPC_VILLAGE;
    }

    public Texture getGREEN_HOUSE() {
        return GREEN_HOUSE;
    }

    public Texture getQUARRY() {
        return QUARRY;
    }

    public Texture getBURNED_GROUND() {
        return BURNED_GROUND;
    }

    public Texture getGROUND() {
        return GROUND;
    }

    public Texture getSTORE() {
        return STORE;
    }

    public Texture getTREE() {
        return TREE;
    }

    public Texture getDARK_GREEN_FLOOR() {
        return DARK_GREEN_FLOOR;
    }

    public Texture getHOUSE() {
        return HOUSE;
    }

    public static Texture treeType() {
        List<Tree> trees = new ArrayList<>();
        for(Location location: App.getCurrentGame().getMainMap().getTilesOfMap()) {
            if(location.getTypeOfTile() == TypeOfTile.TREE) {
                Tree tree = (Tree)location.getObjectInTile();
                trees.add(tree);
            }
        }
        for (Tree tree : trees) {
            TreeType type = tree.getType();
            switch (type) {
                case APRICOT_TREE:
                    return new Texture("Trees/Apricot_Stage_5_Fruit.png");
                case APPLE_TREE:
                    return new Texture("Trees/Apple_Stage_5_Fruit.png");
                case CHERRY_TREE:
                    return new Texture("Trees/Cherry_Stage_5_Fruit.png");
                case OAK_TREE:
                    return new Texture("Trees/Oak_Stage_1.png");
                case MAPLE_TREE:
                    return new Texture("Trees/Maple_Stage_4.png");
                case PINE_TREE:
                    return new Texture("Trees/Pine_Stage_4.png");
                case MUSHROOM_TREE:
                    return new Texture("Trees/MushroomTree_Stage_5.png");
                case PEACH_TREE:
                    return new Texture("Trees/Peach_Stage_5_Fruit.png");
                case MYSTIC_TREE:
                    return new Texture("Trees/Mystic_Tree_Stage_5.png");
                case ORANGE_TREE:
                    return new Texture("Trees/Orange_Stage_5_Fruit.png");
                case MANGO_TREE:
                    return new Texture("Trees/Mango_Stage_5_Fruit.png");
                case BANANA_TREE:
                    return new Texture("Trees/Banana_Stage_5_Fruit.png");
            }
        }
        return null;
    }

}
