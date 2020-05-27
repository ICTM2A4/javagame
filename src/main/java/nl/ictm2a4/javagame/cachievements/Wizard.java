package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.TeleportEvent;
import nl.ictm2a4.javagame.event.EventHandler;

public class Wizard extends Achievement {

    public Wizard() {
        super(9);
    }

    @EventHandler
    public void onTeleport(TeleportEvent event) {
        achieve();
    }


}
