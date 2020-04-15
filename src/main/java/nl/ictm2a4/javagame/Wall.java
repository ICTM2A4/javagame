package nl.ictm2a4.javagame;

import java.awt.*;

public class Wall extends Collidable {

    public Wall(int x, int y) {
        super(x, y, Main.gridWidth, Main.gridHeight);
    }

    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(getX() * Main.gridWidth, getY() * Main.gridHeight, getWidth(), getHeight());
    }
}
