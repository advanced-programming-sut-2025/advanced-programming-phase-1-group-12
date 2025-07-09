package org.example.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.Fundementals.Location;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.map;

public class PixelMapRenderer {
    private map gameMap;
    private final int tileSize = 100;

    public static Texture PLANTS;
    public static Texture DARK_GREEN_FLOOR;
    public static Texture LAKE_TEXTURE;
    public static Texture BURNED_GROUND;
    public static Texture HOUSE;
    public static Texture STONE;
    public static Texture NPC_VILLAGE;
    public static Texture GREEN_HOUSE;
    public static Texture QUARRY;
    public static Texture GROUND;
    public static Texture STORE;
    public static Texture TREE;

    public PixelMapRenderer(map gameMap) {
        this.gameMap = gameMap;
        loadTextures();
        generatePixmap();
    }

    private void loadTextures() {
        LAKE_TEXTURE = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_26.png");
        PLANTS = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_50.png");
        STONE = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_52.png");
        NPC_VILLAGE = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_83.png");
        GREEN_HOUSE = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_03.png");
        QUARRY = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_08.png");
        BURNED_GROUND = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_39.png");
        GROUND = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_53.png");
        STORE = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_02.png");
        TREE = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_29.png");
        DARK_GREEN_FLOOR = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_02.png");
        HOUSE = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_27.png");
    }

    private void generatePixmap() {
        int width = 400 * tileSize;
        int height = 400 * tileSize;

        for (Location location : gameMap.getTilesOfMap()) {
            Texture texture = getTextureForTile(location.getTypeOfTile());
        }
    }

    private Texture getTextureForTile(TypeOfTile tileType) {
        switch (tileType) {
            case LAKE:
                return LAKE_TEXTURE;
            case TREE:
                return TREE;
            case HOUSE:
                return HOUSE;
            case GREENHOUSE:
                return GREEN_HOUSE;
            case QUARRY:
                return QUARRY;
            case STORE:
                return STORE;
            case NPC_VILLAGE:
                return NPC_VILLAGE;
            case BURNED_GROUND:
                return BURNED_GROUND;
            case STONE:
                return STONE;
            case PLANT:
                return PLANTS;
            default:
                return GROUND;
        }
    }

    public void render(SpriteBatch batch, int x, int y) {
        batch.begin();

        for (Location location : gameMap.getTilesOfMap()) {
            Texture texture = getTextureForTile(location.getTypeOfTile());
            batch.draw(texture, x + location.getxAxis() * tileSize, y + (399 - location.getyAxis()) * tileSize, tileSize, tileSize);
        }

        batch.end();
    }

    public void dispose() {
        PLANTS.dispose();
        LAKE_TEXTURE.dispose();
        STONE.dispose();
        STORE.dispose();
        NPC_VILLAGE.dispose();
        GREEN_HOUSE.dispose();
        QUARRY.dispose();
        GROUND.dispose();
        BURNED_GROUND.dispose();
        DARK_GREEN_FLOOR.dispose();
        TREE.dispose();
        HOUSE.dispose();
    }
}
