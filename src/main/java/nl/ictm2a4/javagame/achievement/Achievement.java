package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.event.Listener;

public abstract class Achievement implements Listener {

    private boolean achieved;

    public boolean isAchieved() {
        return this.achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public void achieve(String tekst) {
        this.achieved = true;
        AchievementHandler.getInstance().achieve(tekst);
    }

}
