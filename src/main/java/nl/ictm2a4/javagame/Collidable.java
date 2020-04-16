package nl.ictm2a4.javagame;

import java.awt.*;
import java.util.ArrayList;

public abstract class Collidable implements GameObject {

    private int x, y, width, height;

    public Collidable(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /*
    Checks if the given collidable would have a collision with any other collidable at the given coordinate. The collidables' width and height are taken into account. Only the collidables registered
    in the level's collidable list are taken into account.
     */
    public boolean checkCollide(Collidable collidable, int x, int y) {
        for (Collidable otherCollidable : Main.level.getCollidables()) {
            if(checkCollideSingle(collidable, otherCollidable, x, y)){
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
}
