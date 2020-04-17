package nl.ictm2a4.javagame;

import java.awt.*;

public class Wall extends GameObject {

    private Image image;

    public Wall(Level level, int gridX, int gridY) {
        super(level, gridX * Main.gridWidth, gridY * Main.gridHeight, Main.gridWidth, Main.gridHeight);
    }

    @Override
    public void loadImage() {
        image = Main.loadImage("textures/wall-" + connectedFacesSum(this.hasConnectedFaces()) + ".jpg");
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, getX(), getY(), Main.screen.getLevel());
    }
}
