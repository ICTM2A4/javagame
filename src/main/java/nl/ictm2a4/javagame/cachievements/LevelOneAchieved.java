package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;

public class LevelOneAchieved extends Achievement {
    public LevelOneAchieved(){
        super(1);
    }

    @EventHandler
    public void onReachedEndEvent(ReachedEndEvent event) {
        if (event.getLevelID() == 0) {
            achieve("First level achieved");

            var currentUser = GameScreen.getInstance().getCurrentUser();
            if(currentUser != null) {
                new AchievementsService().addAchievementToUser(id, currentUser.id);
            }
        }
    }
}
