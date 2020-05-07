package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.achievement.AchievementHandler;
import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventHandler;

public class LevelOneAchieved extends Achievement {

    public LevelOneAchieved() {
        AchievementHandler.getInstance().addAchievement(this);
    }

    @EventHandler
    public void onReachedEndEvent(ReachedEndEvent event) {
        if (this.isAchieved())
            return;

        if (event.getLevelID() == 0) {
            achieve("You made it through the first level");
        }
    }
}
