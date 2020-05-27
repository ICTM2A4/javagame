package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.MobKilledEvent;
import nl.ictm2a4.javagame.event.EventHandler;

public class TheStrongest extends Achievement {

    public TheStrongest() {
        super(6);
    }

    @EventHandler
    public void onMobKill(MobKilledEvent event) {
        achieve();
    }
}
