package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;

public class LastLevelAchieved extends Achievement {

    @EventHandler
    public void onLevelAchieved(ReachedEndEvent event) {
        if (event.getLevelID() == LevelLoader.DEFAULTLEVELAMOUNT - 1) {
            achieve("Last level survived");

            int currentUser = GameScreen.getInstance().getCurrentUserId();
            if(currentUser != 0) {
                new AchievementsService().addAchievementToUser(3, currentUser);
            }
        }
    }
}
