package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.event.EventManager;

import java.util.ArrayList;

public class AchievementHandler {

    private static AchievementHandler instance;

    private ArrayList<Achievement> achievements;
    private ArrayList<AchievementPopup> popups;
    private int currentFrame;

    public AchievementHandler() {
        instance = this;
        achievements = new ArrayList<>();
        popups = new ArrayList<>();
    }

    @Deprecated
    public ArrayList<Achievement> getAchievements() {
        return achievements;
    }

    public void addAchievement(Achievement achievement) {
        if (!achievements.contains(achievement)) {
            achievements.add(achievement);
            EventManager.getInstance().registerListener(achievement);
        }
    }

    public static AchievementHandler getInstance() {
        return instance;
    }

    public void achieve(String tekst) {
        this.popups.add(new AchievementPopup(tekst));
    }

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
