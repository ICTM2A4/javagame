package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public class Torch extends GameObject {

    private final int ANIMATEDELAY = 3;
    public static final int IMAGEAMOUNT = 4;

    private int currentImage = 0;
    private int animateCount = 0;

    public Torch(int gridX, int gridY) {
        super(gridX * LevelLoader.GRIDWIDTH, gridY * LevelLoader.GRIDHEIGHT, 32,32);
        setCollidable(false);
        setyIndex(8);
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
        if (animateCount - ANIMATEDELAY == 0) {
            animateCount = 0;
            currentImage++;
            if (currentImage == IMAGEAMOUNT) currentImage = 0;
        }
    }
}
