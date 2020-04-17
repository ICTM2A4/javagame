package nl.ictm2a4.javagame;

import java.awt.*;

public class Ground extends GameObject {

    private Image image;

    public Ground(Level level, int gridX, int gridY) {
        super(level, gridX * Main.gridWidth, gridY * Main.gridHeight, Main.gridWidth, Main.gridHeight);
        setCollidable(false);
    }

    @Override
    public void loadImage() {
        image = Main.loadImage("textures/ground-" + connectedFacesSum(this.hasConnectedFaces()) + ".jpg");
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, getX(), getY(), Main.screen.getLevel());
    }
}
