package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.RegenEvent;
import nl.ictm2a4.javagame.event.EventHandler;

public class BelowTenHealth extends Achievement {

    public BelowTenHealth() {
        super(5);
    }

    @EventHandler
    public void onRegen(RegenEvent event) {
        if (event.getHealth() <= 10)
            achieve();
    }
}
