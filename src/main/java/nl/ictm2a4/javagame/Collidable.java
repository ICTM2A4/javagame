package nl.ictm2a4.javagame;

import java.awt.*;

public abstract class Collidable implements GameObject {

    private int x, y, width, height;

    public Collidable(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

    public void setX(int x) {this.x = x;}

    public  void setY(int y) {this.y = y;}

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


}
