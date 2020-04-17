package nl.ictm2a4.javagame.enums;

public enum PlayerStatus {
    FIGHTINGLEFT(0),
    FIGHTINGRIGHT(0),
    MOVINGRIGHT(4),
    MOVINGLEFT(4),
    IDLE(2);

    private int imageAmount;

    PlayerStatus(int imageAmount) {
        this.imageAmount = imageAmount;
    }

    public int getImageAmount() {
        return this.imageAmount;
    }
}
