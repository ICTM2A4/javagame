package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.LevelEditor;

import java.awt.*;

public class Sword extends Pickup {

    @JSONLoader(JSONString = "swords")
    public Sword(Integer gridX, Integer gridY) {
        super(gridX, gridY, FileLoader.getInstance().getSwordImageSingle());
        setCollidable(false);
        setyIndex(4);
        setDisplayInInventory(false);
    }

    @Override
    public void draw(Graphics g) {

        if(active && LevelLoader.getInstance().getCurrentLevel().isPresent()){
            g.drawImage(FileLoader.getInstance().getSwordImageSingle(),
                getX() + 4, getY(),
                LevelLoader.getInstance().getCurrentLevel().get());
        }
    }

    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(Sword.class, FileLoader.getInstance().getSwordImageSingle()) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                LevelEditor.getInstance().getLevel().addGameObject(new Sword(gridX, gridY));
            }
        }.setRequireGround(true);
    }

}
