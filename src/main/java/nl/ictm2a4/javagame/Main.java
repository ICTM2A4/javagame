package nl.ictm2a4.javagame;

public class Main {

    public static final int width = 640;
    public static final int height = 480;
    public static final int gridWidth = 32;
    public static final int gridHeight = 32;

    public static void main(String[] args) {
        Level level = new Level();
        level.loadLevel();

        GameScreen screen = new GameScreen(level);
    }

}
