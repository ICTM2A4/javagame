package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.TeleportingEvent;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.screens.LevelEditor;

import java.awt.*;
import java.util.*;
import java.util.stream.Stream;

public class TeleportationStone extends GameObject {
    private long prevTeleport;
    private boolean teleported;

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
     * Override the GameObject checkCollideSingle
     * @param gameObject GameObject which is colliding
     * @return result of isColliding check
     */
    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player && !teleported) {
            Level level = LevelLoader.getInstance().getCurrentLevel().get();

            Optional<TeleportationStone> stream = level.getGameObjects().stream()
                .filter(gObject -> gObject instanceof TeleportationStone && (gObject.getX() != this.getX() || gObject.getY() != this.getY())).map(c -> (TeleportationStone)c).findFirst();

            if (stream.isPresent() && level.getPlayer().isPresent()) {
                TeleportationStone stone = stream.get();

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        level.getPlayer().get().setX(stone.getX());
                        level.getPlayer().get().setY(stone.getY());
                    }
                }, 5);

                stone.setTeleported(true);
                this.setTeleported(true);

                EventManager.getInstance().callEvent(new TeleportingEvent(this));
            }
        }

        return false;
    }

    public void tick() {
        if (prevTeleport + 1000 <= System.currentTimeMillis() && teleported)
            teleported = false;

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

    public void setTeleported(boolean teleported) {
        this.teleported = teleported;
        prevTeleport = System.currentTimeMillis();
    }

    public boolean getTeleported() {
        return this.teleported;
    }
}
