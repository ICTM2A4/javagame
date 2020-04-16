package nl.ictm2a4.javagame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level extends JPanel implements Runnable {

    private int id, width, height;
    private ArrayList<GameObject> gameObjects;
    private String name;

    private Thread thread;
    private  boolean isRunning = false;
    private int FPS = 30;
    private long targetTime = 1000 / FPS;

    public Level() {
        id = 0;
        gameObjects = new ArrayList<>();
        setBackground(Color.black);

        width = Main.width;
        height = Main.height;

        this.setPreferredSize(new Dimension(width, height));
        loadLevel();
        generateWalls();
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(GameObject gameObject : gameObjects)
            gameObject.draw(g);
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
        File file = new File(ClassLoader.getSystemClassLoader().getResource("levels/" +
                "level-" + this.id + ".json").getFile().replaceAll("%20", " "));

        try(FileReader reader = new FileReader(file)) {
            Object object = parser.parse(reader);
            JSONObject levelOjbect = (JSONObject) object;

            // Read level name
            name = levelOjbect.get("name").toString();

            // Read all walls
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
            addCollidable(new EndPoint(this,endX, endY));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        long start, elapsed, wait;
        while(isRunning) {
            start = System.nanoTime();

            tick();
            repaint();

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

    public void tick() {

    }

    public void generateWalls() {
        System.out.println(this.getGameObjects().size());
        for (int i = 0; i < this.getGameObjects().size(); i++) {
            if (!(this.getGameObjects().get(i) instanceof Ground))
                continue;

            Ground ground = (Ground) this.getGameObjects().get(i);
            boolean[] connected = ground.hasConnectedFaces();

            int x = (ground.getX() / Main.gridWidth);
            int y = (ground.getY() / Main.gridHeight);

            if (!connected[0])
                addCollidable(new Wall(this, x, y - 1));
            if (!connected[1])
                addCollidable(new Wall(this, x + 1, y));
            if (!connected[2])
                addCollidable(new Wall(this, x, y + 1));
            if (!connected[3])
                addCollidable(new Wall(this, x - 1, y));
        }
        System.out.println(this.getGameObjects().size());
    }

}
