package nl.ictm2a4.javagame.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

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
    private BufferedImage shadow;
    private boolean renderShadows;

    public Level(int id) {
        this.id = id;
        gameObjects = new ArrayList<>();
        setBackground(Color.black);

        this.setPreferredSize(new Dimension(LevelLoader.width, LevelLoader.height));

        loadLevel();
        generateWalls();
        renderShadows = true;
        shadow = new BufferedImage(LevelLoader.width,LevelLoader.height,BufferedImage.TYPE_INT_ARGB);
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
        getGameObjects().forEach(object -> object.draw(g));
        this.player.draw(g);

        if (this.renderShadows) {
            g.setColor(new Color(255,153,51,40));
            g.fillRect(0,0,getWidth(),getHeight());
            drawLights();
            g.drawImage(shadow, 0, 0, null);
        }
    }

    private void drawLights() {
        Graphics2D g = shadow.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,getWidth(),getHeight());

        drawLight(g, player, 50);
        getGameObjects().stream().filter(gameObject -> gameObject instanceof Torch).forEach(torch -> {
            drawLight(g, torch, 50);
        });

        g.dispose();
    }

    private void drawLight(Graphics2D g2d, GameObject center, int radius) {
        int min = (radius - 2);
        int max = (radius + 6);
        radius = new Random().nextInt((max - min) + 1) + min;

        g2d.setComposite(AlphaComposite.DstOut);
        float[] dist = {0.0f, 1.0f};
        Color[] colors = new Color[]{Color.WHITE, new Color(0,0,0,0)};
        Point2D pt = new Point2D.Float(center.getX() + (center.getWidth() / 2), center.getY() + (center.getHeight() / 2));

        if (center instanceof Player)
            pt = new Point2D.Float(center.getX() + (center.getWidth() / 2), center.getY() + (center.getHeight() / 2) - 24);

        RadialGradientPaint paint = new RadialGradientPaint(pt, radius, dist, colors, MultipleGradientPaint.CycleMethod.NO_CYCLE);
        g2d.setPaint(paint);
        g2d.fillOval((int) pt.getX()-radius, (int) pt.getY() - radius,radius*2,radius*2);
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
     * Get the name of the level
     * @return Levelname as String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Load the current level from it's JSON file
     */
    public void loadLevel() {
        JSONParser parser = new JSONParser();

        try (InputStream is = this.getClass().getResourceAsStream("/levels/level-" + id + ".json")) {
            Reader rd = new InputStreamReader(is, StandardCharsets.UTF_8);
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
            int endX = Integer.parseInt(endpoint.get(0).toString());
            int endY = Integer.parseInt(endpoint.get(1).toString());
            addCollidable(new EndPoint(endX, endY));

            // Read player startpoint
            JSONArray playerpoint = (JSONArray) levelOjbect.get("player");
            int playerX = Integer.parseInt(playerpoint.get(0).toString());
            int playerY = Integer.parseInt(playerpoint.get(1).toString());
            player = new Player(playerX, playerY);
            addCollidable(player);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void setRenderShadows(boolean renderShadows) {
        this.renderShadows = renderShadows;
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
        }
    }

    /**
     * Find a GameObject based on the x and y coordinates
     * @param x x to check for
     * @param y y to check for
     * @return Optional GameObject based on the coords, use #.ifPresent() and #.get() to use
     */
    public Optional<GameObject> fromCoords(int x, int y) {
        return getGameObjects().stream().filter(object -> (object.getX() == x && object.getY() == y)).findAny();
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
