package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;
import java.util.Arrays;

public class Door extends GameObject {
    private int keyCode;

    public boolean open = false;

    public Door(int gridX, int gridY, int keyCode){
        super(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, LevelLoader.gridWidth, LevelLoader.gridHeight);

        this.keyCode = keyCode;
        setCollidable(true);
    }

    public int getKeyCode(){
        return keyCode;
    }

    @Override
    public void draw(Graphics g) {
        if(!open){
            g.drawImage(FileLoader.getInstance().getDoorImage(0),
                    getX(), getY(),
                    LevelLoader.getInstance().getCurrentLevel().get());
        }
    }

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player && !open){
            Player player = (Player) gameObject;
            if(player.inventoryHasKey(keyCode)){
                // Deur openen
                System.out.println("Deur geopend");
                open = true;
                setCollidable(false);
            }
        }

        return false;
    }
}
