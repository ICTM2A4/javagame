package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.DoorOpenedEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;

public class FirstDoorOpened extends Achievement {

    @EventHandler
    public void onDoorOpened(DoorOpenedEvent event) {
        achieve("First door opened");

        int currentUser = GameScreen.getInstance().getCurrentUserId();
        if(currentUser != 0) {
            new AchievementsService().addAchievementToUser(1, currentUser);
        }
    }
}
