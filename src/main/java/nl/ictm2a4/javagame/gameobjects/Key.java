package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public class Key extends GameObject {

    private int keyCode;

    public Key(int gridX, int gridY, int keyCode){
        super(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, LevelLoader.gridWidth, LevelLoader.gridHeight);

        this.keyCode = keyCode;
    }

    public int getKeyCode(){
        return keyCode;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getKeyImage(0),
                getX(), getY(),
                LevelLoader.getInstance().getCurrentLevel().get());
    }

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player)
            // TODO OPPAKKEN DOOR PLAYER HIER
            
        return result;
    }
}
