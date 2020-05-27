package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;
import nl.ictm2a4.javagame.gameobjects.TeleportationStone;

public class TeleportEvent extends Event {

    private TeleportationStone origin, target;

    public TeleportEvent(TeleportationStone origin, TeleportationStone target) {
        this.origin = origin;
        this.target = target;
    }

    public TeleportationStone getOrigin() {
        return origin;
    }

    public TeleportationStone getTarget() {
        return target;
    }
}
