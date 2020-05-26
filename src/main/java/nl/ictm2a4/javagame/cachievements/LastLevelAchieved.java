package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;

public class LastLevelAchieved extends Achievement {
    public LastLevelAchieved(){
        super(2);
    }

    @EventHandler
    public void onLevelAchieved(ReachedEndEvent event) {
        if (event.getLevelID() == LevelLoader.DEFAULTLEVELAMOUNT - 1) {
            achieve("Last level survived");

            var currentUser = GameScreen.getInstance().getCurrentUser();
            if(currentUser != null) {
                new AchievementsService().addAchievementToUser(id, currentUser.id);
            }
        }
    }
}
