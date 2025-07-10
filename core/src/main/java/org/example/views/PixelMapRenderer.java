package org.example.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.Fundementals.Location;
import org.example.models.Place.Farm;
import org.example.models.enums.Types.TreeType;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.enums.foraging.Tree;
import org.example.models.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PixelMapRenderer {
    private final int tileSize = 100;
    private static final int TILES_W = 4;
    private static final int TILES_H = 4;
    private final map gameMap;
    private final Set<String> greenhouseTiles = new HashSet<>();
    private final Set<String> houseTiles = new HashSet<>();
    public static Texture PLANTS;
    public static Texture DARK_GREEN_FLOOR;
    public static Texture LAKE_TEXTURE;
    public static Texture BURNED_GROUND;
    public static Texture STONE;
    public static Texture NPC_VILLAGE;
    public static Texture GREEN_HOUSE;
    public static Texture HOUSE;
    public static Texture QUARRY;
    public static Texture GROUND;
    public static Texture STORE;
    public static Texture TREE;

    public PixelMapRenderer(map gameMap) {
        this.gameMap = gameMap;
        loadTextures();
        cacheGreenhouseTiles();
        cacheHouseTiles();
    }

    private void loadTextures() {
        LAKE_TEXTURE = new Texture("Flooring/Flooring_26.png");
        PLANTS = new Texture("Flooring/Flooring_50.png");
        STONE = new Texture("Rock/Farm_Boulder.png");
        NPC_VILLAGE = new Texture("Flooring/Flooring_83.png");
        GREEN_HOUSE = new Texture("Greenhouse/greenhouse.png");
        QUARRY = new Texture("Flooring/Flooring_08.png");
        BURNED_GROUND = new Texture("Flooring/Flooring_39.png");
        GROUND = new Texture("Flooring/Flooring_53.png");
        STORE = new Texture("Flooring/Flooring_02.png");
        TREE = new Texture("Flooring/Flooring_29.png");
        DARK_GREEN_FLOOR = new Texture("Flooring/Flooring_02.png");
        HOUSE = new Texture("House_farmer.png");
    }

    private void cacheGreenhouseTiles() {
        for (Location l : gameMap.getTilesOfMap()) {
            if (l.getTypeOfTile() == TypeOfTile.GREENHOUSE) {
                greenhouseTiles.add(l.getxAxis() + "," + l.getyAxis());
            }
        }
    }

    private void cacheHouseTiles() {
        for (Location l : gameMap.getTilesOfMap()) {
            if (l.getTypeOfTile() == TypeOfTile.HOUSE) {
                houseTiles.add(l.getxAxis() + "," + l.getyAxis());
            }
        }
    }

    private Texture getTextureForTile(TypeOfTile tileType) {
        switch (tileType) {
            case LAKE:
                return LAKE_TEXTURE;
            case TREE:
                return treeType();
            case QUARRY:
                return QUARRY;
            case STORE:
                return STORE;
            case STONE:
                return STONE;
            case NPC_VILLAGE:
                return NPC_VILLAGE;
            case BURNED_GROUND:
                return BURNED_GROUND;
            case PLANT:
                return PLANTS;
            default:
                return GROUND;
        }
    }

    private Texture treeType() {
        List<Tree> trees = new ArrayList<>();
        for(Farm farm : gameMap.getFarms()){
            for(Tree tree : farm.getTrees()){
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
                default:
                    return TREE;
            }
        }
        return TREE;
    }

    public void render(SpriteBatch batch, int offsetX, int offsetY) {
        batch.end();
        batch.begin();

        List<Location> greenhouseAnchors = new ArrayList<>();
        List<Location> houseAnchors = new ArrayList<>();

        for (Location loc : gameMap.getTilesOfMap()) {
            Texture base = getTextureForTile(loc.getTypeOfTile());
            batch.draw(base,
                offsetX + loc.getxAxis() * tileSize, offsetY + (399 - loc.getyAxis()) * tileSize,
                tileSize, tileSize);

            if (loc.getTypeOfTile() == TypeOfTile.GREENHOUSE) {
                boolean hasLeft = greenhouseTiles.contains((loc.getxAxis() - 1) + "," + loc.getyAxis());
                boolean hasBelow = greenhouseTiles.contains(loc.getxAxis() + "," + (loc.getyAxis() - 1));

                if (!hasLeft && !hasBelow) greenhouseAnchors.add(loc);
            } else if (loc.getTypeOfTile() == TypeOfTile.HOUSE) {
                boolean hasLeft = houseTiles.contains((loc.getxAxis() - 1) + "," + loc.getyAxis());
                boolean hasAbove = houseTiles.contains(loc.getxAxis() + "," + (loc.getyAxis() + 1));

                if (!hasLeft && !hasAbove) houseAnchors.add(loc);
            }
        }
        for (Location anchor : greenhouseAnchors) {
            float drawX = offsetX + anchor.getxAxis() * tileSize;
            float drawY = offsetY + (399 - anchor.getyAxis()) * tileSize - tileSize * (TILES_H - 1);
            batch.draw(GREEN_HOUSE, drawX, drawY, tileSize * TILES_W, tileSize * TILES_H);
        }
        for (Location anchor : houseAnchors) {
            float drawX = offsetX + anchor.getxAxis() * tileSize;
            float drawY = offsetY + (399 - anchor.getyAxis()) * tileSize;
            batch.draw(HOUSE, drawX, drawY, tileSize * TILES_W, tileSize * TILES_H);
        }
//        for(Location anchor : gameMap.getTilesOfMap()) {
//            float drawX = offsetX + anchor.getxAxis();
//            float drawY = offsetY + (399 - anchor.getyAxis());
//            if(anchor.getTypeOfTile() == TypeOfTile.STONE) {
//                batch.draw(STONE, drawX, drawY);
//            }
//        }
    }

    public void dispose() {
        PLANTS.dispose();
        LAKE_TEXTURE.dispose();
        STONE.dispose();
        STORE.dispose();
        NPC_VILLAGE.dispose();
        QUARRY.dispose();
        GROUND.dispose();
        BURNED_GROUND.dispose();
        DARK_GREEN_FLOOR.dispose();
        TREE.dispose();
        GREEN_HOUSE.dispose();
        HOUSE.dispose();
    }
}
