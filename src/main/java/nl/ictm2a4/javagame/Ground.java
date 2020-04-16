package nl.ictm2a4.javagame;

import java.awt.*;

public class Ground extends GameObject {

    public Ground(Level level, int gridX, int gridY) {
        super(level, gridX * Main.gridWidth, gridY * Main.gridHeight, Main.gridWidth, Main.gridHeight);
        setCollidable(false);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(Main.loadImage("textures/ground-" + this.connectedFacesSum() + ".jpg"), getX(), getY(), Main.screen.getLevel());
    }
}
