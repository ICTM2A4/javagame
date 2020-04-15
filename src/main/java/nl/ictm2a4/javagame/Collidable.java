package nl.ictm2a4.javagame;

import java.awt.*;

public abstract class Collidable implements GameObject {

    private int x, y, width, height;

    public boolean checkCollide(Collidable collidable) {
        return false;
    }

    public abstract void draw(Graphics g);
}
