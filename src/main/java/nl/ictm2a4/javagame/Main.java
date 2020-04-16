package nl.ictm2a4.javagame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

public class Main {

    public static final int width = 640;
    public static final int height = 480;
    public static final int gridWidth = 32;
    public static final int gridHeight = 32;

    public static Level level;

    public static void main(String[] args) {
        level = new Level();
        GameScreen screen = new GameScreen(level);
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
