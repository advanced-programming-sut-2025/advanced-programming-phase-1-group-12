package models.Fundementals;

import models.enums.Types.TypeOfTile;

public class Location {
    private int xAxis;

    private int yAxis;

    boolean isWalkable;

    private TypeOfTile typeOfTile;

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

    public static class LocationOfRectangle {
        Location topLeftCorner;

        Location downRightCorner;

        public Location getTopLeftCorner() {
            return topLeftCorner;
        }

        public void setTopLeftCorner(Location topLeftCorner) {
            this.topLeftCorner = topLeftCorner;
        }

        public Location getDownRightCorner() {
            return downRightCorner;
        }

        public void setDownRightCorner(Location topRightCorner) {
            this.downRightCorner = topRightCorner;
        }

        public LocationOfRectangle(Location topLeftCorner, Location downRightCorner) {
            this.topLeftCorner = topLeftCorner;
            this.downRightCorner = downRightCorner;
        }
    }
}

