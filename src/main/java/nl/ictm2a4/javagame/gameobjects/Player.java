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

    private final int animateDelay = 6;
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

    public void checkMove(List<Integer> pressedKeys) {
        int stepSize = 4;

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
            object -> !object.checkCollide(this, x, y));

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
        List<Integer> pressedKeys = Main.screen.pressedKeys;

        // Movement
        checkMove(pressedKeys);

        // Status
        // If none of the player's action buttons are being held, return to the idle status
        if(!pressedKeys.contains(KeyEvent.VK_D) && !pressedKeys.contains(KeyEvent.VK_S) && !pressedKeys.contains(KeyEvent.VK_W) && !pressedKeys.contains(KeyEvent.VK_A))
            status = PlayerStatus.IDLE;
        if(pressedKeys.contains(KeyEvent.VK_D))
            status = PlayerStatus.MOVINGRIGHT;
        else if (pressedKeys.contains(KeyEvent.VK_A))
            status = PlayerStatus.MOVINGLEFT;

        animateCount++;


        if (animateCount % animateDelay == 0) {
            animateCount = 0;
            currentImage++;
        }
        if (currentImage >= status.getImageAmount())
            currentImage = 0;
    }
}
