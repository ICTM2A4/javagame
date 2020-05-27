package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.LevelSavedEvent;
import nl.ictm2a4.javagame.event.EventHandler;

public class FirstLevelSaved extends Achievement {

    public FirstLevelSaved() {
        super(4);
    }

    @EventHandler
    public void onLevelSaved(LevelSavedEvent event) {
        achieve();
    }

}
