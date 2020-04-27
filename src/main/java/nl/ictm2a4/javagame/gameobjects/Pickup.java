package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public class Pickup extends GameObject {

    public boolean active = true;

    public Pickup(int gridX, int gridY){
        super(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, LevelLoader.gridWidth, LevelLoader.gridHeight);
    }

    public void draw(Graphics g) { }

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player && active){

            Player player = (Player) gameObject;
            if(!player.getInventory().contains(this)){
                // Deur openen
                player.addToInventory(this);
                System.out.println("Item opgepakt");
                active = false;
            }
        }

        return false;
    }
}
