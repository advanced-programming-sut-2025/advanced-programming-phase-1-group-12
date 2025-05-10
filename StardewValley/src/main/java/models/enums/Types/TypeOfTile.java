package models.enums.Types;

import models.enums.Season;

import java.util.Set;

public enum TypeOfTile {
    LAKE('L'),
    GREENHOUSE('G'),
    TREE('T'),
    STONE('S'),
    QUARRY('Q'),
    HOUSE('H'),
    GROUND('.'),
    PLOUGHED_LAND('P'),
    BURNED_GROUND('b'),
    STORE('s'),

    //this is used for seeing if we can buy an animal
    //TODO:big or Deluxe stuff is not shown
    COOP('C'),
    BARN('B'),
    NPC_VILLAGE('N'),
    PLANT('p');

    private final char nameOfMap;

    TypeOfTile(char name) {
        this.nameOfMap = name;
    }

    public char getNameOfMap() {
        return nameOfMap;
    }
}