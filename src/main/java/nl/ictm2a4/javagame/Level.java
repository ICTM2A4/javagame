package nl.ictm2a4.javagame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Level extends JPanel implements Runnable {

    private int id, width, height;
    private ArrayList<Collidable> collidables;
    private String name;

    private Thread thread;
    private  boolean isRunning = false;
    private int FPS = 30;
    private long targetTime = 1000 / FPS;

    public Level() {
        id = 0;
        collidables = new ArrayList<>();
        setBackground(Color.black);

        width = Main.width;
        height = Main.height;

        this.setPreferredSize(new Dimension(width, height));

//        loadLevel();
    }

    public ArrayList<Collidable> getCollidables() {
        return collidables;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(Collidable collidable : collidables)
            collidable.draw(g);
    }

    public void addCollidable(Collidable collidable) {
        if (!this.collidables.contains(collidable))
            this.collidables.add(collidable);
    }

    public String getName() {
        return this.name;
    }

    public void loadLevel() {
        JSONParser parser = new JSONParser();
        File file = new File(ClassLoader.getSystemClassLoader().getResource("levels/" +
                "level-" + this.id + ".json").getFile());

        try(FileReader reader = new FileReader(file)) {
            Object object = parser.parse(reader);
            JSONObject levelOjbect = (JSONObject) object;

            // Read level name
            name = levelOjbect.get("name").toString();

            // Read endpoint
            JSONArray endpoint = (JSONArray) levelOjbect.get("endpoint");
            int endX = Integer.valueOf(endpoint.get(0).toString());
            int endY = Integer.valueOf(endpoint.get(1).toString());
            addCollidable(new EndPoint(endX, endY));

            // Read all walls
            JSONArray array = (JSONArray) levelOjbect.get("walls");
            for(Object wall : array) {
                JSONArray coords = (JSONArray) wall;
                int x = Integer.valueOf(coords.get(0).toString());
                int y = Integer.valueOf(coords.get(1).toString());
                addCollidable(new Wall(x,y));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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


}
