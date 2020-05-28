package nl.ictm2a4.javagame.loaders;

import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.gameobjects.EndPoint;
import nl.ictm2a4.javagame.gameobjects.Torch;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileLoader {
    private ArrayList<Image> groundTiles, wallTiles, coinImages, torchImages, keyImages, doorImages, leverImages;
    private HashMap<String, ArrayList<Image>> playerImages, orcImages, swordImages;
    private Image fakeWallOverlay, swordImage, teleportationStoneImage;
  
    private static FileLoader instance;

    public FileLoader() {
        instance = this;
        loadImages();
    }

    /**
     * Load all the images
     */
    private void loadImages() {
        reloadGroundTiles();
        reloadWallTiles();
        reloadCoinImages();
        reloadPlayerImages();
        reloadTorchImages();
        reloadKeyImages();
        reloadDoorImages();
        reloadLeverImages();
        reloadFakeWallImages();
        reloadTelportationStoneImage();
        reloadOrcImages();
        reloadSwordImages();
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
     * Reload all lever images
     */
    public void reloadLeverImages(){
        leverImages = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            leverImages.add(loadImage("textures/lever-" + i + ".png"));
    }

    /**
     * Reload all coin images
     */
    public void reloadCoinImages() {
        coinImages = new ArrayList<>();
        for(int i = 0; i < EndPoint.IMAGEAMOUNT; i++)
            coinImages.add(loadImage("textures/coin-" + i + ".png"));
    }

    /**
     * Reload all key images
     */
    public void reloadKeyImages() {
        keyImages = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            keyImages.add(loadImage("textures/key-" + i + ".png"));
    }

    /**
     * Reload all door images
     */
    public void reloadDoorImages() {
        doorImages = new ArrayList<>();
        for(int i = 0; i < 4; i++)
            doorImages.add(loadImage("textures/door-" + i + ".png"));
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
     * Reload all orc images
     */
    public void reloadOrcImages() {
        orcImages = new HashMap<>();
        for(PlayerStatus status : PlayerStatus.values()) {
            for (PlayerStatus.Direction direction : PlayerStatus.Direction.values()) {
                String s = status.toString().toLowerCase() + direction.toString().toLowerCase();
                orcImages.put(s, new ArrayList<>());
                for (int i = 0; i < status.getImageAmount(); i++) {
                    orcImages.get(s).add(loadImage("textures/mob-" + status.toString().toLowerCase() + direction.toString().toLowerCase() + "-" + i + ".png"));
                }
            }
        }
    }

    /**
     * Relaad all torch images
     */
    public void reloadTorchImages() {
        torchImages = new ArrayList<>();
        for(int i = 0; i < Torch.IMAGEAMOUNT; i++)
            torchImages.add(loadImage("textures/torch-" + i + ".png"));
    }

    /**
     * Reload fake wall image
     */
    public void reloadFakeWallImages() {
        fakeWallOverlay = loadImage("textures/fakewalloverlay.png");
    }

    /**
     * Reload teleportation stone image
     */
    public void reloadTelportationStoneImage() {
      teleportationStoneImage = loadImage("textures/teleportation_stone-0.png");
    }

    /**
     * Reload all sword images
     */
    public void reloadSwordImages() {
        swordImage = FileLoader.loadImage("textures/sword-0.png");
        swordImages = new HashMap<>();
        for(PlayerStatus status : PlayerStatus.values()) {
            for (PlayerStatus.Direction direction : PlayerStatus.Direction.values()) {
                String s = status.toString().toLowerCase() + direction.toString().toLowerCase();
                swordImages.put(s, new ArrayList<>());
                for (int i = 0; i < status.getImageAmount(); i++) {
                    swordImages.get(s).add(loadImage("textures/sword-" + status.toString().toLowerCase() + direction.toString().toLowerCase() + "-" + i + ".png"));
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
     * Get a ground tile image by it's index
     * @param index Index of the image, 0 - 15
     * @return Image of ground tile
     */
    public Image getGroundTile(int index) {
        return this.groundTiles.get(index);
    }

    /**
     * Get the key image by it's index
     * @param index Index of the image, 0 - 3
     * @return Image of the key
     */
    public Image getKeyImage(int index) {
        return this.keyImages.get(index);
    }

    /**
     * Get the lever image by it's index
     * @param doorCode Index of the image, 0 - 3
     * @return Image of the lever
     */
    public Image getLeverImage(int doorCode) {
        return this.leverImages.get(doorCode);
    }

    /**
     * Get the door image by it's index
     * @param index Index of the image, 0 - 3
     * @return Image of the door
     */
    public Image getDoorImage(int index) {
        return this.doorImages.get(index);
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
     * @param direction The direction of the player movement, left or right
     * @param index Index of the image
     * @return Image of the player
     */
    public Image getPlayerImage(PlayerStatus status, PlayerStatus.Direction direction, int index) {
        return this.playerImages.get(status.toString().toLowerCase() + direction.toString().toLowerCase()).get(index);
    }

    /**
     * Get an orc image by it's status, direction and index
     * @param status Orc movement status
     * @param direction The direction of the orc movement, left or right
     * @param index Index of the image
     * @return Image of the orc
     */
    public Image getOrcImage(PlayerStatus status, PlayerStatus.Direction direction, int index) {
        return this.orcImages.get(status.toString().toLowerCase() + direction.toString().toLowerCase()).get(index);
    }

    /**
     * Get a player image by it's status, direction and index
     * @param status Player movement status
     * @param direction The direction of the player movement, left or right
     * @param index Index of the image
     * @return Image of the sword
     */
    public Image getSwordImage(PlayerStatus status, PlayerStatus.Direction direction, int index) {
        return this.swordImages.get(status.toString().toLowerCase() + direction.toString().toLowerCase()).get(index);
    }

    /**
     * Get the single sword image
     * @return Sword image
     */
    public Image getSwordImageSingle() {
        return this.swordImage;
    }

    /**
     * Get a torch image, by it's index
     * @param index Index of the torch image, 0 - 3
     * @return Image of the torch
     */
    public Image getTorchImage(int index) {
        return this.torchImages.get(index);
    }

    /**
     * Get the fake wall overlay image
     * @return Image of FakeWall
     */
    public Image getFakeWallOverlay() {
        return fakeWallOverlay;
    }

    /**
     * Get the teleporation stone image
     * @return Image of the TeleportationStone
     */
    public Image getTeleportationStoneImage() {return teleportationStoneImage;}

    /**
     * Get the instance of the FileLoader
     * @return FileLoader instance
     */
    public static FileLoader getInstance() {
        return instance;
    }
}

