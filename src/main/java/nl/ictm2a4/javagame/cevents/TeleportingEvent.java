package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;
import nl.ictm2a4.javagame.gameobjects.TeleportationStone;

public class TeleportingEvent extends Event {

    private TeleportationStone stone;

    public TeleportingEvent(TeleportationStone stone) {
        this.stone = stone;
    }

    public TeleportationStone getStone() {
        return this.stone;
    }

}
