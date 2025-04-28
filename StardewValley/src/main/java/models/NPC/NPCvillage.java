package models.NPC;

import models.Fundementals.LocationOfRectangle;
import models.Place.Place;

import java.util.ArrayList;

public class NPCvillage implements Place {
    ArrayList<NPC> NPCs = new ArrayList<>();
    LocationOfRectangle locationOfRectangle;

    @Override
    public LocationOfRectangle getLocation() {
        return this.locationOfRectangle;
    }
}
