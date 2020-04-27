package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.EndScreen;
import nl.ictm2a4.javagame.screens.GameScreen;

import java.awt.*;

public class Key extends Pickup {

    private int keyCode;

    public Key(int gridX, int gridY, int keyCode){
        super(gridX, gridY);
        this.keyCode = keyCode;
        setCollidable(false);
    }

    public int getKeyCode(){
        return keyCode;
    }

    @Override
    public void draw(Graphics g) {

        if(active){
            g.drawImage(FileLoader.getInstance().getKeyImage(keyCode < 3 ? keyCode : 0),
                    getX(), getY(),
                    LevelLoader.getInstance().getCurrentLevel().get());
        }
    }


}
