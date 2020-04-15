package nl.ictm2a4.javagame;

import java.awt.*;

public class Wall extends Collidable {

    public Wall(int gridX, int gridY) {
        super(gridX, gridY, 32, 32);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(getX() * getWidth(), getY() * getHeight(), getWidth(), getHeight());
    }
}
