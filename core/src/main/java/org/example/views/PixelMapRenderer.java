package org.example.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.Fundementals.Location;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.map;

public class PixelMapRenderer {
    private map gameMap;
    private Texture mapTexture;
    private final int tileSize = 100; // Tile size 100x100

    // Textures
    public static Texture PLANTS;
    public static Texture DARK_GREEN_FLOOR;
    public static Texture LAKE_TEXTURE;
    public static Texture FLOORING_03;
    public static Texture FLOORING_09;
    public static Texture FLOORING_10;
    public static Texture STONE;
    public static Texture NPC_VILLAGE;
    public static Texture GREEN_HOUSE;
    public static Texture QUARRY;

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
        DARK_GREEN_FLOOR = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_02.png");
        FLOORING_03 = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_04.png");
        FLOORING_09 = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_05.png");
        FLOORING_10 = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_06.png");
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
                return LAKE_TEXTURE;  // Texture for LAKE
            case GROUND:
                return FLOORING_03;  // Texture for GROUND
            case TREE:
                return FLOORING_09;  // Texture for TREE
            case HOUSE:
                return FLOORING_10;  // Texture for HOUSE
            case GREENHOUSE:
                return GREEN_HOUSE;  // Texture for GREENHOUSE
            case QUARRY:
                return QUARRY;  // Texture for QUARRY
            case STORE:
                return DARK_GREEN_FLOOR;  // Texture for STORE
            case NPC_VILLAGE:
                return NPC_VILLAGE;  // Texture for NPC_VILLAGE
            case BURNED_GROUND:
                return FLOORING_03;  // Texture for BURNED_GROUND
            case STONE:
                return STONE;  // Texture for STONE
            case PLANT:
                return PLANTS;  // Texture for PLANT
            default:
                return LAKE_TEXTURE;  // Default texture
        }
    }

    public void render(SpriteBatch batch, int x, int y) {
        batch.begin();

        // Loop through the map and render each tile's texture
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
        NPC_VILLAGE.dispose();
        GREEN_HOUSE.dispose();
        QUARRY.dispose();
        DARK_GREEN_FLOOR.dispose();
        FLOORING_03.dispose();
        FLOORING_09.dispose();
        FLOORING_10.dispose();
    }
}
