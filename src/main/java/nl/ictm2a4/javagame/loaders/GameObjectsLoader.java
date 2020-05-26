package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.gameobjects.GameObject;
import nl.ictm2a4.javagame.gameobjects.*;

import java.util.Arrays;
import java.util.List;

public class GameObjectsLoader {

    private static GameObjectsLoader instance;
    private List<Class<? extends GameObject>> objectList;

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

    public List<Class<? extends GameObject>> getObjectList() {
        return objectList;
    }

    public static GameObjectsLoader getInstance() {
        if (instance == null) {
            new GameObjectsLoader();
        }
        return instance;
    }
}
