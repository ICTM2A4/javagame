package nl.ictm2a4.javagame;

import java.awt.*;

public class Wall extends GameObject {

    public Wall(Level level, int gridX, int gridY) {
        super(level, gridX * Main.gridWidth, gridY * Main.gridHeight, Main.gridWidth, Main.gridHeight);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(Main.loadImage("textures/wall-" + this.connectedFacesSum() + ".jpg"), getX(), getY(), Main.screen.getLevel());
    }
}
