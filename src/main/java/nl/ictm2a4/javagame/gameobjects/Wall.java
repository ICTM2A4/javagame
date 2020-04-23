package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public class Wall extends GameObject {

    public Wall(int gridX, int gridY) {
        super(gridX * LevelLoader.GRIDWIDTH, gridY * LevelLoader.GRIDHEIGHT, LevelLoader.GRIDWIDTH, LevelLoader.GRIDHEIGHT);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getWallTile(connectedFacesSum(this.hasConnectedFaces())),
            getX(), getY(),
            LevelLoader.getInstance().getCurrentLevel().get());
    }
}
