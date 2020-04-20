package nl.ictm2a4.javagame.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import nl.ictm2a4.javagame.gameobjects.GameObject;
import nl.ictm2a4.javagame.gameobjects.*;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level extends JPanel {

    private int id;
    private ArrayList<GameObject> gameObjects;
    private String name;
    private Player player;

    public Level(int id) {
        this.id = id;
        gameObjects = new ArrayList<>();
        setBackground(Color.black);

        this.setPreferredSize(new Dimension(LevelLoader.width, LevelLoader.height));

        loadLevel();
        generateWalls();
    }

    /**
     * Get all loaded GameObjects in the Level
     * @return ArrayList of GameObjects
     */
    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        getGameObjects().stream().sorted(Comparator.comparingInt(GameObject::getyIndex)).forEach(object -> object.draw(g));
    }

    /**
     * Add a GameObject to the level
     * @param gameObject GameObject to add
     */
    public void addCollidable(GameObject gameObject) {
        if (!this.gameObjects.contains(gameObject))
            this.gameObjects.add(gameObject);
    }

    /**
     * Remove a GameObject to the level
     * @param gameObject GameObject to remove
     */
    public void removeCollidable(GameObject gameObject) {
        if (this.gameObjects.contains(gameObject))
            this.gameObjects.remove(gameObject);
    }

    /**
     * Get the name of the level
     * @return Levelname as String
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Load the current level from it's JSON file
     */
    public void loadLevel() {
        JSONParser parser = new JSONParser();

        try (InputStream is = this.getClass().getResourceAsStream("/levels/level-" + id + ".json")) {
            Reader rd = new InputStreamReader(is, "UTF-8");
            Object object = parser.parse(rd);
            JSONObject levelOjbect = (JSONObject) object;

            // Read level name
            name = levelOjbect.get("name").toString();

            // Read all ground tiles
            JSONArray groundTiles = (JSONArray) levelOjbect.get("ground");
            for(Object ground : groundTiles) {
                JSONArray coords = (JSONArray) ground;
                int x = Integer.parseInt(coords.get(0).toString());
                int y = Integer.parseInt(coords.get(1).toString());
                addCollidable(new Ground(x,y));
            }

            // Read endpoint
            JSONArray endpoint = (JSONArray) levelOjbect.get("endpoint");
            if (endpoint != null) {
                int endX = Integer.parseInt(endpoint.get(0).toString());
                int endY = Integer.parseInt(endpoint.get(1).toString());
                addCollidable(new EndPoint(endX, endY));
            }

            // Read player startpoint
            JSONArray playerpoint = (JSONArray) levelOjbect.get("player");
            if (playerpoint != null) {
                int playerX = Integer.parseInt(playerpoint.get(0).toString());
                int playerY = Integer.parseInt(playerpoint.get(1).toString());
                player = new Player(playerX, playerY);
                addCollidable(player);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void saveLevel() {
        JSONObject object = new JSONObject();
        object.put("name", "test");

        JSONArray groundTiles = new JSONArray();
        for(Ground ground : getGameObjects().stream().filter(gameObject -> gameObject instanceof Ground).toArray(Ground[]::new)) {
            JSONArray groundArray = new JSONArray();
            groundArray.add(ground.getX() / LevelLoader.gridWidth);
            groundArray.add(ground.getY() / LevelLoader.gridHeight);
            groundTiles.add(groundArray);
        }
        object.put("ground",groundTiles);

        JSONArray player = new JSONArray();
        Optional<GameObject> oPlayer = getGameObjects().stream().filter(gameObject -> gameObject instanceof Player).findFirst();
        if (oPlayer.isPresent()) {
            player.add(oPlayer.get().getX() / LevelLoader.gridWidth);
            player.add(oPlayer.get().getY() / LevelLoader.gridHeight);
            object.put("player", player);
        }


        JSONArray endpoint = new JSONArray();
        Optional<GameObject> end = getGameObjects().stream().filter(gameObject -> gameObject instanceof EndPoint).findFirst();
        if (end.isPresent()) {
            endpoint.add(end.get().getX() / LevelLoader.gridWidth);
            endpoint.add(end.get().getY() / LevelLoader.gridHeight);
            object.put("endpoint", endpoint);
        }

        URL url = getClass().getResource("/levels/level-" + id + ".json");
        File file = new File(url.getFile());

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(object.toJSONString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * Generate the walls based on the groundTiles
     *
     * Loop over all ground tiles and check if the
     * 8 positions around the ground tile are occupied, if empty generate wall
     */
    private void generateWalls() {
        Ground[] groundTiles = getGameObjects().stream().filter(object -> (object instanceof Ground)).toArray(Ground[]::new);

        for(Ground ground : groundTiles) {
            int x = ground.getX() / LevelLoader.gridWidth;
            int y = ground.getY() / LevelLoader.gridHeight;

            for (int _x = x-1; _x <= x+1; _x++) {
                for (int _y = y-1; _y <= y+1; _y++) {
                    if (_x == x && _y == y)
                        continue;

                    if (fromCoords(_x * LevelLoader.gridWidth, _y * LevelLoader.gridHeight).isEmpty())
                        addCollidable(new Wall(_x,_y));
                }
            }
        };
    }

    public void regenerateWalls() {
        Wall[] walls = getGameObjects().stream().filter(gameObject -> gameObject instanceof Wall).toArray(Wall[]::new);
        for (Wall wall : walls)
            getGameObjects().remove(wall);
        generateWalls();
    }

    /**
     * Find a GameObject based on the x and y coordinates
     * @param x x to check for
     * @param y y to check for
     * @return Optional GameObject based on the coords, use #.ifPresent() and #.get() to use
     */
    public Optional<GameObject> fromCoords(int x, int y) {
        return getGameObjects().stream().filter(gameObject ->
            (gameObject.getX() <= x &&
            gameObject.getY() <= y &&
            gameObject.getX() + gameObject.getWidth() > x &&
            gameObject.getY() + gameObject.getHeight() > y)
        ).findAny();
    }

    public Stream<GameObject> fromCoordsToArray(int x, int y) {
        return Arrays.stream(getGameObjects().stream().filter(gameObject ->
            (gameObject.getX() <= x &&
                gameObject.getY() <= y &&
                gameObject.getX() + gameObject.getWidth() > x &&
                gameObject.getY() + gameObject.getHeight() > y)
        ).toArray(GameObject[]::new));
    }

    /**
     * Tick the level and all it's GameObjects
     */
    public void tick() {
        repaint();
        getGameObjects().forEach(GameObject::tick);

        if(GameScreen.getInstance().getPressedKeys().contains(KeyEvent.VK_ESCAPE)) {
            LevelLoader.getInstance().stop();
            GameScreen.getInstance().setPanel(new PauseScreen(), "Paused");
        }
    }
}
