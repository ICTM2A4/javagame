package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.PlayerDiedEvent;
import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.event.EventManager;
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
    private int health, damage;
    private long prevHit;

    private List<Pickup> inventory;

    @JSONLoader(JSONString = "player")
    public Player(Integer gridX, Integer gridY) {
        super(((gridX * LevelLoader.GRIDWIDTH) + 4),
            ((gridY * LevelLoader.GRIDHEIGHT) + 2),
            16, 20);
        setCollidable(false);
        setyIndex(10);
        status = PlayerStatus.IDLE;
        direction = PlayerStatus.Direction.RIGHT;
        inventory = new ArrayList<>();
        health = 100;
        damage = 4;
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
            if (status != PlayerStatus.FIGHTING)
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
            if (status != PlayerStatus.FIGHTING)
                status = PlayerStatus.MOVING;
        } else {
            if (status != PlayerStatus.FIGHTING)
                status = PlayerStatus.IDLE;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (LevelLoader.getInstance().getCurrentLevel().isEmpty())
            return;
        g.drawImage(FileLoader.getInstance().getPlayerImage(status,direction,currentImage),
            getX() - 8, getY() - 30,
            LevelLoader.getInstance().getCurrentLevel().get());
        if (getInventory().stream().anyMatch(object -> object instanceof Sword))
            g.drawImage(FileLoader.getInstance().getSwordImage(status,direction,currentImage),
                getX() - 8, getY() - 30,
                LevelLoader.getInstance().getCurrentLevel().get());
    }

    /**
     * Update the player image, corresponding to the current frame and player status
     */
    @Override
    public void tick() {
        checkCollide(getX(), getY());
        checkMove();

        animateCount++;
        if (animateCount % ANIMATEDELAY == 0) {
            animateCount = 0;
            currentImage++;
        }

        if (status == PlayerStatus.FIGHTING && currentImage >= PlayerStatus.FIGHTING.getImageAmount())
            status = PlayerStatus.IDLE;

        if (currentImage >= status.getImageAmount())
            currentImage = 0;


        tryHitting();
    }

    /**
     * Check if the player is trying to hit the mob
     */
    private void tryHitting() {
        List<Integer> pressedKeys = GameScreen.getInstance().getPressedKeys();
        String rpiButton = RaspberryPIController.getInstance().getPressedButton();

        if ((pressedKeys.contains(KeyEvent.VK_SPACE)  || rpiButton.equals("middle")) && prevHit + 200 <= System.currentTimeMillis()
            && LevelLoader.getInstance().getCurrentLevel().isPresent()) {
            for(Mob mob : LevelLoader.getInstance().getCurrentLevel().get().
                getGameObjects().stream().filter(gameObject -> gameObject instanceof Mob).map(m -> (Mob)m).toArray(Mob[]::new)) {

                if (mob.checkCollideSingle(this, getX(), getY())) {
                    mob.removeHealth(getDamage());
                    status = PlayerStatus.FIGHTING;
                    currentImage = 0;
                    prevHit = System.currentTimeMillis();
                }
            }
        }
    }

    /**
     * Add the Pickup item to the inventory
     * @param pickup Pickup item to add
     */
    public void addToInventory(Pickup pickup) {
        inventory.add(pickup);
    }

    /**
     * Remove a Pickup item from the inventory
     * @param pickup Pickup item to remove
     */
    public void removeFromInventory(Pickup pickup) {
        inventory.remove(pickup);
    }

    /**
     * Get all the Pickup items from the inventory
     * @return List of inventory Pickup items
     */
    public List<Pickup> getInventory(){
        return inventory;
    }

    /**
     * Get the Key item from the player by it's keyCode
     * @param keycode keyCode to find the Key by
     * @return The Optional Key
     */
    public Optional<Pickup> getFromInventory(int keycode){
        return inventory.stream().filter(pickup -> pickup instanceof Key).filter(key -> key.getExtra() == keycode).findFirst();
    }

    /**
     * Get the LevelEditorSpecs for Player
     * @return LevelEditorSpecs
     */
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

    /**
     * Set the new health of the player
     * @param health New health to set
     */
    public void setHealth(int health) {
        this.health = health;
        if (this.health <= 0) {
            EventManager.getInstance().callEvent(new PlayerDiedEvent());
        }
    }

    /**
     * Get the damage of the player
     * @return player damage
     */
    public int getDamage() {
        return this.damage;
    }

    /**
     * Get the health of the player
     * @return player health
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Set the Player damage
     * @param damage Damage to set
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }
}
