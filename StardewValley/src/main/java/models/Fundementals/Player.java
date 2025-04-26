package models.Fundementals;

import models.MapDetails.Shack;
import models.Refrigrator;
import models.RelatedToUser.Ability;
import models.RelatedToUser.Energy;
import models.RelatedToUser.User;
import models.RelationShip;
import models.map;

import java.util.ArrayList;

public class Player {
    private User user;

    private Location userLocation;

    private Shack shack;

    private map map;

    private boolean isMarried;

    private Energy energy;

    public Refrigrator Refrigrator = new Refrigrator();

    private ArrayList<Ability> abilitis = new ArrayList<Ability>();

    private ArrayList<RelationShip> relationShips = new ArrayList<>();

    private ArrayList<RelationShip.Trade> trade = new ArrayList<>();

    public void collapse() {
    }

    public Player(User user, Location userLocation, Shack shack, map map, boolean isMarried, Energy energy,
                  Refrigrator refrigrator, ArrayList<Ability> abilitis, ArrayList<RelationShip> relationShips,
                  ArrayList<RelationShip.Trade> trade) {
        this.user = user;
        this.userLocation = userLocation;
        this.shack = shack;
        this.map = map;
        this.isMarried = isMarried;
        this.energy = energy;
        Refrigrator = refrigrator;
        this.abilitis = abilitis;
        this.relationShips = relationShips;
        this.trade = trade;
    }

    public User getUser() {
        return user;
    }
}
