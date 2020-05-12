package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;

public class LeverSwitchEvent extends Event {

    private int doorCode;

    public LeverSwitchEvent(int doorCode) {
        this.doorCode = doorCode;
    }

    public int getDoorCode() {
        return doorCode;
    }
}
