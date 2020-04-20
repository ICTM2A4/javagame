package nl.ictm2a4.javagame.enums;

public enum PlayerStatus {
    MOVING(4),
    IDLE(2);

    private int imageAmount;

    PlayerStatus(int imageAmount) {
        this.imageAmount = imageAmount;
    }

    public int getImageAmount() {
        return this.imageAmount;
    }

    public enum Direction {
        LEFT,
        RIGHT
    }
}
