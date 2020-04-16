package nl.ictm2a4.javagame;

import java.awt.*;

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
        return false;
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
        for (int n = 0; n <= 3; n++)
            if (!faces[n]) sum += 2^n;

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
