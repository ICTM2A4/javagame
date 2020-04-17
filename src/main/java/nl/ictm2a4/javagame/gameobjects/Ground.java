package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public class Ground extends GameObject {

    private Image image;

    public Ground(Level level, int gridX, int gridY) {
        super(level, gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, LevelLoader.gridWidth, LevelLoader.gridHeight);
        setCollidable(false);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getGroundTile(connectedFacesSum(this.hasConnectedFaces())),
            getX(), getY(),
            LevelLoader.getInstance().getCurrentLevel().get());
    }
}
