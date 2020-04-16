package nl.ictm2a4.javagame;

import java.awt.*;
import java.util.ArrayList;

public abstract class Collidable implements GameObject {

    private int x, y, width, height;
    private boolean collidable = true;

    public Collidable(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
  
    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean checkCollide(Collidable collidable, int x, int y) {
        for (Collidable otherCollidable : Main.level.getCollidables()) {
            if(checkCollideSingle(collidable, otherCollidable, x, y)){
                // Another collidable has been found at the given position
                return true;
            }
        }
      
        return false;
    }

    /*
    Checks if the given collidable would have a collision with the given other collidable at the given coordinates.
     */
    public boolean checkCollideSingle(Collidable collidable, Collidable otherCollidable, int x, int y) {
        if (x >= otherCollidable.getX() + otherCollidable.getWidth() || otherCollidable.getX() >= x + collidable.getWidth()){
            // The objects do not overlap on the X axis
            return false;
        }

        if (y >= otherCollidable.getY() + otherCollidable.getHeight() || otherCollidable.getY() >= y + collidable.getHeight()) {
            // The objects do not overlap on the Y axis
            return false;
        }

        // The objects overlap on either or both of the axis.
        return true;
    }

    public abstract void draw(Graphics g);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int connectedFacesSum() {
        // north : 1
        // east : 2
        // south : 4
        // west : 8

        int sum = 0;

        boolean[] faces = hasConnectedFaces();
        for (int n = 0; n < 4; n++)
            if (!faces[n]) sum += Math.pow(2,n);
        return sum;
    }

    /*
    [0] north
    [1] east
    [2] south
    [3] west
     */
    public boolean[] hasConnectedFaces() {
        boolean[] result = {false,false,false,false};
        for(Collidable collidable : Main.level.getCollidables()) {
            if (!collidable.getClass().getCanonicalName().equals(this.getClass().getCanonicalName()))
                continue;

            if (this.getX() == collidable.getX()) {
                if (this.getY() - Main.gridHeight == collidable.getY()) // north
                    result[0] = true;
                if (this.getY() + Main.gridHeight == collidable.getY()) // south
                    result[2] = true;
            } if (getY() == collidable.getY()) {
                if (this.getX() + Main.gridWidth == collidable.getX()) // east
                    result[1] = true;
                if (this.getX() - Main.gridWidth == collidable.getX()) // west
                    result[3] = true;
            }
        }
        return result;
    }
}
