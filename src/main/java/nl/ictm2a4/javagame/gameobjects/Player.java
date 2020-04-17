package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.Main;
import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.loaders.LevelLoader;

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
        checkMove();
    }
}
