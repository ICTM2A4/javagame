package nl.ictm2a4.javagame.cachievements;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cevents.MobKilledEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.gameobjects.Player;
import nl.ictm2a4.javagame.gameobjects.Sword;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import java.util.Optional;

public class FistFight extends Achievement {

    public FistFight() {
        super(7);
    }

    @EventHandler
    public void onMobKilled(MobKilledEvent event) {
        Optional<Player> player = LevelLoader.getInstance().getCurrentLevel().get().getPlayer();
        if (player.isPresent() && player.get().getInventory().stream().noneMatch(object -> object instanceof Sword))
            achieve();
    }

}
