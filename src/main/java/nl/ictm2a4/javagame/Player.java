package nl.ictm2a4.javagame;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Collidable {

    public Player(int x, int y) {
        super(x, y, 16, 16);
    }

    public void checkMove(KeyEvent event) {
        int key = event.getKeyCode();
        int stepSize = 8;

        if(key == KeyEvent.VK_W){
            move(getX(), getY()- stepSize);
        }
        if(key == KeyEvent.VK_A){
            move(getX() - stepSize, getY());
        }
        if(key == KeyEvent.VK_S){
            move(getX(), getY() + stepSize);
        }
        if(key == KeyEvent.VK_D){
            move(getX() + stepSize, getY());
        }
    }

    private void move(int x, int y) {
        boolean canMove = true;
        for(Collidable c : Main.level.getCollidables()) {
            if(c  instanceof Player) {
                continue;
            }
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

    public void draw(Graphics g) {
        g.setColor(Color.orange);
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }
}
