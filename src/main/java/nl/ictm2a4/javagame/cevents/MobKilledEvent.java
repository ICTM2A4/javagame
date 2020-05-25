package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;
import nl.ictm2a4.javagame.gameobjects.Mob;

public class MobKilledEvent extends Event {

    private Mob mob;

    public MobKilledEvent(Mob mob) {
        this.mob = mob;
    }

    public Mob getMob() {
        return this.mob;
    }

}
