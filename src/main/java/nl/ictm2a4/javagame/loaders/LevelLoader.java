package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.screens.MainMenu;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.Level;

import java.util.Optional;

public class LevelLoader implements Runnable {

    private static LevelLoader instance;

    private Thread thread;
    private  boolean isRunning = false;
    private int FPS = 30;
    private long targetTime = 1000 / FPS;

    public static final int width = 640;
    public static final int height = 480;
    public static final int gridWidth = 32;
    public static final int gridHeight = 32;

    private Level currentLevel;

    public LevelLoader() {
        instance = this;
    }

    /**
     * Load a level by it's id
     * @param id ID of the level
     */
    public void loadLevel(int id) {
        currentLevel = new Level(id);
    }

    /**
     * Start a level by it's id, and load the level
     * @param id ID of the level
     * @return true if the level could be started, false if the level could not be started
     */
    public boolean startLevel(int id) {
        loadLevel(id);
        return startLevel();
    }

    /**
     * Start the current loaded level
     * @return true if the level could be started, false if the level could not be started
     */
    public boolean startLevel() {
        if (currentLevel == null) return false;
        GameScreen.getInstance().setPanel(currentLevel, currentLevel.getName());
        start();
        return true;
    }

    /**
     * Stop the current level
     */
    public void stopLevel() {
        GameScreen.getInstance().setPanel(new MainMenu());
        stop();
    }

    @Override
    public void run() {
        long start, elapsed, wait;
        while(isRunning) {
            start = System.nanoTime();

            tick();

            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;

            if(wait <= 0) {
                wait = 5;
            }

            try {
                Thread.sleep(wait);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void tick() {
        currentLevel.tick();
    }

    /**
     * Start the game thread
     */
    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stop the thread
     */
    public void stop() {
        isRunning = false;
        thread = null;
    }

    public void resume() {
        start();
        GameScreen.getInstance().requestFocus();
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
}
