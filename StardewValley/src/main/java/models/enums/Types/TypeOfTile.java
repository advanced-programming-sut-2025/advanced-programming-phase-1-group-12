package models.enums.Types;

import models.enums.Season;

import java.util.Set;

public enum TypeOfTile {
    LAKE('L'),
    GREENHOUSE('G'),
    TREE('T'),
    STONE('S'),
    QUARRY('Q'),
    HOUSE('H');

    private final char nameOfMap;

    TypeOfTile(char name) {
        this.nameOfMap = name;
    }

    public char getNameOfMap() {
        return nameOfMap;
    }
}
