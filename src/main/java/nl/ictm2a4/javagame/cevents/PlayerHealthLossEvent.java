package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;

public class PlayerHealthLossEvent extends Event {

    private int loss, totalHealth;

    public PlayerHealthLossEvent(int loss, int totalHealth) {
        this.loss = loss;
        this.totalHealth = totalHealth;
    }

    public int getLoss() {
        return this.loss;
    }

    public int getTotalHealth() {
        return this.totalHealth;
    }

}
