package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.event.EventManager;

import java.util.ArrayList;

public class AchievementHandler {

    private static AchievementHandler instance;

    private ArrayList<Achievement> achievements;
    private ArrayList<AchievementPopup> popups;
    private int currentFrame;

    /**
     * Create an instance of the AchievementHandler
     */
    public AchievementHandler() {
        instance = this;
        achievements = new ArrayList<>();
        popups = new ArrayList<>();
    }

    /**
     * Add an achievement, so it can be used
     * @param achievement The achievement to add to the handler
     */
    public void addAchievement(Achievement achievement) {
        if (!achievements.contains(achievement)) {
            achievements.add(achievement);
            EventManager.getInstance().registerListener(achievement);
        }
    }

    /**
     * Get the instance of the AchievementHandler
     * @return AchievementHandler instance
     */
    public static AchievementHandler getInstance() {
        return instance;
    }

    /**
     * Create a popup of the achievement
     * @param tekst The tekst to display on the popup
     */
    public void achieve(String tekst) {
        this.popups.add(new AchievementPopup(tekst));
    }

    /**
     * Tick the AchievementHandler
     * If a achievement is being achieved, tick that achievement
     */
    public void tick() {
        if (this.popups.size() > 0) {
            currentFrame++;
            if (this.popups.get(0).render(currentFrame)) {
                this.popups.remove(this.popups.get(0));
                currentFrame = 0;
            }
        }
    }
}
