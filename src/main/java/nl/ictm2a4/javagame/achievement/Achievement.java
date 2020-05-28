package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.event.Listener;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;

import java.util.ArrayList;
import java.util.Optional;

public abstract class Achievement implements Listener {

    protected int id;

    /**
     * Create an achievement and add it to the AchievementHandler
     * @param id The ID of the achievement int the database
     */
    public Achievement(int id) {
        AchievementHandler.getInstance().addAchievement(this);
        this.id = id;
    }

    /**
     * Get the achieved status of the achievement
     * @return Value of achieved
     */
    public boolean isAchieved() {
        var achievedAchievements = GameScreen.getInstance().getAchievedAchievements();

        return achievedAchievements.stream().anyMatch(a -> a.getId() == this.id);
    }

    /**
     * Set the achieved status to true
     * Saves the status of the achievment in the database
     */
    public void achieve() {
        Optional<Level> level = LevelLoader.getInstance().getCurrentLevel();
        if(isAchieved() || (level.isPresent() && level.get().getId() > LevelLoader.DEFAULTLEVELAMOUNT))
            return;

        var gameScreen = GameScreen.getInstance();

        String tekst = gameScreen.getAchievement(id).getName();
        AchievementHandler.getInstance().achieve(tekst);
        var currentUser = gameScreen.getCurrentUser();

        GameScreen.getInstance().addAchievedAchievement(new nl.ictm2a4.javagame.services.achievements.Achievement(id, "", ""));
        currentUser.ifPresent(user -> new AchievementsService().addAchievementToUser(id, user.getId()));
    }
}
