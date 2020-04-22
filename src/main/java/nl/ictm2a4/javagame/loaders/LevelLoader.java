package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.screens.MainMenu;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.io.*;
import java.nio.file.Path;
import java.util.Optional;

public class LevelLoader implements Runnable {

    private final Path customLevelsPath = Path.of((new JFileChooser().getFileSystemView().getDefaultDirectory().toPath() + File.separator + GameScreen.gameName).replaceAll("%20", " "));
    public static final int defaultLevelAmount = 3;

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

        File gameFolder = new File(customLevelsPath.toUri());
        if (!gameFolder.exists()) {
            gameFolder.mkdir();
        }
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
        currentLevel.setRenderShadows(true);
        currentLevel.loadLevel();
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

    /**
     * Tick the current level
     */
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

    /**
     * Resume the current level and request focus on the JPanel
     */
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

    private File checkFolders() {
        File customLevelsFolder = new File((customLevelsPath + File.separator + "customlevels").replaceAll("%20", ""));

        if (!customLevelsFolder.exists())
            customLevelsFolder.mkdir();

        return customLevelsFolder;
    }

    /**
     * Create a custom level file
     * @param id The id of the level
     * @return The created file
     * @throws IOException
     */
    public File createCustomLevelFile(int id) throws IOException {

        File customLevelsFolder = checkFolders();

        File file = new File(customLevelsFolder.getPath() + File.separator + "level-"+id+".json");
        file.createNewFile();


        JSONObject object = new JSONObject();
        object.put("name", "");
        object.put("ground", new JSONArray());

        try(FileWriter writer = new FileWriter(file)) {
            writer.write(object.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    /**
     * Get a custom level file from the filesystem
     * @param id The id of the level
     * @return Get the custom level file
     * @throws IOException
     */
    public File getCustomLevelFile(int id) throws IOException {
        File customLevelsFolder = checkFolders();
        File file = new File(customLevelsFolder.getPath() + File.separator + "level-"+id+".json");
        if (!file.exists())
            return createCustomLevelFile(id);
        else
            return file;
    }

    /**
     * Return a level JSON object from default levels or custom level
     * @param id The id of the level
     * @return Optional JSON object
     */
    public Optional<JSONObject> getLevelObject(int id) {
        JSONParser parser = new JSONParser();
        Optional<JSONObject> levelOjbect = Optional.empty();

        if (id < LevelLoader.defaultLevelAmount) {
            try (InputStream is = this.getClass().getResourceAsStream("/levels/level-" + id + ".json")) {
                Reader rd = new InputStreamReader(is, "UTF-8");
                Object object = parser.parse(rd);
                levelOjbect = Optional.of((JSONObject) object);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            File customLevelsFolder = new File((customLevelsPath + File.separator + "customlevels").replaceAll("%20", ""));
            File file = new File(customLevelsFolder.getPath() + File.separator + "level-" + id + ".json");

            try (InputStream is = new FileInputStream(file)) {
                Reader rd = new InputStreamReader(is, "UTF-8");
                Object object = parser.parse(rd);
                levelOjbect = Optional.of((JSONObject) object);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        return levelOjbect;
    }

    /**
     * Get a new level id
     * @return Integer of new level id
     */
    public int getNewLevelId() {
        File customLevelFolder = checkFolders();
        return defaultLevelAmount + customLevelFolder.listFiles().length;
    }

    /**
     * Create a new custom level
     */
    public void createCustomLevel() {
        LevelLoader.getInstance().loadLevel(LevelLoader.getInstance().getNewLevelId());
    }
}
