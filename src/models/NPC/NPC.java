package models.NPC;

import models.Fundementals.Location;
import models.MapDetails.Shack;
import models.Refrigrator;
import models.enums.NPCdetails;

import java.util.ArrayList;

public class NPC {
    private String name;

    private Location userLocation;

    private Shack shack;

    private boolean isMarried;

    private int energy;

    public Refrigrator Refrigrator = new Refrigrator();


    private int count =0;

    private NPCdetails details;

    private ArrayList<Quest> quests = new ArrayList<>();

    public class Quest {
    }

}
