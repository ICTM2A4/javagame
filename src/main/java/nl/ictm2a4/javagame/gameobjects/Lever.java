package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

public class Lever extends GameObject {//implements EventListener, KeyListener {
    private boolean active;

    private int keycode;

    public Lever(int gridX, int gridY, int keyCode) {
        super(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, LevelLoader.gridWidth, LevelLoader.gridHeight);
        setCollidable(false);
        this.keycode = keyCode;
    }

    @Override
    public void draw(Graphics g) {
        if(!active){
            g.drawImage(FileLoader.getInstance().getLeverImage(0),
                    getX(), getY(),
                    LevelLoader.getInstance().getCurrentLevel().get());
        }
    }

    private void activate(){
        for(Door door : LevelLoader.getInstance().getCurrentLevel().get().getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof Door)
                .filter(gameObject -> ((Door)gameObject).getKeyCode() == keycode)
                .toArray(Door[]::new)){
            door.setOpen();
        }

        active = true;
    };

    @Override
    public void tick() {
        GameObject[] collisions = checkCollideAllGameObjects(getX(), getY());
        List<Integer> pressedKeys = GameScreen.getInstance().getPressedKeys();

        if(!active && Arrays.stream(collisions).anyMatch(gameObject -> gameObject instanceof Player) && pressedKeys.contains(KeyEvent.VK_SPACE)){
            System.out.println("HENDEL GEACTIVEERD");
            activate();
        }
    }
}
