package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.AnnotatedArrayType;
import java.util.ArrayList;

public class FileLoader {

    private ArrayList<Image> groundTiles, wallTiles, coinImages;
    private static FileLoader instance;

    public FileLoader() {
        instance = this;
        loadImages();
    }

    private void loadImages() {
        reloadGroundTiles();
        reloadWallTiles();
        reloadCoinImages();
    }

    public void reloadGroundTiles() {
        groundTiles = new ArrayList<>();
        for(int i = 0; i < 16; i++)
            groundTiles.add(loadImage("textures/ground-" + i + ".jpg"));
    }

    public void reloadWallTiles() {
        wallTiles = new ArrayList<>();
        for(int i = 0; i < 16; i++)
            wallTiles.add(loadImage("textures/wall-" + i + ".jpg"));
    }

    public void reloadCoinImages() {
        coinImages = new ArrayList<>();
        for(int i = 0; i < 7; i++)
            coinImages.add(loadImage("textures/coin-" + i + ".png"));
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

    public static File loadFile(String path) {
        return new File(ClassLoader.getSystemClassLoader().getResource(path).getFile().replaceAll("%20", " "));
    }

    public Image getGroundTile(int index) {
        return this.groundTiles.get(index);
    }

    public Image getWallTile(int index) {
        return this.wallTiles.get(index);
    }

    public Image getCoinImage(int index) {
        return this.coinImages.get(index);
    }

    public static FileLoader getInstance() {
        return instance;
    }
}

