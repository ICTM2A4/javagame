package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Player extends GameObject {

    private final int ANIMATEDELAY = 4;
    private int currentImage = 0;
    private int animateCount = 0;

    private PlayerStatus status;
    private PlayerStatus.Direction direction;

    private ArrayList<Key> inventory;

    public Player(int gridX, int gridY) {
        super(((gridX * LevelLoader.GRIDWIDTH) + 4),
            ((gridY * LevelLoader.GRIDHEIGHT) + 2),
            16, 20, true);
        setCollidable(false);
        setyIndex(10);
        status = PlayerStatus.IDLE;
        direction = PlayerStatus.Direction.RIGHT;
        inventory = new ArrayList<>();
    }

    /**
     * Check if movement buttons are pressed, and then check if new location is occupied.
     */
    public void checkMove() {
        int stepSize = 4;

        List<Integer> pressedKeys = GameScreen.getInstance().getPressedKeys();

        if (pressedKeys.contains(KeyEvent.VK_W)){
            move(getX(), getY()- stepSize);
        }

        if (pressedKeys.contains(KeyEvent.VK_A)){
            move(getX() - stepSize, getY());
            direction = PlayerStatus.Direction.LEFT;
        }

        if (pressedKeys.contains(KeyEvent.VK_S)){
            move(getX(), getY() + stepSize);
        }

        if (pressedKeys.contains(KeyEvent.VK_D)){
            move(getX() + stepSize, getY());
            direction = PlayerStatus.Direction.RIGHT;
        }

        if (!pressedKeys.contains(KeyEvent.VK_W) && !pressedKeys.contains(KeyEvent.VK_A) && !pressedKeys.contains(KeyEvent.VK_S) && !pressedKeys.contains(KeyEvent.VK_D))
            status = PlayerStatus.IDLE;

    }

    /**
     * Test the x, y coords for a GameObject, which is collidable, and update the playerStatus
     * @param x x of the new player's location
     * @param y y of the new player's location
     */
    private void move(int x, int y) {
        if(!checkCollide(x, y)) {
            setX(x);
            setY(y);
            status = PlayerStatus.MOVING;
        } else {
            status = PlayerStatus.IDLE;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getPlayerImage(status,direction,currentImage),
            getX() - 8, getY() - 30,
            LevelLoader.getInstance().getCurrentLevel().get());
    }

    /**
     * Update the player image, corresponding to the current frame and player status
     */
    @Override
    public void tick() {
        checkMove();

        animateCount++;
        if (animateCount % ANIMATEDELAY == 0) {
            animateCount = 0;
            currentImage++;
        }
        if (currentImage >= status.getImageAmount())
            currentImage = 0;

    }

    public void addToInventory(Key key) {
        inventory.add(key);
    }

    public void removeFromInventory(Key key) {
        inventory.remove(key);
    }

    public Key[] getInventory(){
        return inventory.toArray(Key[]::new);
    }

    public boolean inventoryHasKey(int keycode){
        return inventory.stream().filter(key -> key.getKeyCode() == keycode).count() > 0;
    }
}
