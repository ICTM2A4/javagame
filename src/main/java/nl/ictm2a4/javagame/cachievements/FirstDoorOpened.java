package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.DoorOpenedEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;

public class FirstDoorOpened extends Achievement {

    public FirstDoorOpened(){
        super(3);
    }

    @EventHandler
    public void onDoorOpened(DoorOpenedEvent event) {
        achieve();

        var currentUser = GameScreen.getInstance().getCurrentUser();
        currentUser.ifPresent(user -> new AchievementsService().addAchievementToUser(id, user.getId()));
    }
}
