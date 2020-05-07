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
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public class LevelLoader {

    private final Path CUSTOMLEVELSPATH = Path.of((new JFileChooser().getFileSystemView().getDefaultDirectory().toPath() + File.separator + GameScreen.GAMENAME).replaceAll("%20", " "));
    public static final int DEFAULTLEVELAMOUNT = 10;

    private static LevelLoader instance;

    public static final int GRIDWIDTH = 32;
    public static final int GRIDHEIGHT = 32;
    public static final int WIDTH = 1184;
    public static final int HEIGHT = 640;

    private boolean paused;
    private Level currentLevel;

    public LevelLoader() {
        instance = this;
        paused = true;

        File gameFolder = new File(CUSTOMLEVELSPATH.toUri());
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
        loadLevel(currentLevel.getId());
        currentLevel.setRenderShadows(true);
        currentLevel.loadLevel();
        GameScreen.getInstance().setPanel(currentLevel);
        paused = false;
        return true;
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
            System.out.println("tick");
        }
    }

    /**
     * Resume the current level and request focus on the JPanel
     */
    public void resume() {
        GameScreen.getInstance().requestFocus();
        paused = false;
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

    private File checkFolders() {
        File customLevelsFolder = new File((CUSTOMLEVELSPATH + File.separator + "customlevels").replaceAll("%20", ""));

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

    public boolean removeCustomLevelFile(int id) {
        File customLevelsFolder = checkFolders();
        File file = new File(customLevelsFolder.getPath() + File.separator + "level-"+id+".json");
        return file.delete();
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

        if (id < LevelLoader.DEFAULTLEVELAMOUNT) {
            try (InputStream is = this.getClass().getResourceAsStream("/levels/level-" + id + ".json")) {
                Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8);
                Object object = parser.parse(rd);
                levelOjbect = Optional.of((JSONObject) object);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            File customLevelsFolder = new File((CUSTOMLEVELSPATH + File.separator + "customlevels").replaceAll("%20", ""));
            File file = new File(customLevelsFolder.getPath() + File.separator + "level-" + id + ".json");

            try (InputStream is = new FileInputStream(file)) {
                Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8);
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
        return DEFAULTLEVELAMOUNT + Objects.requireNonNull(customLevelFolder.listFiles()).length;
    }

    /**
     * Create a new custom level
     */
    public void createCustomLevel() {
        LevelLoader.getInstance().loadLevel(LevelLoader.getInstance().getNewLevelId());
    }
}
