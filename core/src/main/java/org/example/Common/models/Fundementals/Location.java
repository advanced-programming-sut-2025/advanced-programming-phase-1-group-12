package org.example.Common.models.Fundementals;

import org.example.Common.models.enums.Types.TypeOfTile;
import java.util.Objects;

public class Location {
    private int xAxis;

    private int yAxis;

    private boolean isWalkable;

    private TypeOfTile typeOfTile;

    private Object objectInTile;

    public Location() {
        //for jackson
    }


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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return xAxis == location.xAxis && yAxis == location.yAxis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xAxis, yAxis);
    }
}

