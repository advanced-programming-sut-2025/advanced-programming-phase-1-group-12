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

    //this is used for seeing if we can buy an animal
    COOP('C'),
    BARN('B');

    private final char nameOfMap;

    TypeOfTile(char name) {
        this.nameOfMap = name;
    }

    public char getNameOfMap() {
        return nameOfMap;
    }
}
