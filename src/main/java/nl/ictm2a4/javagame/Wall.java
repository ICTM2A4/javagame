package nl.ictm2a4.javagame;

import javax.imageio.ImageIO;
import java.awt.*;

public class Wall extends Collidable {

    public Wall(int gridX, int gridY) {
        super(gridX * Main.gridWidth, gridY * Main.gridHeight, Main.gridWidth, Main.gridHeight);
    }

    public void draw(Graphics g) {
        g.drawImage(Main.loadImage("textures/wall-" + this.connectedFacesSum() + ".jpg"), getX(), getY(), Main.level);
    }
}
