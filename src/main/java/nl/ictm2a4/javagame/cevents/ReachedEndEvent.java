package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.event.Event;

public class ReachedEndEvent extends Event {

    private int levelID;
    public ReachedEndEvent(int levelID) {
        this.levelID = levelID;
    }

    public int getLevelID() {
        return levelID;
    }
}
