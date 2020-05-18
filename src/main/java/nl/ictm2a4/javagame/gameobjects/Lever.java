package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.DoorOpenedEvent;
import nl.ictm2a4.javagame.cevents.LeverSwitchEvent;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.raspberrypi.RaspberryPIController;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.LevelEditor;
import nl.ictm2a4.javagame.screens.PlaceDoorLeverKeyDialog;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class Lever extends GameObject {
    private boolean active;

    @JSONLoader(JSONString = "levers", withExtra = true)
    public Lever(Integer gridX, Integer gridY, Integer doorCode) {
        super(gridX * LevelLoader.GRIDWIDTH, gridY * LevelLoader.GRIDHEIGHT, LevelLoader.GRIDWIDTH, LevelLoader.GRIDHEIGHT);
        setCollidable(false);

        // set extra as doorCode
        setExtra(doorCode);
        setyIndex(4);
    }

    @Override
    public void draw(Graphics g) {
        if(!active){
            g.drawImage(FileLoader.getInstance().getLeverImage(getExtra() <= 3 ? getExtra() : 0),
                    getX(), getY(),
                    32, 32,
                    LevelLoader.getInstance().getCurrentLevel().get());
        } else {
            g.drawImage(FileLoader.getInstance().getLeverImage(getExtra() <= 3 ? getExtra() : 0),
                    getX() + 32, getY(),
                    -32, 32,
                    LevelLoader.getInstance().getCurrentLevel().get());
        }

    }

    private void activate(){
        EventManager.getInstance().callEvent(new LeverSwitchEvent(getExtra()));

        for(Door door : LevelLoader.getInstance().getCurrentLevel().get().getGameObjects().stream()
                .filter(gameObject -> gameObject instanceof Door)
                .filter(gameObject -> gameObject.getExtra() == getExtra())
                .toArray(Door[]::new)){
            door.setOpen();
            EventManager.getInstance().callEvent(new DoorOpenedEvent(getExtra()));
        }

        active = true;
    };

    @Override
    public void tick() {
        GameObject[] collisions = checkCollideAllGameObjects(getX(), getY());
        List<Integer> pressedKeys = GameScreen.getInstance().getPressedKeys();
        String rpiButton = RaspberryPIController.getInstance().getPressedButton();

        if(!active && Arrays.stream(collisions).anyMatch(gameObject -> gameObject instanceof Player) && (pressedKeys.contains(KeyEvent.VK_SPACE)  || rpiButton.equals("middle"))){
            activate();
        }
    }

    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(Lever.class, FileLoader.getInstance().getLeverImage(0)) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                var leverDialog = new PlaceDoorLeverKeyDialog(GameScreen.getInstance());

                if(leverDialog.isConfirmed()){
                    LevelEditor.getInstance().getLevel().addGameObject(new Lever(gridX, gridY, leverDialog.getDoorCode()));
                }
            }
        }.setRequireGround(true);
    }
}
