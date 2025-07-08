//package org.example.views;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Pixmap;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import org.example.models.Fundementals.Location;
//import org.example.models.enums.Types.TypeOfTile;
//import org.example.models.map;
//
//public class PixelMapRenderer {
//    private map gameMap;
//    private Texture mapTexture;
//    private Pixmap pixmap;
//    private final int tileSize = 2; // هر پیکسل 2x2 خواهد بود
//
//    public PixelMapRenderer(map gameMap) {
//        this.gameMap = gameMap;
//        generatePixmap();
//    }
//
//    private void generatePixmap() {
//        int width = 400 * tileSize;
//        int height = 400 * tileSize;
//
//        pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
//
//        for (Location location : gameMap.getTilesOfMap()) {
//            Color color = getColorForTile(location.getTypeOfTile());
//            pixmap.setColor(color);
//            pixmap.fillRectangle(location.getxAxis() * tileSize,
//                (399 - location.getyAxis()) * tileSize,
//                tileSize,
//                tileSize);
//        }
//
//        mapTexture = new Texture(pixmap);
//        pixmap.dispose(); // حافظه پاکسازی شود
//    }
//
//    private Color getColorForTile(TypeOfTile tileType) {
//        switch (tileType) {
//            case LAKE:
//                return Color.BLUE;
//            case GROUND:
//                return Color.BROWN;
//            case TREE:
//                return Color.FOREST;
//            case HOUSE:
//                return Color.LIGHT_GRAY;
//            case GREENHOUSE:
//                return Color.GREEN;
//            case QUARRY:
//                return Color.YELLOW;
//            case STORE:
//                return Color.CYAN;
//            case NPC_VILLAGE:
//                return Color.PINK;
//            case BURNED_GROUND:
//                return Color.BLACK;
//            case STONE:
//                return Color.DARK_GRAY;
//            case PLANT:
//                return Color.LIME;
//            default:
//                return Color.WHITE;
//        }
//    }
//
//    public void render(SpriteBatch batch, int x, int y) {
//        batch.begin();
//        batch.draw(mapTexture, x, y);
//        batch.end();
//    }
//
//    public void dispose() {
//        mapTexture.dispose();
//    }
//}
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
    public static Texture LIGHT_GREEN_FLOOR;
    public static Texture DARK_GREEN_FLOOR;
    public static Texture FLOORING_01;
    public static Texture FLOORING_03;
    public static Texture FLOORING_09;
    public static Texture FLOORING_10;
    public static Texture FLOORING_17;

    public PixelMapRenderer(map gameMap) {
        this.gameMap = gameMap;
        loadTextures(); // Initialize all the textures
        generatePixmap();
    }

    private void loadTextures() {
        // Load the textures from assets (replace with your actual texture paths)
        LIGHT_GREEN_FLOOR = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_01.png");
        DARK_GREEN_FLOOR = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_02.png");
        FLOORING_01 = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_03.png");
        FLOORING_03 = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_04.png");
        FLOORING_09 = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_05.png");
        FLOORING_10 = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_06.png");
        FLOORING_17 = new Texture("Stardew_Valley_Images-main/Flooring/Flooring_07.png");
    }

    private void generatePixmap() {
        int width = 400 * tileSize;
        int height = 400 * tileSize;

        // Loop through the tiles of the map
        for (Location location : gameMap.getTilesOfMap()) {
            Texture texture = getTextureForTile(location.getTypeOfTile());

            // Place the texture for the tile on the map
            // Here you would add logic to handle drawing it
            // You can update your method to work with these textures
        }
    }

    private Texture getTextureForTile(TypeOfTile tileType) {
        switch (tileType) {
            case LAKE:
                return FLOORING_01;  // Texture for LAKE
            case GROUND:
                return FLOORING_03;  // Texture for GROUND
            case TREE:
                return FLOORING_09;  // Texture for TREE
            case HOUSE:
                return FLOORING_10;  // Texture for HOUSE
            case GREENHOUSE:
                return LIGHT_GREEN_FLOOR;  // Texture for GREENHOUSE
            case QUARRY:
                return DARK_GREEN_FLOOR;  // Texture for QUARRY
            case STORE:
                return FLOORING_17;  // Texture for STORE
            case NPC_VILLAGE:
                return LIGHT_GREEN_FLOOR;  // Texture for NPC_VILLAGE
            case BURNED_GROUND:
                return FLOORING_03;  // Texture for BURNED_GROUND
            case STONE:
                return DARK_GREEN_FLOOR;  // Texture for STONE
            case PLANT:
                return LIGHT_GREEN_FLOOR;  // Texture for PLANT
            default:
                return FLOORING_01;  // Default texture
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
        // Dispose the textures to free memory
        LIGHT_GREEN_FLOOR.dispose();
        DARK_GREEN_FLOOR.dispose();
        FLOORING_01.dispose();
        FLOORING_03.dispose();
        FLOORING_09.dispose();
        FLOORING_10.dispose();
        FLOORING_17.dispose();
    }
}
