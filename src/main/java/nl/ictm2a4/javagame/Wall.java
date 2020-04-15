package nl.ictm2a4.javagame;

import javax.imageio.ImageIO;
import java.awt.*;

public class Wall extends Collidable {

    public Wall(int x, int y) {
        super(x, y, Main.gridWidth, Main.gridHeight);
    }

    public void draw(Graphics g) {
        g.drawImage(Main.loadImage("textures/wall-" + this.connectedFacesSum() + ".jpg"), getX() * Main.gridWidth, getY() * Main.gridHeight, Main.level);
    }
}
