package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;
import nl.ictm2a4.javagame.gameobjects.Pickup;

public class ItemPickupEvent extends Event {

    private Pickup pickup;

    public ItemPickupEvent(Pickup pickup) {
        this.pickup = pickup;
    }

    public Pickup getPickup() {
        return pickup;
    }
}
