package models.enums.Types;

import models.enums.Season;

import java.util.Set;

public enum TypeOfTile {
    LAKE('L'),
    GREENHOUSE('G'),
    HOUSE('H'),
    TREE('T'),
    STONE('S');

    private final char nameOfMap;

    TypeOfTile(char name) {
        this.nameOfMap = name;
    }
}
