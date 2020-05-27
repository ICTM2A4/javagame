package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.cevents.MobKilledEvent;
import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.event.EventManager;

import java.awt.*;
import java.util.Random;

public abstract class Mob extends GameObject {

    private final int ANIMATEDELAY = 6;
    private int currentImage = 0;
    private int animateCount = 0;

    private PlayerStatus status;
    private PlayerStatus.Direction direction;

    private long prevMove;

    private int range, damage, maxHealth, health, prevDirection;
    private boolean alive = true;
    private int stepSize = 2;

    public Mob(int gridX, int gridY, int width, int height, int range, int damage, int health) {
        super(gridX, gridY, width, height, true);
        this.range = range;
        this.damage = damage;
        this.maxHealth = health;
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

    @Deprecated
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

            if (status == PlayerStatus.FIGHTING && currentImage >= PlayerStatus.FIGHTING.getImageAmount())
                status = PlayerStatus.IDLE;

            if (currentImage >= status.getImageAmount())
                currentImage = 0;
        }
    }

    @Override
    public void draw(Graphics g) {

        if (isAlive())
            paintHealthBar(g, getX() - 6, getY() - 46);

    }

    private void paintHealthBar(Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, 24, 8);
        g2.setColor(Color.GREEN);
        int width = (int)Math.round(20 * ((double)getHealth() / (double)maxHealth));

        g2.fillRect(x + 2, y + 2, width, 4);
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
            direction = PlayerStatus.Direction.LEFT;
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
            if (status != PlayerStatus.FIGHTING)
                status = PlayerStatus.MOVING;
        } else {
            if (status != PlayerStatus.FIGHTING)
                status = PlayerStatus.IDLE;
        }
    }

    public void removeHealth(int healthRemoval) {
        this.health -= healthRemoval;
        if (this.health <= 0) {
            EventManager.getInstance().callEvent(new MobKilledEvent(this));
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
        currentImage = 0;
    }

    public int getCurrentImage() {
        return currentImage;
    }

    public int getDamage() {
        return this.damage;
    }
}
