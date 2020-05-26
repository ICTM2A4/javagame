package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.raspberrypi.RaspberryPIController;
import nl.ictm2a4.javagame.screens.EndScreen;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.screens.LevelEditor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class TeleportationStone extends GameObject {
    private long press;

    @JSONLoader(JSONString = "teleportationStones")
    public TeleportationStone(Integer gridX, Integer gridY) {
        super(
                gridX * LevelLoader.GRIDWIDTH,
                gridY * LevelLoader.GRIDHEIGHT,
                16,16);
        setyIndex(9);
        setCollidable(false);
    }

    /**
     * Override the GameObject checkCollideSingle, test if player collided with the endpoint
     * @param gameObject GameObject which is colliding
     * @return result of isColliding check
     */
    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);
        return (result && gameObject instanceof Player);
    }

    public void tick() {
        GameObject[] collisions = checkCollideAllGameObjects(getX(), getY());
        List<Integer> pressedKeys = GameScreen.getInstance().getPressedKeys();
        String rpiButton = RaspberryPIController.getInstance().getPressedButton();

        if(press + 5000 <= System.currentTimeMillis() &&
                Arrays.stream(collisions).anyMatch(gameObject -> gameObject instanceof Player) && (pressedKeys.contains(KeyEvent.VK_SPACE)  || rpiButton.equals("middle"))){
            Level level = LevelEditor.getInstance().getLevel();

            Optional<GameObject> stream = level.getGameObjects().stream()
                    .filter(gObject -> gObject instanceof TeleportationStone && (gObject.getX() != this.getX() || gObject.getY() != this.getY())).findFirst();
                if (stream.isPresent() && level.getPlayer().isPresent()) {
                    GameObject stone = stream.get();
                    level.getPlayer().get().setX(stone.getX());
                    level.getPlayer().get().setY(stone.getY());
                    press = System.currentTimeMillis();
                }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getTeleportationStoneImage(),
                getX() ,getY(),
                LevelLoader.getInstance().getCurrentLevel().get());
    }

    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(TeleportationStone.class, FileLoader.getInstance().getTeleportationStoneImage()) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                Level level = LevelEditor.getInstance().getLevel();
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                Stream<GameObject> stream = level.getGameObjects().stream().filter(gameObject -> gameObject instanceof TeleportationStone);
                if (stream.toArray().length > 1) {
                    level.removeGameObject(level.getGameObjects().stream().filter(gameObject -> gameObject instanceof TeleportationStone).findFirst().get());
                }
                level.addGameObject(new TeleportationStone(gridX, gridY));
            }
        }.setRequireGround(true);
    }
}
