package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.ThroughFakeWallEvent;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.LevelEditor;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FakeWall extends GameObject {

    private final int EVENTINTERVAL = 10;
    private int tick;

    @JSONLoader(JSONString = "fakeWalls")
    public FakeWall(Integer gridX, Integer gridY) {
        super(gridX * LevelLoader.GRIDWIDTH, gridY * LevelLoader.GRIDHEIGHT, LevelLoader.GRIDWIDTH, LevelLoader.GRIDHEIGHT);
        setyIndex(2);
    }

    /**
     * Check if something is colliding with the FakeWall
     * If a player is colliding return false (so the player can pass) any other object must collide
     * @param gameObject The other game object against which the collision will be checked
     * @param x the X coordinate of the position at which the collision will be checked
     * @param y the Y coordinate of the position at which the collision will be checked
     * @return If there is a collision between this object and the other game object at the given coordinates
     */
    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject, x, y);

        tick++;
        if (tick >= EVENTINTERVAL) {
            tick = 0;
            if (result && gameObject instanceof Player)
                EventManager.getInstance().callEvent(new ThroughFakeWallEvent(this));
        }

        if (gameObject instanceof Player)
            return false;

        return result;
    }

    @Override
    public void draw(Graphics g) {
        if (LevelLoader.getInstance().getCurrentLevel().isEmpty())
            return;

        g.drawImage(FileLoader.getInstance().getWallTile(connectedFacesSum(this.hasConnectedFaces(Wall.class))), getX(), getY(),
            LevelLoader.getInstance().getCurrentLevel().get());

        g.drawImage(FileLoader.getInstance().getFakeWallOverlay(),
            getX() + 2, getY() + 2,
            LevelLoader.getInstance().getCurrentLevel().get());
    }

    /**
     * Get the LevelEditorSpecs for FakeWall
     * @return LevelEditorSpecs
     */
    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        BufferedImage image = new BufferedImage(32,32,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        g.drawImage(FileLoader.getInstance().getWallTile(0), 0, 0, null);
        g.drawImage(FileLoader.getInstance().getFakeWallOverlay(), 2, 2, null);

        return new LevelEditor.LevelEditorItem(FakeWall.class, image) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                LevelEditor.getInstance().getLevel().addGameObject(new FakeWall(gridX, gridY));
            }
        }.setRequireGround(true);
    }

}
