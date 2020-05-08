package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.event.Listener;

public abstract class Achievement implements Listener {

    private boolean achieved;

    public Achievement() {
        AchievementHandler.getInstance().addAchievement(this);
    }

    public boolean isAchieved() {
        return this.achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public void achieve(String tekst) {
        if(isAchieved())
            return;
        this.achieved = true;
        AchievementHandler.getInstance().achieve(tekst);
    }

}
