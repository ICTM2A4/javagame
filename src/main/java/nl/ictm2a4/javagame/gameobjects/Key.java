package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.LevelEditor;
import nl.ictm2a4.javagame.screens.PlaceDoorLeverKeyDialog;

import java.awt.*;

public class Key extends Pickup {

    // extra = doorCode

    @JSONLoader(JSONString = "keys", withExtra = true)
    public Key(Integer gridX, Integer gridY, Integer doorCode){
        super(gridX, gridY, FileLoader.getInstance().getKeyImage(doorCode));

        // set extra as doorCode
        setExtra(doorCode);
        setCollidable(false);
        setyIndex(4);
    }

    @Override
    public void draw(Graphics g) {

        if(active && LevelLoader.getInstance().getCurrentLevel().isPresent()){
            g.drawImage(FileLoader.getInstance().getKeyImage(getExtra() <= 3 ? getExtra() : 0),
                    getX(), getY(),
                    LevelLoader.getInstance().getCurrentLevel().get());
        }
    }

    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(Key.class, FileLoader.getInstance().getKeyImage(0)) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                var keyDialog = new PlaceDoorLeverKeyDialog(GameScreen.getInstance());

                if(keyDialog.isConfirmed()){
                    LevelEditor.getInstance().getLevel().addGameObject(new Key(gridX, gridY, keyDialog.getDoorCode()));
                }
            }
        }.setRequireGround(true);
    }
}
