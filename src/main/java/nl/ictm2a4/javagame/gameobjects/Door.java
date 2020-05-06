package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.awt.*;

public class Door extends GameObject {
    private int doorCode;

    public boolean open = false;

    public Door(int gridX, int gridY, int doorCode){
        super(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight, LevelLoader.gridWidth, LevelLoader.gridHeight);

        this.doorCode = doorCode;
        setCollidable(!open);
    }

    public int getDoorCode(){
        return doorCode;
    }

    @Override
    public void draw(Graphics g) {
        if(!open){
            g.drawImage(FileLoader.getInstance().getDoorImage(doorCode < 3 ? doorCode : 0),
                    getX(), getY(),
                    LevelLoader.getInstance().getCurrentLevel().get());
        }
    }

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player && !open){
            Player player = (Player) gameObject;
            if(player.inventoryHasKey(doorCode)){
                setOpen();
            }
        }

        return result;
    }

    public void setOpen(){
        this.open = true;
        setCollidable(false);
    }

    public void setClosed(){
        this.open = false;
        setCollidable(true);
    }
}
