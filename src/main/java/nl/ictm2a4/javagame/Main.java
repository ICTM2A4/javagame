package nl.ictm2a4.javagame;

public class Main {

    public static final int width = 640;
    public static final int height = 480;

    public static Level level;

    public static void main(String[] args) {
        level = new Level();
        GameScreen screen = new GameScreen(level);
    }

}