package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.MobKilledEvent;
import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.event.EventManager;

import java.util.Random;

public abstract class Mob extends GameObject {

    private final int ANIMATEDELAY = 4;
    private int currentImage = 0;
    private int animateCount = 0;

    private PlayerStatus status;
    private PlayerStatus.Direction direction;

    private long prevMove;

    private int range, damage, health, prevDirection;
    private boolean alive = true;
    private int stepSize = 4;

    public Mob(int gridX, int gridY, int width, int height, int range, int damage, int health) {
        super(gridX, gridY, width, height, true);
        this.range = range;
        this.damage = damage;
        this.health = health;
        status = PlayerStatus.IDLE;
        direction = PlayerStatus.Direction.RIGHT;
        setyIndex(9);
        setCollidable(false);
    }

    public boolean withinRange(int x, int y) {
        int a = getX();
        int b = getY();
        int r = range;

        // (x, y) is within circle a.k.a. range of mob
        return (r * r) >= ((x - a) * (x - a)) + ((y - b) * (y - b));
    }

    public int getRange() {
        return this.range;
    }

    @Override
    public void tick() {
        if (alive) {
            nextMove();

            animateCount++;
            if (animateCount % ANIMATEDELAY == 0) {
                animateCount = 0;
                currentImage++;
            }
            if (currentImage >= status.getImageAmount())
                currentImage = 0;
        }
    }

    public boolean isAlive() {
        return this.alive;
    }

    private void nextMove() {
        // 0: nothing, 1: up, 2: right, 3: down, 4: left

        double random = new Random().nextInt(400) + 200;

        int x = getX();
        int y = getY();
        if (prevDirection == 2)
            x += stepSize;
        if (prevDirection == 4)
            x -= stepSize;
        if (prevDirection == 1)
            y -= stepSize;
        if (prevDirection == 3)
            y += stepSize;

        if (prevMove + random <= System.currentTimeMillis() || checkCollide(x,y)) {
            prevMove = System.currentTimeMillis();

            prevDirection = new Random().nextInt(5);
        }
        checkMove();
    }

    private void checkMove() {
        if (prevDirection == 1) {
            move(getX(), getY()- stepSize);
        }

        if (prevDirection == 2){
            move(getX() - stepSize, getY());
            direction = PlayerStatus.Direction.RIGHT;
        }

        if (prevDirection == 3){
            move(getX(), getY() + stepSize);
        }

        if (prevDirection == 4){
            move(getX() + stepSize, getY());
            direction = PlayerStatus.Direction.RIGHT;
        }
    }

    private void move(int x, int y) {
        if(!checkCollide(x, y)) {
            setX(x);
            setY(y);
            status = PlayerStatus.MOVING;
        } else {
            status = PlayerStatus.IDLE;
        }
    }

    public void removeHealth(int healthRemoval) {
        this.health -= healthRemoval;
        if (this.health <= 0) {
            EventManager.getInstance().callEvent(new MobKilledEvent());
            this.alive = false;
        }
    }

    public int getHealth() {
        return this.health;
    }

    public PlayerStatus.Direction getDirection() {
        return this.direction;
    }

    public PlayerStatus getStatus() {
        return this.status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public int getCurrentImage() {
        return currentImage;
    }

    public int getDamage() {
        return this.damage;
    }
}
