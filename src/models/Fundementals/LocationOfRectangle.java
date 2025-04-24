package models.Fundementals;

public class LocationOfRectangle {
    private Location topLeftCorner;

    private Location downRightCorner;

    private int length;

    private int width;


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
        this.length =  downRightCorner.getxAxis() - topLeftCorner.getxAxis() ;
        this.width = - downRightCorner.getyAxis() + topLeftCorner.getyAxis() ;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }
}
