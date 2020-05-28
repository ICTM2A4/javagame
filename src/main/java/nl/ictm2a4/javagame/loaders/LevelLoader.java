package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.screens.MainMenu;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.services.levels.LevelService;
import nl.ictm2a4.javagame.uicomponents.HUD;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Optional;

public class LevelLoader {

    public static final int DEFAULTLEVELAMOUNT = 10;

    private static LevelLoader instance;

    public static final int GRIDWIDTH = 32;
    public static final int GRIDHEIGHT = 32;
    public static final int WIDTH = 1184;
    public static final int HEIGHT = 640;

    private boolean paused;
    private Level currentLevel;

    /**
     * Create an instance of the LevelLoader
     */
    public LevelLoader() {
        instance = this;
        paused = true;
    }

    /**
     * Load a level by it's id
     * @param id ID of the level
     */
    public void loadLevel(int id) {
        currentLevel = new Level(id);
        HUD.getInstance().reset();
    }

    /**
     * Start a level by it's id, and load the level
     * @param id ID of the level
     */
    public void startLevel(int id) {
        loadLevel(id);
        startLevel();
    }

    /**
     * Start the current loaded level
     */
    public void startLevel() {
        if (currentLevel == null) return;
        loadLevel(currentLevel.getId());
        currentLevel.setRenderShadows(Settings.getInstance().isShowShadows());
        currentLevel.setAnimateLights(Settings.getInstance().isAnimatedLights());
        try {
            currentLevel.loadLevel();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        GameScreen.getInstance().setPanel(currentLevel);
        paused = false;
    }

    /**
     * Stop the current level
     */
    public void stopLevel() {
        GameScreen.getInstance().setPanel(new MainMenu());
    }

    /**
     * Tick the current level
     */
    public void tick() {
        if(!paused) {
            currentLevel.tick();
        }
    }

    /**
     * Resume the current level and request focus on the JPanel
     */
    public void resume() {
        if (getCurrentLevel().isEmpty())
            return;

        GameScreen.getInstance().requestFocus();
        paused = false;
        getCurrentLevel().get().resetStart();
    }

    public void pause() {
        paused = true;
    }

    /**
     * Get the current loaded level
     * @return Optional<Level>, use #.ifPresent() and #.get()
     */
    public Optional<Level> getCurrentLevel() {
        return Optional.ofNullable(currentLevel);
    }

    /**
     * Get the instance of the Level Loader
     * @return LevelLoader instance
     */
    public static LevelLoader getInstance() {
        return instance;
    }

    /**
     * Return a level JSON object from default levels or custom level
     * @param id The id of the level
     * @return Optional JSON object
     */
    public Optional<JSONObject> getLevelObject(int id) {
        JSONParser parser = new JSONParser();
        Optional<JSONObject> levelObject = Optional.empty();

        if (id <= LevelLoader.DEFAULTLEVELAMOUNT) {
            try (InputStream is = this.getClass().getResourceAsStream("/levels/level-" + id + ".json")) {
                Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8);
                Object object = parser.parse(rd);
                levelObject = Optional.of((JSONObject) object);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        return levelObject;
    }

    /**
     * Get the status of the level paused
     * @return Status of paused
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Create a new custom level
     */
    public void createCustomLevel() {
        LevelService service = new LevelService();
        Level level = new Level(service.getLevels().size() + 1);
        LevelLoader.getInstance().loadLevel(level.getId());
    }
}
