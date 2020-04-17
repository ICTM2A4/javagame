package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.GameObject;
import nl.ictm2a4.javagame.Level;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EndPoint extends GameObject {

    private final int animateDelay = 3;
    private final int imageAmount = 7;

    private List<Image> images;
    private int currentImage = 0;
    private int animateCount = 0;

    public EndPoint(Level level, int gridX, int gridY) {
        super(
            level,
            gridX * LevelLoader.gridWidth + ((LevelLoader.gridWidth - 16) / 2),
            gridY * LevelLoader.gridHeight + ((LevelLoader.gridHeight - 16) / 2) - 2,
            16,16);
        images = new ArrayList<>();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getCoinImage(currentImage),
            getX(),getY(),
            LevelLoader.getInstance().getCurrentLevel().get());
    }

    @Override
    public void tick() {
        animateCount++;
        if (animateCount - animateDelay == 0) {
            animateCount = 0;
            currentImage++;
            if (currentImage == imageAmount) currentImage = 0;
        }
    }

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result) {
            this.getLevel().setVisible(false);
            System.out.println("Je hebt gewonnen");
        }

        return result;
    }
}
