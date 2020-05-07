package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;

public class DoorOpenedEvent extends Event {

    private int doorCode;

    public DoorOpenedEvent(int doorCode) {
        this.doorCode = doorCode;
    }

    public int getDoorCode() {
        return doorCode;
    }
}
