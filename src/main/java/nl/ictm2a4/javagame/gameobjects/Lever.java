package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Lever extends GameObject {
    private boolean active;

    private int keycode;

    public Lever(int gridX, int gridY, int keyCode) {
        super(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, LevelLoader.gridWidth, LevelLoader.gridHeight);
        setCollidable(false);
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
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);
        List<Integer> pressedKeys = GameScreen.getInstance().getPressedKeys();

        if (result && gameObject instanceof Player && !active && pressedKeys.contains(KeyEvent.VK_SPACE)){
            activate();
            System.out.println("Geactiveerd");
        }

        return result;
    }
}
