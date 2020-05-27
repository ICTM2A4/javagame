package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.ThroughFakeWallEvent;
import nl.ictm2a4.javagame.event.EventHandler;

public class FakeWallPassage extends Achievement {

    public FakeWallPassage() {
        super(8);
    }

    @EventHandler
    public void onThroughFakeWall(ThroughFakeWallEvent event) {
        achieve();
    }
}
