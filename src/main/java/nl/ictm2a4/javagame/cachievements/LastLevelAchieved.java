package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.loaders.LevelLoader;

public class LastLevelAchieved extends Achievement {

    @EventHandler
    public void onLevelAchieved(ReachedEndEvent event) {
        if (event.getLevelID() == LevelLoader.DEFAULTLEVELAMOUNT - 1) {
            achieve("You made it to through the last level");
        }
    }


}
