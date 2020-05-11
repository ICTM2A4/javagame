package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wall extends GameObject {

    private List<Point> endPoints;

    public Wall(int gridX, int gridY) {
        super(gridX * LevelLoader.GRIDWIDTH, gridY * LevelLoader.GRIDHEIGHT, LevelLoader.GRIDWIDTH, LevelLoader.GRIDHEIGHT);
        endPoints = Arrays.asList(
            new Point(this.getX(), this.getY()),
            new Point(this.getX() + LevelLoader.GRIDWIDTH, this.getY()),
            new Point(this.getX() + LevelLoader.GRIDWIDTH, this.getY() + LevelLoader.GRIDHEIGHT),
            new Point(this.getX(), this.getY() + LevelLoader.GRIDHEIGHT));
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getWallTile(connectedFacesSum(this.hasConnectedFaces())),
            getX(), getY(),
            LevelLoader.getInstance().getCurrentLevel().get());
    }

    public List<Point> getEndPoints() {
        return endPoints;
    }
}
