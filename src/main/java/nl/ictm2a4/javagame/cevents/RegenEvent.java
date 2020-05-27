package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;

public class RegenEvent extends Event {

    private int health;

    public RegenEvent(int health) {
        this.health = health;
    }

    public int getHealth() {
        return health;
    }
}
