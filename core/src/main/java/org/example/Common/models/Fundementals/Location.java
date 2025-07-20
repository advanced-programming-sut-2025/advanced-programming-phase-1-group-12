package org.example.Common.models.Fundementals;

import org.example.Common.models.enums.Types.TypeOfTile;

public class Location {
    private int xAxis;

    private int yAxis;

    private boolean isWalkable;

    private TypeOfTile typeOfTile;

    private Object objectInTile;

    public Location(int xAxis, int yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public int getxAxis() {
        return xAxis;
    }

    public void setxAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    public TypeOfTile getTypeOfTile() {
        return typeOfTile;
    }

    public void setTypeOfTile(TypeOfTile typeOfTile) {
        this.typeOfTile = typeOfTile;
    }

    public void setWalkable(boolean walkable) {
        isWalkable = walkable;
    }

    public boolean isWalkable() {
        return isWalkable;
    }

    public void setObjectInTile(Object objectInTile) {
        this.objectInTile = objectInTile;
    }

    public Object getObjectInTile() {
        return objectInTile;
    }
}

