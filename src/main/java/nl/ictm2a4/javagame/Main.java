package nl.ictm2a4.javagame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Main {

    public static final int width = 640;
    public static final int height = 480;
    public static final int gridWidth = 32;
    public static final int gridHeight = 32;
  
    public static  Player player;
    public static GameScreen screen;
    private static MainMenu MainMenu;

    public static void main(String[] args) {

        Level level = new Level(1);
        player = new Player(level, 40, 40);
        level.addCollidable(player);
        MainMenu = new MainMenu();
        screen = new GameScreen(level);
        level.start();
    }

    public static Image loadImage(String path) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            return ImageIO.read(classLoader.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
