package nl.ictm2a4.javagame.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

import nl.ictm2a4.javagame.cevents.ItemPickupEvent;
import nl.ictm2a4.javagame.cevents.PlayerDiedEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.event.Listener;
import nl.ictm2a4.javagame.gameobjects.GameObject;
import nl.ictm2a4.javagame.gameobjects.*;
import nl.ictm2a4.javagame.loaders.GameObjectsLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.services.levels.LevelService;
import nl.ictm2a4.javagame.uicomponents.HUD;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level extends JPanel implements Listener {

    private int id;
    private ArrayList<GameObject> gameObjects;
    private String name;
    private BufferedImage shadow;
    private boolean renderShadows, animateLights;
    private Optional<JSONObject> object;
    private long score;
    private long start;

    public Level(int id) {
        this.id = id;
        gameObjects = new ArrayList<>();
        setBackground(Color.black);

        setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(LevelLoader.WIDTH, LevelLoader.HEIGHT));

        try {
            loadLevel();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        generateWalls();

        animateLights = true;

        EventManager.getInstance().registerListener(this);
        shadow = new BufferedImage(LevelLoader.WIDTH,LevelLoader.HEIGHT,BufferedImage.TYPE_INT_ARGB);
        setVisible(true);
    }

    public int getId() {
        return id;
    }

    public long getScore() {
        return score;
    }

    /**
     * Load the JSONObject from the level file
     */
    public void loadObject() {
        // loading custom level
        if (id > LevelLoader.DEFAULTLEVELAMOUNT) {
            var dbLevel = GameScreen.getInstance().getCustomLevel(this.id);
            JSONParser parser = new JSONParser();

            if (dbLevel == null) {
                object = Optional.of(new JSONObject());
            } else {
                try {
                    object = Optional.of((JSONObject)parser.parse(dbLevel.getContent()));
                    return;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        object = LevelLoader.getInstance().getLevelObject(id);
    }

    /**
     * Get all loaded GameObjects in the Level
     * 
     * @return ArrayList of GameObjects
     */
    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void clearGameObjects() {
        this.gameObjects = new ArrayList<>();
    }
    /**
     * Render the level
     * 
     * @param g Graphics of the JPanel
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        getGameObjects().stream().sorted(Comparator.comparingInt(GameObject::getyIndex))
                .forEach(object -> object.draw(g));


        if (this.renderShadows) {
            g.setColor(new Color(255, 153, 51, 40));
            g.fillRect(0, 0, getWidth(), getHeight());
            drawLights();
            g.drawImage(shadow, 0, 0, null);
        }
    }

    /**
     * Create a lightmap and draw all light cirlces on the map
     */
    private void drawLights() {
        Graphics2D g = shadow.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        getPlayer().ifPresent(value -> drawLight(g, new Point(value.getX() + (value.getWidth() / 2), value.getY() + (value.getHeight() / 2) - 24),
            50));

        getGameObjects().stream().filter(gameObject -> gameObject instanceof Torch).forEach(torch -> drawLight(g,
                new Point(torch.getX() + (torch.getWidth() / 2), torch.getY() + (torch.getHeight() / 2)), 50));

        getGameObjects().stream().filter(gameObject -> gameObject instanceof EndPoint).forEach(endPoint ->
            drawLight(g, new Point(endPoint.getX() + (endPoint.getWidth() / 2), endPoint.getY() + (endPoint.getHeight() / 2)), 12));

        g.dispose();
    }

    /**
     * Draw a light circle on the lightmap
     * 
     * @param g2d    Graphics2D of the JPanel
     * @param center The centre point of the light circle
     * @param radius Radius of the light cirlce
     */
    private void drawLight(Graphics2D g2d, Point center, int radius) {
        if (animateLights) {
            int min = (radius - 2);
            int max = (radius + 6);
            radius = new Random().nextInt((max - min) + 1) + min;
        }

        g2d.setComposite(AlphaComposite.DstOut);
        float[] dist = { 0.0f, 1.0f };
        Color[] colors = new Color[] { Color.WHITE, new Color(0, 0, 0, 0) };
        Point2D pt = new Point2D.Float(center.x, center.y);

        RadialGradientPaint paint = new RadialGradientPaint(pt, radius, dist, colors,
                MultipleGradientPaint.CycleMethod.NO_CYCLE);
        g2d.setPaint(paint);
        g2d.fillOval((int) pt.getX() - radius, (int) pt.getY() - radius, radius * 2, radius * 2);
    }

    /**
     * Add a GameObject to the level
     * 
     * @param gameObject GameObject to add
     */
    public void addGameObject(GameObject gameObject) {
        if (!this.gameObjects.contains(gameObject))
            this.gameObjects.add(gameObject);
    }

    /**
     * Remove a GameObject to the level
     * 
     * @param gameObject GameObject to remove
     */
    public void removeGameObject(GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    /**
     * Get the name of the level
     * 
     * @return Levelname as String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the level name
     * 
     * @param name levelname to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setAnimateLights(boolean animateLights) {
        this.animateLights = animateLights;
    }

    private Constructor getConstructor(Class<? extends GameObject> object) throws NoSuchMethodException {
        for (Constructor con : object.getConstructors()) {
            JSONLoader jl = (JSONLoader) con.getAnnotation(JSONLoader.class);

            if (jl != null) {
                if (jl.withExtra()) {
                    return object.getConstructor(Integer.class, Integer.class, Integer.class);
                }
                else {
                    return object.getConstructor(Integer.class, Integer.class);
                }
            }
        }
        return null;
    }

    /**
     * Load the current level from it's JSON file
     */
    public void loadLevel() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        clearGameObjects();
        loadObject();

        if (object.isEmpty())
            return;

        JSONObject levelObject = object.get();

        // Read level name
        name = levelObject.getOrDefault("name", "undefined").toString();

        for(Class<? extends GameObject> object : GameObjectsLoader.getInstance().getObjectList()) {
            Constructor constructor = getConstructor(object);
            JSONLoader jl = (JSONLoader) constructor.getAnnotation(JSONLoader.class);

            if (jl == null || constructor == null || levelObject.get(jl.JSONString()) == null)
                continue;

            JSONArray array = (JSONArray) levelObject.get(jl.JSONString());
            if (array == null) continue;
            for(Object arrayItem : array) {
                JSONArray coords = (JSONArray) arrayItem;

                int x = Integer.parseInt(coords.get(0).toString());
                int y = Integer.parseInt(coords.get(1).toString());

                if (jl.withExtra()) {
                    int type = Integer.parseInt(coords.get(2).toString());
                    addGameObject((GameObject) constructor.newInstance(new Object[] {x, y, type}));
                }
                else {
                    addGameObject((GameObject) constructor.newInstance(new Object[] {x, y}));
                }

            }
        }

        regenerateWalls();
    }

    /**
     * Covert the level to a .JSON file
     */
    public boolean saveLevel() throws NoSuchMethodException {
        JSONObject saveObject = new JSONObject();
        saveObject.put("name", this.getName());

        for(Class<? extends GameObject> object : GameObjectsLoader.getInstance().getObjectList()) {
            Constructor constructor = getConstructor(object);
            JSONLoader jl = (JSONLoader) constructor.getAnnotation(JSONLoader.class);

            if (jl == null || constructor == null)
                continue;

            Stream<GameObject> stream = getGameObjects().stream().filter(gameObject -> gameObject.getClass().isAssignableFrom(object));
            JSONArray arrayTiles = new JSONArray();

            for(GameObject tile : stream.toArray(GameObject[]::new)) {
                JSONArray itemArray = new JSONArray();
                itemArray.add(tile.getX() / LevelLoader.GRIDWIDTH);
                itemArray.add(tile.getY() / LevelLoader.GRIDHEIGHT);
                if (jl.withExtra()) {
                    itemArray.add(tile.getExtra());
                }
                arrayTiles.add(itemArray);
            }

            if (arrayTiles.size() > 0)
                saveObject.put(jl.JSONString(), arrayTiles);
        }

        var gameScreen = GameScreen.getInstance();
        var levelService = new LevelService();

        gameScreen.refreshCustomLevels();

        var dbLevel = gameScreen.getCustomLevel(this.id);
        var currentUser = GameScreen.getInstance().getCurrentUser();

        if (dbLevel == null) {
            if(currentUser.isPresent()){
                dbLevel = new nl.ictm2a4.javagame.services.levels.Level(this.id, this.getName(), "", saveObject.toString(), currentUser.get().getId(), "");
            }

            GameScreen.getInstance().addCustomLevel(dbLevel);
            return (levelService.addLevel(dbLevel) != null);
        }

        dbLevel.setName(name);
        dbLevel.setContent(saveObject.toString());

        gameScreen.updateCustomLevel(dbLevel.getId(), dbLevel);
        return levelService.updateLevel(dbLevel);
    }

    /**
     * Set if the level should render dark overlay with light circles
     * 
     * @param renderShadows Value of rendering shadows
     */
    public void setRenderShadows(boolean renderShadows) {
        this.renderShadows = renderShadows;
    }

    /**
     * Generate the walls based on the groundTiles
     *
     * Loop over all ground tiles and check if the 8 positions around the ground
     * tile are occupied, if empty generate wall
     */
    private void generateWalls() {
        Ground[] groundTiles = getGameObjects().stream().filter(object -> (object instanceof Ground))
                .map(g -> (Ground)g)
                .toArray(Ground[]::new);

        for(Ground ground : groundTiles) {
            int x = ground.getX() / LevelLoader.GRIDWIDTH;
            int y = ground.getY() / LevelLoader.GRIDHEIGHT;

            for (int _x = x - 1; _x <= x + 1; _x++) {
                for (int _y = y - 1; _y <= y + 1; _y++) {
                    if (_x == x && _y == y)
                        continue;

                    if (fromCoordsToArray(_x * LevelLoader.GRIDWIDTH, _y * LevelLoader.GRIDHEIGHT)
                        .filter(gameObject -> gameObject instanceof Wall || gameObject instanceof Ground).findAny().isEmpty())
                        addGameObject(new Wall(_x,_y));
                }
            }
        }
        ;
    }

    /**
     * Regenerate all walls - remove all walls - create new walls
     */
    public void regenerateWalls() {
        Wall[] walls = getGameObjects().stream().filter(gameObject -> gameObject instanceof Wall).map(w -> (Wall)w).toArray(Wall[]::new);
        for (Wall wall : walls)
            getGameObjects().remove(wall);
        generateWalls();
    }

    /**
     * Find a list of GameObjects based on the x, y
     * 
     * @param x x to search by
     * @param y y to search by
     * @return Stream of GameObjects where x, y are within the boundries
     */
    public Stream<GameObject> fromCoordsToArray(int x, int y) {
        return Arrays.stream(getGameObjects().stream()
                .filter(gameObject -> (gameObject.getX() <= x && gameObject.getY() <= y
                        && gameObject.getX() + gameObject.getWidth() > x
                        && gameObject.getY() + gameObject.getHeight() > y))
                .toArray(GameObject[]::new));
    }

    /**
     * Tick the level and all it's GameObjects
     */
    public void tick() {
        repaint();
        getGameObjects().forEach(GameObject::tick);
        HUD.getInstance().tick();
        score++; //= System.currentTimeMillis() - current;

        if(start + 500 <= System.currentTimeMillis() && GameScreen.getInstance().getPressedKeys().contains(KeyEvent.VK_ESCAPE)) {
            LevelLoader.getInstance().pause();
            GameScreen.getInstance().addOverlay(new PauseScreen());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDiedEvent event) {
        GameScreen.getInstance().addOverlay(new GameOverScreen());
        LevelLoader.getInstance().pause();

    }

    @EventHandler
    public void onItemPickup(ItemPickupEvent event) {
        if (event.getPickup() instanceof Sword && getPlayer().isPresent())
            getPlayer().get().setDamage(20);
    }

    /**
     * Restart the current level
     */
    public void restart() {
        LevelLoader.getInstance().startLevel();
    }


    /**
     * get the player
     * @return stream of gameobjects that are instance of player
     */
    public Optional<Player> getPlayer() {
        return getGameObjects().stream().filter(gameObject -> gameObject instanceof Player).map(p -> (Player)p).findFirst();
    }

    /**
     * resets the score in levels
     */
    public void resetStart() {
        start = System.currentTimeMillis();
    }

}