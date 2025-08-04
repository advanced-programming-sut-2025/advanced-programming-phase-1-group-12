package org.example.Common.models.NPC;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private LocationOfRectangle location;
    private Map<String, NPC> npcsByName = new HashMap<>();
    private Map<Shack, NPC> npcsByShack = new HashMap<>();
    private Random random = new Random();

    public NPCvillage(LocationOfRectangle location) {
        this.location = location;
        setupNPCHouses();
        initializeNPCs();
    }

    public NPCvillage(){}
    private void initializeNPCs() {
        createNPCsFromEnum();
    }

    private void setupNPCHouses() {
        // Place 5 NPC houses in the village area
        // One in the center, four in the corners
        int houseSize = 4; // 4x4 tiles for each house
        int villageWidth = location.getWidth();
        int villageHeight = location.getLength();
        int startX = location.getTopLeftCorner().getxAxis();
        int startY = location.getTopLeftCorner().getyAxis();

        // Calculate positions for 5 houses
        int[][] housePositions = {
            // Center house
            {startX + villageWidth/2 - houseSize/2, startY + villageHeight/2 - houseSize/2},
            // Corner houses
            {startX + 2, startY + 2}, // Top-left corner
            {startX + villageWidth - houseSize - 2, startY + 2}, // Top-right corner
            {startX + 2, startY + villageHeight - houseSize - 2}, // Bottom-left corner
            {startX + villageWidth - houseSize - 2, startY + villageHeight - houseSize - 2} // Bottom-right corner
        };

        for (int i = 0; i < 5; i++) {
            int houseX = housePositions[i][0];
            int houseY = housePositions[i][1];

            // Set the house area to NPC_HOUSE tile type
            for (int x = houseX; x < houseX + houseSize; x++) {
                for (int y = houseY; y < houseY + houseSize; y++) {
                    Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
                    if (location != null) {
                        location.setTypeOfTile(TypeOfTile.NPC_HOUSE);
                    }
                }
            }
        }
    }

    private void createNPCsFromEnum() {
        // Calculate house positions first
        ArrayList<Location> housePositions = calculateHousePositions();

        // Create NPCs and position them next to houses
        int npcIndex = 0;
        for (NPCdetails npcDetail : NPCdetails.values()) {
            if (npcIndex < housePositions.size()) {
                Location housePos = housePositions.get(npcIndex);

                // Position NPC next to the house (to the right of the house)
                Location npcLocation = new Location(housePos.getxAxis() + 4, housePos.getyAxis() + 2);

                // Create a small shack area for the NPC
                Location topLeft = new Location(npcLocation.getxAxis() - 1, npcLocation.getyAxis() - 1);
                Location bottomRight = new Location(npcLocation.getxAxis() + 1, npcLocation.getyAxis() + 1);
                LocationOfRectangle shackLocation = new LocationOfRectangle(topLeft, bottomRight);

                Shack npcShack = new Shack(shackLocation);
                NPC npc = new NPC(npcDetail, npcLocation, npcShack);

                addNPC(npc);
                npcIndex++;
            }
        }
    }

    private ArrayList<Location> calculateHousePositions() {
        ArrayList<Location> housePositions = new ArrayList<>();

        int houseSize = 4; // 4x4 tiles for each house
        int villageWidth = location.getWidth();
        int villageHeight = location.getLength();
        int startX = location.getTopLeftCorner().getxAxis();
        int startY = location.getTopLeftCorner().getyAxis();

        // Calculate positions for 5 houses
        int[][] positions = {
            // Center house
            {startX + villageWidth/2 - houseSize/2, startY + villageHeight/2 - houseSize/2},
            // Corner houses
            {startX + 2, startY + 2}, // Top-left corner
            {startX + villageWidth - houseSize - 2, startY + 2}, // Top-right corner
            {startX + 2, startY + villageHeight - houseSize - 2}, // Bottom-left corner
            {startX + villageWidth - houseSize - 2, startY + villageHeight - houseSize - 2} // Bottom-right corner
        };

        for (int i = 0; i < 5; i++) {
            // Add the top-left corner of each house
            housePositions.add(new Location(positions[i][0], positions[i][1]));
        }

        return housePositions;
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

    @JsonIgnore
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
        return this.location;
    }
}
