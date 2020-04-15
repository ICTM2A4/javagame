package nl.ictm2a4.javagame;

import java.awt.*;

public class Ground extends Collidable {

    public Ground(int x, int y) {
        super(x, y, Main.gridWidth, Main.gridHeight);
        setCollidable(false);
    }

    @Override
    public void draw(Graphics g) {
        System.out.println("Floor: " + this.connectedFacesSum() + "; " + getX() + ", " + getY());
        g.drawImage(Main.loadImage("textures/ground-" + this.connectedFacesSum() + ".jpg"), getX() * Main.gridWidth, getY() * Main.gridHeight, Main.level);
    }
}
