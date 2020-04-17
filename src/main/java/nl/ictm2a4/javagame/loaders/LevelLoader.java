package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.Level;
import nl.ictm2a4.javagame.Main;

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

    public void loadLevel(int id) {
        currentLevel = new Level(id);
    }

    public boolean startLevel() {
        if (currentLevel == null) return false;
        Main.screen.setContentPane(currentLevel);
        Main.screen.pack();
        start();
        Main.screen.addTitle(currentLevel.getName());
        return true;
    }

    public void stopLevel() {
        Main.screen.setContentPane(null);
        stop();
        Main.screen.resetTitle();
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

    private void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() {
        isRunning = false;
        thread = null;
    }

    public Optional<Level> getCurrentLevel() {
        return Optional.ofNullable(currentLevel);
    }

    public static LevelLoader getInstance() {
        return instance;
    }
}
