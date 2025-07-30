package org.example.Common.models.NPC;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.enums.Types.TypeOfTile;
import org.example.Common.models.Item;
import org.example.Common.models.MapDetails.Shack;
import org.example.Common.models.Place.Place;
import org.example.Common.models.enums.NPCdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NPCvillage implements Place {
    private ArrayList<NPC> NPCs = new ArrayList<>();
    private LocationOfRectangle locationOfRectangle;
    private Map<String, NPC> npcsByName = new HashMap<>();
    private Map<Shack, NPC> npcsByShack = new HashMap<>();
    private Random random = new Random();

    public NPCvillage(LocationOfRectangle location) {
        this.locationOfRectangle = location;
        setupNPCHouses();
        initializeNPCs();
    }

    private void initializeNPCs() {
        createNPCsFromEnum();
    }

    private void setupNPCHouses() {
        // Place 5 NPC houses in the village area
        int houseSize = 4; // 4x4 tiles for each house
        int spacing = 6; // Space between houses
        int startX = locationOfRectangle.getTopLeftCorner().getxAxis() + 2;
        int startY = locationOfRectangle.getTopLeftCorner().getyAxis() + 2;
        
        for (int i = 0; i < 5; i++) {
            // Set the house area to NPC_HOUSE tile type
            for (int x = startX; x < startX + houseSize; x++) {
                for (int y = startY; y < startY + houseSize; y++) {
                    Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
                    if (location != null) {
                        location.setTypeOfTile(TypeOfTile.NPC_HOUSE);
                    }
                }
            }
            
            // Move to next house position
            startX += houseSize + spacing;
            
            // If we've reached the end of the row, move to next row
            if (startX + houseSize > locationOfRectangle.getTopLeftCorner().getxAxis() + locationOfRectangle.getWidth() - 2) {
                startX = locationOfRectangle.getTopLeftCorner().getxAxis() + 2;
                startY += houseSize + spacing;
            }
        }
    }

    private void createNPCsFromEnum() {
        int spacing = 4;
        int startX = locationOfRectangle.getTopLeftCorner().getxAxis() + 2;
        int startY = locationOfRectangle.getTopLeftCorner().getyAxis() + 2;

        for (NPCdetails npcDetail : NPCdetails.values()) {
            Location topLeft = new Location(startX, startY);
            Location bottomRight = new Location(topLeft.getxAxis() + 2, topLeft.getyAxis() + 2);
            LocationOfRectangle shackLocation = new LocationOfRectangle(topLeft, bottomRight);

            Shack npcShack = new Shack(shackLocation);
            Location npcLocation = new Location(topLeft.getxAxis(), topLeft.getyAxis() + 1);

            NPC npc = new NPC(npcDetail, npcLocation, npcShack);

            addNPC(npc);

            startX += spacing;

            if (startX > locationOfRectangle.getTopLeftCorner().getxAxis() + locationOfRectangle.getWidth() - 4) {
                startX = locationOfRectangle.getTopLeftCorner().getxAxis() + 2;
                startY += spacing;
            }
        }
    }


    public void addNPC(NPC npc) {
        NPCs.add(npc);
        npcsByName.put(npc.getName(), npc);
        npcsByShack.put(npc.getShack(), npc);
    }

    public NPC getNPCByName(String name) {
        return npcsByName.get(name);
    }

    public NPC getNPCByShack(Shack shack) {
        return npcsByShack.get(shack);
    }

    public ArrayList<NPC> getAllNPCs() {
        return NPCs;
    }

    public boolean isPlayerNearNPC(Player player, NPC npc) {
        int playerX = player.getUserLocation().getxAxis();
        int playerY = player.getUserLocation().getyAxis();
        int npcX = npc.getUserLocation().getxAxis();
        int npcY = npc.getUserLocation().getyAxis();

        return Math.abs(playerX - npcX) <= 1 && Math.abs(playerY - npcY) <= 1;
    }

    public void resetDailyNPCFlags() {
        for (NPC npc : NPCs) {
            npc.resetDailyFlags();
        }
    }

    public void processDailyGifts(Player player) {
        for (NPC npc : NPCs) {
            if (npc.getFriendshipLevel(player) >= 3 && npc.shouldGiveGiftToday()) {
                Item gift = npc.getRandomGiftToGive();
                if (gift != null) {
                    player.getBackPack().addItem(gift, 1);
                    System.out.println(npc.getName() + " sent you a gift: " + gift.getName());
                }
            }
        }
    }

    @Override
    public LocationOfRectangle getLocation() {
        return this.locationOfRectangle;
    }
}
