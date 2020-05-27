package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.Level;

import java.awt.*;
import java.util.ArrayList;

public abstract class GameObject {

    private int x, y, extra, width, height;
    private boolean collidable = true;
    private int yIndex;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.yIndex = 0;
    }

    /**
     * Set if the GameObject is collidable, default is true
     * @param collidable new collidable setting
     */
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
        return LevelLoader.getInstance().getCurrentLevel().get().getGameObjects().stream()
                .anyMatch(gameObject -> gameObject.checkCollideSingle(this, x, y) && gameObject.isCollidable());
    }

    @Deprecated
    public GameObject[] checkCollideGameObjects(int x, int y) {
        return LevelLoader.getInstance().getCurrentLevel().get().getGameObjects()
                .stream()
                .filter(gameObject -> gameObject.checkCollideSingle(this, x, y) && gameObject.isCollidable())
                .toArray(GameObject[]::new);
    }

    public GameObject[] checkCollideAllGameObjects(int x, int y) {
        return LevelLoader.getInstance().getCurrentLevel().get().getGameObjects()
                .stream()
                .filter(gameObject -> gameObject.checkCollideSingle(this, x, y))
                .toArray(GameObject[]::new);
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

    /**
     * Set the yIndex of the object, used in the drawing order
     * @param yIndex index of the object
     */
    public void setyIndex(int yIndex) {
        this.yIndex = yIndex;
    }

    /**
     * Draw the object
     * @param g Graphics from the Frame,Panel
     */
    public abstract void draw(Graphics g);

    /**
     * Get the x of the GameObject
     * @return GameObject's X
     */
    public int getX() {
        return x;
    }

    /**
     * Get the y of the GameObject
     * @return GameObject's Y
     */
    public int getY() {
        return y;
    }

    /**
     * Set the new GameObject's x
     * @param x The x to set
     */
    public void setX(int x) {this.x = x;}

    /**
     * Set the new GameObject's y
     * @param y The y to set
     */
    public void setY(int y) {this.y = y;}

    /**
     * Get the height of the GameObject
     * @return GameObject's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the width of the GameObject
     * @return GameObject's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the sum of all connected faces
     * North: 1
     * East:  2
     * South: 4
     * West:  8
     * @param faces Boolean array true if face is connected, false if no connected face is found
     * @return The total sum of all connected faces
     */
    public static int connectedFacesSum(boolean[] faces) {
        int sum = 0;
        for (int n = 0; n < 4; n++)
            if (!faces[n]) sum += Math.pow(2,n);
        return sum;
    }

    /**
     * Get the Y index of the object
     * @return Yindex of object
     */
    public int getyIndex() {
        return yIndex;
    }

    /**
     * Locate all connected faces to a GameObject of same object type
     *
     * [0]: north
     * [1]: east
     * [2]: south
     * [3]: west
     *
     * @return Boolean array true if connected faces is found, false if no connected face is found
     */
    public boolean[] hasConnectedFaces(Class<? extends GameObject> match) {
        ArrayList<Class<? extends GameObject>> list = new ArrayList<>();
        list.add(match);
        return hasConnectedFaces(list);
    }

    public boolean[] hasConnectedFaces(ArrayList<Class<? extends GameObject>> objectMatch) {
        ArrayList<String> matches = new ArrayList<>();
        for(Class object : objectMatch)
            matches.add(object.getCanonicalName());

        Level level = LevelLoader.getInstance().getCurrentLevel().get();

        return new boolean[] {
            level.fromCoordsToArray(getX(), getY() - LevelLoader.GRIDHEIGHT).anyMatch(o -> matches.contains(o.getClass().getCanonicalName())),
            level.fromCoordsToArray(getX() + LevelLoader.GRIDWIDTH, getY()).anyMatch(o -> matches.contains(o.getClass().getCanonicalName())),
            level.fromCoordsToArray(getX(), getY() + LevelLoader.GRIDHEIGHT).anyMatch(o -> matches.contains(o.getClass().getCanonicalName())),
            level.fromCoordsToArray(getX() - LevelLoader.GRIDWIDTH, getY()).anyMatch(o -> matches.contains(o.getClass().getCanonicalName())),
        };
    }

    /**
     * Find the collidable status of the GameObject
     * @return true if object is collidable, false if object is not collidable
     */
    public boolean isCollidable() {
        return this.collidable;
    }

    /**
     * Tick the GameObject
     */
    public void tick() {}

    public int getExtra() {
        return this.extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }
}
