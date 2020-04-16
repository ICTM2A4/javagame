package nl.ictm2a4.javagame;

import java.awt.*;

public class EndPoint extends GameObject {

    public EndPoint(Level level, int gridX, int gridY) {
        super(
            level,
            gridX * Main.gridWidth + ((Main.gridWidth - 16) / 2),
            gridY * Main.gridHeight + ((Main.gridHeight - 16) / 2),
            16,16);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result) {
            this.getLevel().setVisible(false);
            System.out.println("Je hebt gewonnen");
        }

        return result;
    }
}
