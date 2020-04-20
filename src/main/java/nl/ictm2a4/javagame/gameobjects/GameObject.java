package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.Level;

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

    /**
     *  Returns whether there is a collision with any other game object at a specified location. Useful for checking if an object can move to the given position.
     *  This function iterates the level's list of gameobjects, please add all of your gameobjects that need to be involved with the collision detection to this list.
     *
     * @param x the X coordinate of the position at which the collision will be checked
     * @param y the Y coordinate of the position at which the collision will be checked
     * @return If there is a collision between this object and any other game object at the given coordinates
     */
    public boolean checkCollide(int x, int y) {
        return LevelLoader.getInstance().getCurrentLevel().get().getGameObjects().stream().anyMatch(object -> object.checkCollideSingle(this, x, y) && object.isCollidable());
    }

    /**
     * Returns whether there is a collision between this gameobject and another gameobject at a specified location. Useful for checking if an object can move anywhere.
     *
     * @param otherGameObject The other game object against which the collision will be checked
     * @param x the X coordinate of the position at which the collision will be checked
     * @param y the Y coordinate of the position at which the collision will be checked
     * @return If there is a collision between this object and the other game object at the given coordinates
     */
    public boolean checkCollideSingle(GameObject otherGameObject, int x, int y) {
        if (x >= this.getX() + this.getWidth() || this.getX() >= x + otherGameObject.getWidth()){
            // The objects do not overlap on the X axis
            return false;
        }

        if (y >= this.getY() + this.getHeight() || this.getY() >= y + otherGameObject.getHeight()) {
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
                if (this.getY() - LevelLoader.gridHeight == gameObject.getY()) // north
                    result[0] = true;
                if (this.getY() + LevelLoader.gridHeight == gameObject.getY()) // south
                    result[2] = true;
            } if (getY() == gameObject.getY()) {
                if (this.getX() + LevelLoader.gridWidth == gameObject.getX()) // east
                    result[1] = true;
                if (this.getX() - LevelLoader.gridWidth == gameObject.getX()) // west
                    result[3] = true;
            }
        }
        return result;
    }

    public boolean isCollidable() {
        return this.collidable;
    }

    public void tick() {}

}
