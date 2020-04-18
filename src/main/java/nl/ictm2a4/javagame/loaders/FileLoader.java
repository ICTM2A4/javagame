package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.enums.PlayerStatus;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileLoader {

    private ArrayList<Image> groundTiles, wallTiles, coinImages;
    private HashMap<String, ArrayList<Image>> playerImages;
    private static FileLoader instance;

    public FileLoader() {
        instance = this;
        loadImages();
    }

    private void loadImages() {
        reloadGroundTiles();
        reloadWallTiles();
        reloadCoinImages();
        reloadPlayerImages();
    }

    /**
     * Reload all ground tile images
     */
    public void reloadGroundTiles() {
        groundTiles = new ArrayList<>();
        for(int i = 0; i < 16; i++)
            groundTiles.add(loadImage("textures/ground-" + i + ".jpg"));
    }

    /**
     * Reload all wall tile images
     */
    public void reloadWallTiles() {
        wallTiles = new ArrayList<>();
        for(int i = 0; i < 16; i++)
            wallTiles.add(loadImage("textures/wall-" + i + ".jpg"));
    }

    /**
     * Reload all coin images
     */
    public void reloadCoinImages() {
        coinImages = new ArrayList<>();
        for(int i = 0; i < 7; i++)
            coinImages.add(loadImage("textures/coin-" + i + ".png"));
    }

    /**
     * Reload all player images
     */
    public void reloadPlayerImages() {
        playerImages = new HashMap<>();
        for(PlayerStatus status : PlayerStatus.values()) {
            for (PlayerStatus.Direction direction : PlayerStatus.Direction.values()) {
                String s = status.toString().toLowerCase() + direction.toString().toLowerCase();
                playerImages.put(s, new ArrayList<>());
                for (int i = 0; i < status.getImageAmount(); i++) {
                    playerImages.get(s).add(loadImage("textures/player-" + status.toString().toLowerCase() + direction.toString().toLowerCase() + "-" + i + ".png"));
                }
            }
        }
    }

    /**
     * Load an image from the resource folder using a path
     * @param path Path to file in the src/resources folder
     * @return Image file
     */
    public static Image loadImage(String path) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try {
            return ImageIO.read(classLoader.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load a file from the resource folder using a path
     * @param path Path to the file in the src/resources folder
     * @return File
     */
    public static File loadFile(String path) {
        return new File(ClassLoader.getSystemClassLoader().getResource(path).getFile().replaceAll("%20", " "));
    }

    /**
     * Get a ground tile image by it's index
     * @param index Index of the image, 0 - 15
     * @return Image of ground tile
     */
    public Image getGroundTile(int index) {
        return this.groundTiles.get(index);
    }

    /**
     * Get a wall tile image by it's index
     * @param index Index of the image, 0 - 15
     * @return Image of the wall tile
     */
    public Image getWallTile(int index) {
        return this.wallTiles.get(index);
    }

    /**
     * Get a coin image by it's index
     * @param index Index of the image, 0 - 6
     * @return Image of the coin
     */
    public Image getCoinImage(int index) {
        return this.coinImages.get(index);
    }

    /**
     * Get a player image by it's status, direction and index
     * @param status Player movement status
     * @param direction The direction of the player movement, left, right
     * @param index Index of the image
     * @return Image of the player
     */
    public Image getPlayerImage(PlayerStatus status, PlayerStatus.Direction direction, int index) {
        return this.playerImages.get(status.toString().toLowerCase() + direction.toString().toLowerCase()).get(index);
    }

    /**
     * Get the instance of the FileLoader
     * @return FileLoader instance
     */
    public static FileLoader getInstance() {
        return instance;
    }
}

