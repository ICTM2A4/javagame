package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public class Key extends Pickup {

    private int doorCode;

    public Key(int gridX, int gridY, int doorCode){
        super(gridX, gridY);
        this.doorCode = doorCode;
        setCollidable(false);
        setyIndex(4);
    }

    public int getDoorCode(){
        return doorCode;
    }

    @Override
    public void draw(Graphics g) {

        if(active){
            g.drawImage(FileLoader.getInstance().getKeyImage(doorCode <= 3 ? doorCode : 0),
                    getX(), getY(),
                    LevelLoader.getInstance().getCurrentLevel().get());
        }
    }
}
