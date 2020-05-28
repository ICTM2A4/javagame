package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.EndScreen;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.screens.LevelEditor;

import java.awt.*;
import java.util.Optional;

public class EndPoint extends GameObject {

    private final int ANIMATEDELAY = 3;
    public static final int IMAGEAMOUNT = 7;

    private int currentImage = 0;
    private int animateCount = 0;

    @JSONLoader(JSONString = "endpoint")
    public EndPoint(Integer gridX, Integer gridY) {
        super(
            gridX * LevelLoader.GRIDWIDTH + ((LevelLoader.GRIDWIDTH - 16) / 2),
            gridY * LevelLoader.GRIDHEIGHT + ((LevelLoader.GRIDHEIGHT - 16) / 2) - 2,
            16,16);
        setyIndex(9);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getCoinImage(currentImage),
            getX(),getY(),
            LevelLoader.getInstance().getCurrentLevel().get());
    }

    /**
     * Update the coin image based on it's current frame
     */
    @Override
    public void tick() {
        animateCount++;
        if (animateCount - ANIMATEDELAY == 0) {
            animateCount = 0;
            currentImage++;
            if (currentImage == IMAGEAMOUNT) currentImage = 0;
        }
    }

    /**
     * Override the GameObject checkCollideSingle, test if player collided with the endpoint
     * @param gameObject GameObject which is colliding
     * @return result of isColliding check
     */
    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result && gameObject instanceof Player) {
            EventManager.getInstance().callEvent(new ReachedEndEvent(LevelLoader.getInstance().getCurrentLevel().get().getId())); // call end reached event
            LevelLoader.getInstance().pause();
            GameScreen.getInstance().addOverlay(new EndScreen());
        }
        return result;
    }

    /**
     * Get the LevelEditorSpecs for EndPoint
     * @return LevelEditorSpecs
     */
    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(EndPoint.class, FileLoader.getInstance().getCoinImage(0)) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                Level level = LevelEditor.getInstance().getLevel();
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                Optional<GameObject> endpoint = level.getGameObjects().stream().filter(gameObject -> gameObject instanceof EndPoint).findFirst();
                endpoint.ifPresent(level::removeGameObject);
                level.addGameObject(new EndPoint(gridX, gridY));
            }
        }.setRequireGround(true);
    }
}
