package org.example.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Assets.PlantAssetsManager;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Player;
import org.example.models.ShippingBin;
import org.example.models.enums.Types.TypeOfTile;
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
            case QUARRY:
                return GameAssetManager.getGameAssetManager().getQUARRY();
            case STORE:
                return GameAssetManager.getGameAssetManager().getSTORE();
            case NPC_VILLAGE:
                return GameAssetManager.getGameAssetManager().getNPC_VILLAGE();
            case BURNED_GROUND:
                return GameAssetManager.getGameAssetManager().getBURNED_GROUND();
            case BARN:
                return GameAssetManager.getGameAssetManager().getBarn();
            case COOP:
                return GameAssetManager.getGameAssetManager().getCoop();
            case CRAFT:
                return GameAssetManager.craftType(location);
            case PLOUGHED_LAND:
                return GameAssetManager.getGameAssetManager().getPloughedLand();
            case SHIPPINGBIN:
                return GameAssetManager.getGameAssetManager().getShippingBin();
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
                offsetY + loc.getyAxis() * tileSize,
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
            float drawY = offsetY + anchor.getyAxis() * tileSize - tileSize * (TILES_H - 1);
            batch.draw(GameAssetManager.getGameAssetManager().getGREEN_HOUSE(), drawX, drawY, tileSize * TILES_W, tileSize * TILES_H);
        }
        for (Location anchor : houseAnchors) {
            float drawX = offsetX + anchor.getxAxis() * tileSize;
            float drawY = offsetY + anchor.getyAxis() * tileSize;
            batch.draw(GameAssetManager.getGameAssetManager().getHOUSE(), drawX, drawY, tileSize * TILES_W, tileSize * TILES_H);
        }
        for (Location loc : App.getCurrentGame().getMainMap().getTilesOfMap()) {
            float drawX = offsetX + loc.getxAxis() * tileSize;
            float drawY = offsetY + loc.getyAxis() * tileSize;
            if (loc.getTypeOfTile() == TypeOfTile.PLANT) {
                batch.draw(PlantAssetsManager.treeType(loc), drawX, drawY, tileSize, tileSize);
            } else if(loc.getTypeOfTile() == TypeOfTile.STONE){
                batch.draw(GameAssetManager.getGameAssetManager().getSTONE(), drawX, drawY, 50, 50);
            }for(Player player : App.getCurrentGame().getPlayers()) {
                if(player.getRefrigrator().getLocation().equals(loc)) {
                    batch.draw(GameAssetManager.getGameAssetManager().getFridge(), drawX, drawY, 50, 50);
                }
            }
        }
    }

    public void dispose() {
        GameAssetManager.dispose();
    }
}
