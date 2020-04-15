package nl.ictm2a4.javagame;

public class Main {

    public static final int width = 640;
    public static final int height = 480;

    public static void main(String[] args) {
        Level level = new Level();

        for(int x = 0; x < 20; x++) {
            for (int z = 0; z < 15; z++) {
                if ((x % 2 == 0) == (z % 2 == 0)) {
                    level.addCollidable(new Wall(x,z));
                }
            }
        }

//        level.addCollidable(new Wall(0,0));
//        level.addCollidable(new Wall(2,0));
//        level.addCollidable(new Wall(1,1));
        GameScreen screen = new GameScreen(level);
    }

}
