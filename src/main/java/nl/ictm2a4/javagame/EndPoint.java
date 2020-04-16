package nl.ictm2a4.javagame;

import java.awt.*;

public class EndPoint extends Collidable {

    public EndPoint(int gridX, int gridY) {
        super(
            gridX + ((Main.gridWidth - 16) / 2),
            gridY + ((Main.gridHeight - 16) / 2),
            16,16);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}
