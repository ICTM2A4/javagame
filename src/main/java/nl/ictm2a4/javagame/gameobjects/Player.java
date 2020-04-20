package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.Main;
import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.Level;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Player extends GameObject {

    private final int animateDelay = 4;
    private int currentImage = 0;
    private int animateCount = 0;

    private PlayerStatus status;

    public Player(Level level, int gridX, int gridY) {
        super(level,
            ((gridX * LevelLoader.gridWidth) + 4),
            ((gridY * LevelLoader.gridHeight) + 2),
            16, 20);
        setCollidable(false);
        status = PlayerStatus.IDLE;
    }

    public void checkMove() {
        int stepSize = 4;

        List<Integer> pressedKeys = Main.screen.pressedKeys;

        if(pressedKeys.contains(KeyEvent.VK_W)){
            move(getX(), getY()- stepSize);
        }

        if(pressedKeys.contains(KeyEvent.VK_A)){
            move(getX() - stepSize, getY());
        }

        if(pressedKeys.contains(KeyEvent.VK_S)){
            move(getX(), getY() + stepSize);
        }

        if(pressedKeys.contains(KeyEvent.VK_D)){
            move(getX() + stepSize, getY());
        }
    }

    // TODO: update player status when moving
    private void move(int x, int y) {
        boolean canMove = LevelLoader.getInstance().getCurrentLevel().get().getGameObjects().stream().anyMatch(
            object -> !object.checkCollide(x, y));

        if(canMove) {
            setX(x);
            setY(y);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getPlayerImage(status,currentImage),
            getX() - 4, getY() - 26,
            LevelLoader.getInstance().getCurrentLevel().get());
    }

    @Override
    public void tick() {
        checkMove();

        animateCount++;

        if (animateCount % animateDelay == 0) {
            animateCount = 0;

            currentImage++;
            if (currentImage >= status.getImageAmount())
                currentImage = 0;
        }

    }
}
