package nl.ictm2a4.javagame.cevents;

import nl.ictm2a4.javagame.gameobjects.FakeWall;
import nl.ictm2a4.javagame.event.Event;

public class ThroughFakeWallEvent extends Event {

    private FakeWall wall;

    public ThroughFakeWallEvent(FakeWall wall) {
        this.wall = wall;
    }

    public FakeWall getWall() {
        return wall;
    }
}
