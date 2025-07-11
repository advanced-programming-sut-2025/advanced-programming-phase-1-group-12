package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Fundementals.Location;
import org.example.models.Place.Farm;
import org.example.models.enums.Types.TreeType;
import org.example.models.enums.Types.TypeOfTile;
import org.example.models.enums.foraging.Tree;
import org.example.models.map;

import java.util.*;

public class PixelMapRenderer {
    private final int tileSize = 100;
    private static final int TILES_W = 4;
    private static final int TILES_H = 4;
    private final map gameMap;
    private final Set<String> greenhouseTiles = new HashSet<>();
    private final Set<String> houseTiles = new HashSet<>();
    public PixelMapRenderer(map gameMap) {
        this.gameMap = gameMap;
        cacheGreenhouseTiles();
        cacheHouseTiles();
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

    public Texture getTextureForTile(TypeOfTile tileType, Location location) {
        switch (tileType) {
            case LAKE:
                return GameAssetManager.getGameAssetManager().getLAKE_TEXTURE();
            case TREE:
                return GameAssetManager.treeType(location);
            case QUARRY:
                return GameAssetManager.getGameAssetManager().getQUARRY();
            case STORE:
                return GameAssetManager.getGameAssetManager().getSTORE();
            case STONE:
                return GameAssetManager.getGameAssetManager().getSTONE();
            case NPC_VILLAGE:
                return GameAssetManager.getGameAssetManager().getNPC_VILLAGE();
            case BURNED_GROUND:
                return GameAssetManager.getGameAssetManager().getBURNED_GROUND();
            case PLANT:
                return GameAssetManager.getGameAssetManager().getPLANTS();
            case BARN:
                return GameAssetManager.getGameAssetManager().getBarn();
                case COOP:
                    return GameAssetManager.getGameAssetManager().getCoop();
            default:
                return GameAssetManager.getGameAssetManager().getGROUND();
        }
    }

    public void render(SpriteBatch batch, int offsetX, int offsetY) {
        batch.end();
        batch.begin();

        List<Location> greenhouseAnchors = new ArrayList<>();
        List<Location> houseAnchors = new ArrayList<>();

        for (Location loc : gameMap.getTilesOfMap()) {
            Texture base = getTextureForTile(loc.getTypeOfTile(), loc);

            batch.draw(base,
                offsetX + loc.getxAxis() * tileSize,
                offsetY +  loc.getyAxis() * tileSize,
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
            float drawY = offsetY +  anchor.getyAxis() * tileSize - tileSize * (TILES_H - 1);
            batch.draw(GameAssetManager.getGameAssetManager().getGREEN_HOUSE(), drawX, drawY, tileSize * TILES_W, tileSize * TILES_H);
        }
        for (Location anchor : houseAnchors) {
            float drawX = offsetX + anchor.getxAxis() * tileSize;
            float drawY = offsetY + anchor.getyAxis() * tileSize;
            batch.draw(GameAssetManager.getGameAssetManager().getHOUSE(), drawX, drawY, tileSize * TILES_W, tileSize * TILES_H);
        }
//        batch.end();
    }


    public void dispose() {
        GameAssetManager.getGameAssetManager().getGROUND().dispose();
        GameAssetManager.getGameAssetManager().getSTONE().dispose();
        GameAssetManager.getGameAssetManager().getPLANTS().dispose();
        GameAssetManager.getGameAssetManager().getNPC_VILLAGE().dispose();
        GameAssetManager.getGameAssetManager().getBURNED_GROUND().dispose();
        GameAssetManager.getGameAssetManager().getQUARRY().dispose();
        GameAssetManager.getGameAssetManager().getSTORE().dispose();
        GameAssetManager.getGameAssetManager().getTREE().dispose();
        GameAssetManager.getGameAssetManager().getDARK_GREEN_FLOOR().dispose();
        GameAssetManager.getGameAssetManager().getHOUSE().dispose();
        GameAssetManager.getGameAssetManager().getLAKE_TEXTURE().dispose();

        GameAssetManager.getGameAssetManager().getBarn().dispose();
        GameAssetManager.getGameAssetManager().getCoop().dispose();

        GameAssetManager.getBANANA_TREE().dispose();
        GameAssetManager.getMUSHROOM_TREE().dispose();
        GameAssetManager.getMYSTIC_TREE().dispose();
        GameAssetManager.getMANGO_TREE().dispose();
        GameAssetManager.getOAK_TREE().dispose();
        GameAssetManager.getPEACH_TREE().dispose();
        GameAssetManager.getCHERRY_TREE().dispose();
        GameAssetManager.getAPPLE_TREE().dispose();
        GameAssetManager.getORANGE_TREE().dispose();
        GameAssetManager.getMAPLE_TREE().dispose();
        GameAssetManager.getAPRICOT_TREE().dispose();
        GameAssetManager.getPINE_TREE().dispose();

    }
}
