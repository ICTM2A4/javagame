package nl.ictm2a4.javagame;

import java.awt.*;

public class EndPoint extends Collidable {


    public EndPoint(int x, int y) {
        super(x, y, 22,22);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        int x = (getX() * Main.gridWidth) + ((Main.gridWidth - getWidth()) / 2);
        int y = (getY() * Main.gridHeight) + ((Main.gridHeight - getHeight()) / 2);
        g.fillOval(x, y, getWidth(), getHeight());
    }
}
