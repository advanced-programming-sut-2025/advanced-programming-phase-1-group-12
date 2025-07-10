package org.example.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.Fundementals.Location;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PixelMapRenderer {

    private final int tileSize = 100;
    private static final int GH_TILES_W = 4;
    private static final int GH_TILES_H = 4;
    private final map gameMap;
    private final Set<String> greenhouseTiles = new HashSet<>();
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
        cacheGreenhouseTiles();
    }

    private void loadTextures() {
        LAKE_TEXTURE = new Texture("Flooring/Flooring_26.png");
        PLANTS = new Texture("Flooring/Flooring_50.png");
        STONE = new Texture("Flooring/Flooring_52.png");
        NPC_VILLAGE = new Texture("Flooring/Flooring_83.png");
        GREEN_HOUSE = new Texture("Greenhouse/greenhouse.png");
        QUARRY = new Texture("Flooring/Flooring_08.png");
        BURNED_GROUND = new Texture("Flooring/Flooring_39.png");
        GROUND = new Texture("Flooring/Flooring_53.png");
        STORE = new Texture("Flooring/Flooring_02.png");
        TREE = new Texture("Flooring/Flooring_29.png");
        DARK_GREEN_FLOOR = new Texture("Flooring/Flooring_02.png");
        HOUSE = new Texture("Flooring/Flooring_27.png");
    }

    private void cacheGreenhouseTiles() {
        for (Location l : gameMap.getTilesOfMap()) {
            if (l.getTypeOfTile() == TypeOfTile.GREENHOUSE) {
                greenhouseTiles.add(l.getxAxis() + "," + l.getyAxis());
            }
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

    public void render(SpriteBatch batch, int offsetX, int offsetY) {
        batch.end();
        batch.begin();

        List<Location> greenhouseAnchors = new ArrayList<>();

        for (Location loc : gameMap.getTilesOfMap()) {
            Texture base = getTextureForTile(loc.getTypeOfTile());
            batch.draw(base,
                offsetX + loc.getxAxis() * tileSize, offsetY + (399 - loc.getyAxis()) * tileSize,
                tileSize, tileSize);

            if (loc.getTypeOfTile() == TypeOfTile.GREENHOUSE) {
                boolean hasLeft = greenhouseTiles.contains((loc.getxAxis() - 1) + "," + loc.getyAxis());
                boolean hasBelow = greenhouseTiles.contains(loc.getxAxis() + "," + (loc.getyAxis() - 1));

                if (!hasLeft && !hasBelow) greenhouseAnchors.add(loc);
            }
        }

        for (Location anchor : greenhouseAnchors) {
            float drawX = offsetX + anchor.getxAxis() * tileSize;
            float drawY = offsetY + (399 - anchor.getyAxis()) * tileSize - tileSize * (GH_TILES_H - 1);
            batch.draw(GREEN_HOUSE, drawX, drawY, tileSize * GH_TILES_W, tileSize * GH_TILES_H);
        }
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
