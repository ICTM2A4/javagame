package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.EndScreen;
import nl.ictm2a4.javagame.screens.GameScreen;

import java.awt.*;

public class Key extends GameObject {

    private int keyCode;

    public boolean active = true;

    public Key(int gridX, int gridY, int keyCode){
        super(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, LevelLoader.gridWidth, LevelLoader.gridHeight);

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

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player && active){

            Player player = (Player) gameObject;
            if(!player.inventoryHasKey(keyCode)){
                // Deur openen
                player.addToInventory(this);
                System.out.println("Sleutel opgepakt");
                active = false;
            }
        }

        return false;
    }
}
