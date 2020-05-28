package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.gameobjects.GameObject;
import nl.ictm2a4.javagame.gameobjects.*;

import java.util.Arrays;
import java.util.List;

public class GameObjectsLoader {

    private static GameObjectsLoader instance;
    private List<Class<? extends GameObject>> objectList;

    /**
     * Create an instance of the GameObjectsLoader
     * - creates a list of all active gameObjects used in the LevelEditor
     */
    public GameObjectsLoader() {
        instance = this;
        objectList = Arrays.asList(
            Ground.class,
            Player.class,
            EndPoint.class,
            Sword.class,
            Torch.class,
            Door.class,
            Lever.class,
            Key.class,
            FakeWall.class,
            TeleportationStone.class,
            Orc.class
        );
    }

    /**
     * Get the list of all GameObjects classes
     * @return List of GameObject classes
     */
    public List<Class<? extends GameObject>> getObjectList() {
        return objectList;
    }

    /**
     * Get the instance of the GameObjectsLoader
     * @return GameObjectsLoader intance
     */
    public static GameObjectsLoader getInstance() {
        if (instance == null) {
            new GameObjectsLoader();
        }
        return instance;
    }
}
