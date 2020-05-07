package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.ItemPickupEvent;
<<<<<<< HEAD
=======
import nl.ictm2a4.javagame.event.EventExecutor;
>>>>>>> Achievements start
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public abstract class Pickup extends GameObject {

    public boolean active = true;

    public Pickup(int gridX, int gridY){
        super(gridX * LevelLoader.GRIDWIDTH, gridY * LevelLoader.GRIDHEIGHT, LevelLoader.GRIDWIDTH, LevelLoader.GRIDHEIGHT);
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

                EventManager.getInstance().callEvent(new ItemPickupEvent(this));
            }
        }

        return false;
    }
}
