package nl.ictm2a4.javagame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Player extends GameObject {

    public Player(Level level, int x, int y) {
        super(level, x, y, 16, 16);
        setCollidable(false);
    }

    public void checkMove(KeyEvent event) {
        int key = event.getKeyCode();
        int stepSize = 8;

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

    private void move(int x, int y) {
        boolean canMove = true;
        for(GameObject c : Main.screen.getLevel().getGameObjects()) {
            if(c.checkCollide(this, x, y)) {
                canMove = false;
                break;
            }
        }
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
    public void loadImage() {

    }
}
