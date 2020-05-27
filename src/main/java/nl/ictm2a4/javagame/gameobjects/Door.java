package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.DoorOpenedEvent;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.LevelEditor;
import nl.ictm2a4.javagame.screens.PlaceDoorLeverKeyDialog;

import java.awt.*;
import java.util.Optional;

public class Door extends GameObject {

    public boolean open = false;

    @JSONLoader(JSONString = "doors", withExtra = true)
    public Door(Integer gridX, Integer gridY, Integer doorCode){
        super(gridX * LevelLoader.GRIDWIDTH, gridY * LevelLoader.GRIDHEIGHT, LevelLoader.GRIDWIDTH, LevelLoader.GRIDHEIGHT);

        // set extra as doorcode
        setExtra(doorCode);
        setCollidable(!open);
        setyIndex(4);
    }

    @Override
    public void draw(Graphics g) {
        if(!open){
            g.drawImage(FileLoader.getInstance().getDoorImage(getExtra() <= 3 ? getExtra() : 0),
                    getX(), getY(),
                    LevelLoader.getInstance().getCurrentLevel().get());
        }
    }

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player && !open){
            Player player = (Player) gameObject;
            Optional<Pickup> key = player.getFromInventory(getExtra());
            if(key.isPresent()) {
                setOpen();
                player.removeFromInventory(key.get());
                EventManager.getInstance().callEvent(new DoorOpenedEvent(getExtra()));
            }
        }

        return result;
    }

    public void setOpen(){
        this.open = true;
        setCollidable(false);
    }

    @Deprecated
    public void setClosed(){
        this.open = false;
        setCollidable(true);
    }

    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(Door.class, FileLoader.getInstance().getDoorImage(0)) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                var doorDialog = new PlaceDoorLeverKeyDialog(GameScreen.getInstance());

                if(doorDialog.isConfirmed()){
                    LevelEditor.getInstance().getLevel().addGameObject(new Door(gridX, gridY, doorDialog.getDoorCode()));
                }
            }
        }.setRequireGround(true);
    }
}
