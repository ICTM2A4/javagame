
package nl.ictm2a4.javagame;

import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;

public class Main {

    public static void main(String[] args) {
        new GameScreen();
        LevelLoader.getInstance().loadLevel(1);
    }
}
