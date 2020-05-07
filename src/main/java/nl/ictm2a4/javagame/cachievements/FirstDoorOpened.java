package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.DoorOpenedEvent;
import nl.ictm2a4.javagame.event.EventHandler;

public class FirstDoorOpened extends Achievement {

    @EventHandler
    public void onDoorOpened(DoorOpenedEvent event) {
        achieve("You opened your first door");
    }

}
