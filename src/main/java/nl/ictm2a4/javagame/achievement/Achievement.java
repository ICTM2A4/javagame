package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.event.Listener;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;

import java.util.ArrayList;

public abstract class Achievement implements Listener {

    protected int id;

    public Achievement(int id) {
        AchievementHandler.getInstance().addAchievement(this);
        this.id = id;
    }

    public boolean isAchieved() {

        var currentUser = GameScreen.getInstance().getCurrentUser();

        ArrayList<nl.ictm2a4.javagame.services.achievements.Achievement> achieved_achievements = new ArrayList<>();

        if(currentUser.isPresent()){
            achieved_achievements = (ArrayList<nl.ictm2a4.javagame.services.achievements.Achievement>) new AchievementsService().getAchievements(currentUser.get().id);
        } else{
            return false;
        }

        return achieved_achievements.stream().filter(a -> a.id == this.id).findAny().isPresent();
    }

    public void achieve(String tekst) { //TODO: tekst uit api gebruiken
        if(isAchieved())
            return;
        AchievementHandler.getInstance().achieve(tekst);
    }
}
