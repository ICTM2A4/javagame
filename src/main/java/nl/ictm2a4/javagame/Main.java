package nl.ictm2a4.javagame;

public class Main {

    public static final int width = 640;
    public static final int height = 480;
    public static final int gridWidth = 32;
    public static final int gridHeight = 32;

    public static  Player player;
    public static Level level;

    public static void main(String[] args) {

        level = new Level();
        //level.loadLevel();
        player = new Player(40, 40);
        level.addCollidable(player);
        GameScreen screen = new GameScreen(level);
        level.start();
    }

}
