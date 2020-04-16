package nl.ictm2a4.javagame;

import java.awt.*;

public class Ground extends Collidable {

    public Ground(int gridX, int gridY) {
        super(gridX * Main.gridWidth, gridY * Main.gridHeight, Main.gridWidth, Main.gridHeight);
        setCollidable(false);
    }

    @Override
    public void draw(Graphics g) {
        System.out.println("Floor: " + this.connectedFacesSum() + "; " + getX() + ", " + getY());
        g.drawImage(Main.loadImage("textures/ground-" + this.connectedFacesSum() + ".jpg"), getX(), getY(), Main.level);
    }
}
