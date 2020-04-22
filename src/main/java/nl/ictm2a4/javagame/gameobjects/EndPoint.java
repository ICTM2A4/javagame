package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.EndScreen;
import nl.ictm2a4.javagame.screens.GameScreen;

import javax.swing.*;
import java.awt.*;

public class EndPoint extends GameObject {

    private final int animateDelay = 3;
    public static final int imageAmount = 7;

    private int currentImage = 0;
    private int animateCount = 0;

    public EndPoint(int gridX, int gridY) {
        super(
            gridX * LevelLoader.gridWidth + ((LevelLoader.gridWidth - 16) / 2),
            gridY * LevelLoader.gridHeight + ((LevelLoader.gridHeight - 16) / 2) - 2,
            16,16);
        setyIndex(9);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getCoinImage(currentImage),
            getX(),getY(),
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

    /**
     * Override the GameObject checkCollideSingle, test if player collided with the endpoint
     * @param gameObject GameObject which is colliding
     * @return result of isColliding check
     */
    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player)
            GameScreen.getInstance().setPanel(new EndScreen(), "finished!");

        return result;
    }
}
