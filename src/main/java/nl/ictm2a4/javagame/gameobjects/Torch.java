package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public class Torch extends GameObject {

    private final int animateDelay = 3;
    public static final int imageAmount = 4;

    private int currentImage = 0;
    private int animateCount = 0;

    public Torch(int gridX, int gridY) {
        super(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, 32,32);
        setCollidable(false);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getTorchImage(currentImage),
            getX(), getY(),
            LevelLoader.getInstance().getCurrentLevel().get());
    }

    /**
     * Update the coin image based on it's current frame
     */
    @Override
    public void tick() {
        animateCount++;
        if (animateCount - animateDelay == 0) {
            animateCount = 0;
            currentImage++;
            if (currentImage == imageAmount) currentImage = 0;
        }
    }
}
