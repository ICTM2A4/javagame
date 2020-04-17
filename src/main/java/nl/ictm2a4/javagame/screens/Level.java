package nl.ictm2a4.javagame.screens;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import nl.ictm2a4.javagame.gameobjects.GameObject;
import nl.ictm2a4.javagame.gameobjects.*;
import nl.ictm2a4.javagame.loaders.FileLoader;
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

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        getGameObjects().forEach(object -> object.draw(g));
        this.player.draw(g);
    }

    public void addCollidable(GameObject gameObject) {
        if (!this.gameObjects.contains(gameObject))
            this.gameObjects.add(gameObject);
    }

    public String getName() {
        return this.name;
    }

    public void loadLevel() {
        JSONParser parser = new JSONParser();
        File file = FileLoader.loadFile("levels/level-" + id + ".json");

        try(FileReader reader = new FileReader(file)) {
            Object object = parser.parse(reader);
            JSONObject levelOjbect = (JSONObject) object;

            // Read level name
            name = levelOjbect.get("name").toString();

            // Read all ground tiles
            JSONArray groundTiles = (JSONArray) levelOjbect.get("ground");
            for(Object ground : groundTiles) {
                JSONArray coords = (JSONArray) ground;
                int x = Integer.parseInt(coords.get(0).toString());
                int y = Integer.parseInt(coords.get(1).toString());
                addCollidable(new Ground(this,x,y));
            }

            // Read endpoint
            JSONArray endpoint = (JSONArray) levelOjbect.get("endpoint");
            int endX = Integer.parseInt(endpoint.get(0).toString());
            int endY = Integer.parseInt(endpoint.get(1).toString());
            addCollidable(new EndPoint(this, endX, endY));

            JSONArray playerpoint = (JSONArray) levelOjbect.get("player");
            int playerX = Integer.parseInt(playerpoint.get(0).toString());
            int playerY = Integer.parseInt(playerpoint.get(1).toString());
            player = new Player(this, playerX, playerY);
            addCollidable(player);

            System.out.println(player);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void generateWalls() {
        Ground[] groundTiles = getGameObjects().stream().filter(object -> (object instanceof Ground)).toArray(Ground[]::new);

        for(Ground ground : groundTiles) {
            int x = ground.getX() / LevelLoader.gridWidth;
            int y = ground.getY() / LevelLoader.gridHeight;

            for (int _x = x-1; _x <= x+1; _x++) {
                for (int _y = y-1; _y <= y+1; _y++) {
                    if (_x == x && _y == y)
                        continue;

                    if (!fromCoords(_x * LevelLoader.gridWidth, _y * LevelLoader.gridHeight).isPresent())
                        addCollidable(new Wall(this,_x,_y));
                }
            }
        }
    }

    public Optional<GameObject> fromCoords(int x, int y) {
        return getGameObjects().stream().filter(object -> (object.getX() == x && object.getY() == y)).findFirst();
    }

    public void tick() {
        repaint();
        getGameObjects().forEach(GameObject::tick);
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }
}
