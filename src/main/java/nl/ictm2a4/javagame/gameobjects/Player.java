package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.raspberrypi.RaspberryPIController;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.screens.LevelEditor;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Player extends GameObject {

    private final int ANIMATEDELAY = 4;
    private int currentImage = 0;
    private int animateCount = 0;

    private PlayerStatus status;
    private PlayerStatus.Direction direction;

    private List<Pickup> inventory;

    @JSONLoader(JSONString = "player")
    public Player(Integer gridX, Integer gridY) {
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
        String rpiButton = RaspberryPIController.getInstance().getPressedButton();

        if (pressedKeys.contains(KeyEvent.VK_W) || rpiButton.equals("up")) {
            move(getX(), getY()- stepSize);
        }

        if (pressedKeys.contains(KeyEvent.VK_A) || rpiButton.equals("left")){
            move(getX() - stepSize, getY());
            direction = PlayerStatus.Direction.LEFT;
        }

        if (pressedKeys.contains(KeyEvent.VK_S) || rpiButton.equals("down")){
            move(getX(), getY() + stepSize);
        }

        if (pressedKeys.contains(KeyEvent.VK_D) || rpiButton.equals("right")) {
            move(getX() + stepSize, getY());
            direction = PlayerStatus.Direction.RIGHT;
        }

        if (!(pressedKeys.contains(KeyEvent.VK_W)  || rpiButton.equals("up")) &&
            !(pressedKeys.contains(KeyEvent.VK_A) || rpiButton.equals("left")) &&
            !(pressedKeys.contains(KeyEvent.VK_S) || rpiButton.equals("down")) &&
            !(pressedKeys.contains(KeyEvent.VK_D) || rpiButton.equals("right")))
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

    public void addToInventory(Pickup pickup) {
        inventory.add(pickup);
    }

    public void removeFromInventory(Pickup pickup) {
        inventory.remove(pickup);
    }

    public List<Pickup> getInventory(){
        return inventory;
    }

    public Optional<Pickup> getFromInventory(int keycode){
        return inventory.stream().filter(pickup -> pickup instanceof Key).filter(key -> key.getExtra() == keycode).findFirst();
    }

    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(Player.class, FileLoader.getInstance().getPlayerImage(PlayerStatus.IDLE, PlayerStatus.Direction.RIGHT, 0)) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                Level level = LevelEditor.getInstance().getLevel();
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                Optional<GameObject> player = level.getGameObjects().stream().filter(gameObject -> gameObject instanceof Player).findFirst();
                player.ifPresent(level::removeGameObject);
                level.addGameObject(new Player(gridX, gridY));
            }
        }.setRequireGround(true);
    }
}
