package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.Main;
import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.Level;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Player extends GameObject {

    private PlayerStatus status;

    public Player(Level level, int gridX, int gridY) {
        super(level,
            ((gridX * LevelLoader.gridWidth) + 4),
            ((gridY * LevelLoader.gridHeight) + 2),
            16, 24);
        setCollidable(false);
        status = PlayerStatus.IDLE;
    }

    public void checkMove(List<Integer> pressedKeys) {
        int stepSize = 8;

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
        g.setColor(Color.red);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void tick() {
        List<Integer> pressedKeys = Main.screen.pressedKeys;

        // Movement
        checkMove(pressedKeys);

        // Status
        // If none of the player's action buttons are being held, return to the idle status
        if(!pressedKeys.contains(KeyEvent.VK_D) && !pressedKeys.contains(KeyEvent.VK_D) && !pressedKeys.contains(KeyEvent.VK_W) && !pressedKeys.contains(KeyEvent.VK_A))
        {
            status = PlayerStatus.IDLE;
        }

        if(pressedKeys.contains(KeyEvent.VK_D))
        {
            status = PlayerStatus.MOVINGRIGHT;
        }
        else if (pressedKeys.contains(KeyEvent.VK_A))
        {
            status = PlayerStatus.MOVINGLEFT;
        }
    }
}