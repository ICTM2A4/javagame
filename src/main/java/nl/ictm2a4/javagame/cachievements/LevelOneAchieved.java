package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventHandler;

public class LevelOneAchieved extends Achievement {

    @EventHandler
    public void onReachedEndEvent(ReachedEndEvent event) {
        if (event.getLevelID() == 0) {
            achieve("First level achieved");
        }
    }
}
