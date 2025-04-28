package models.Fundementals;

import java.util.ArrayList;

public class LocationOfRectangle {
    private Location topLeftCorner;

    private Location downRightCorner;

    private int length;

    private int width;

    private ArrayList<Location> locationsInRectangle = new ArrayList<>();


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
        this.length = this.downRightCorner.getxAxis() - this.topLeftCorner.getxAxis() ;
        this.width =  this.downRightCorner.getyAxis() - this.topLeftCorner.getyAxis() ;
        for(int i = 0 ; i < this.length ; i++){
            for(int j = 0 ; j < this.width ; j++){
                this.locationsInRectangle.add(topLeftCorner);
            }
        }
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

}
