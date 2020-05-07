package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.Level;

public class AchievementPopup {

    private String tekst;

    public AchievementPopup(String tekst) {
        this.tekst = tekst;
    }

    public boolean render(int frame) {

        int startX = LevelLoader.WIDTH;
        int startY = 30;

        Level Level = LevelLoader.getInstance().getCurrentLevel().get();


        return false;
    }

}
