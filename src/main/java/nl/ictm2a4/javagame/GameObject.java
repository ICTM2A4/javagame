package nl.ictm2a4.javagame;

import java.awt.*;

public abstract class GameObject {

    private int x, y, width, height;
    private boolean collidable = true;
    private Level level;

    public GameObject(Level level, int x, int y, int width, int height) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
  
    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean checkCollide(GameObject gameObject, int x, int y) {
        for (GameObject otherGameObject : Main.screen.getLevel().getGameObjects()) {
            if(!otherGameObject.isCollidable()) continue;
            if(otherGameObject.checkCollideSingle(gameObject, x, y)){
                // Another collidable has been found at the given position
                return true;
            }
        }
      
        return false;
    }

    /*
    Checks if the given collidable would have a collision with the given other collidable at the given coordinates.
     */
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        if (x >= this.getX() + this.getWidth() || this.getX() >= x + gameObject.getWidth()){
            // The objects do not overlap on the X axis
            return false;
        }

        if (y >= this.getY() + this.getHeight() || this.getY() >= y + gameObject.getHeight()) {
            // The objects do not overlap on the Y axis
            return false;
        }

        // The objects overlap on either or bot@h of the axis.
        return true;
    }

    public abstract void draw(Graphics g);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {this.x = x;}

    public  void setY(int y) {this.y = y;}

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Level getLevel() {
        return this.level;
    }
  
    public static int connectedFacesSum(boolean[] faces) {
        // north : 1
        // east : 2
        // south : 4
        // west : 8

        int sum = 0;
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
        for(GameObject gameObject : level.getGameObjects()) {
            if (!gameObject.getClass().getCanonicalName().equals(this.getClass().getCanonicalName()))
                continue;

            if (this.getX() == gameObject.getX()) {
                if (this.getY() - Main.gridHeight == gameObject.getY()) // north
                    result[0] = true;
                if (this.getY() + Main.gridHeight == gameObject.getY()) // south
                    result[2] = true;
            } if (getY() == gameObject.getY()) {
                if (this.getX() + Main.gridWidth == gameObject.getX()) // east
                    result[1] = true;
                if (this.getX() - Main.gridWidth == gameObject.getX()) // west
                    result[3] = true;
            }
        }
        return result;
    }

    public boolean isCollidable() {
        return this.collidable;
    }
}
