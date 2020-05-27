package nl.ictm2a4.javagame.listeners;

import nl.ictm2a4.javagame.cevents.MobKilledEvent;
import nl.ictm2a4.javagame.cevents.ReachedEndEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.event.Listener;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.services.scores.Score;
import nl.ictm2a4.javagame.services.scores.ScoreService;

import java.time.LocalDateTime;

public class ScoreListener implements Listener {
    private int mobsKilled;
    private static ScoreListener instance;
    public long scoreAmount;

    public ScoreListener(){
        instance = this;
    }

    @EventHandler
    public void onMobKill(MobKilledEvent event){
        mobsKilled += 1;
    }

    @EventHandler
    public void onLevelAchieved(ReachedEndEvent event) {
        if (LevelLoader.getInstance().getCurrentLevel().isEmpty())
            return;

        var timeScore = LevelLoader.getInstance().getCurrentLevel().get().getScore();

        scoreAmount = Math.round((60 * 30 - timeScore) * 0.9);
        scoreAmount = (scoreAmount > 0) ? scoreAmount : 0;
        scoreAmount = scoreAmount +  (mobsKilled * 50);

        var user = GameScreen.getInstance().getCurrentUser();

        if(user.isEmpty()){
            return;
        }

        var score = new Score(0, scoreAmount, LocalDateTime.now(), user.get().getId(), "", event.getLevelID(), "");

        new ScoreService().addScore(score);
    }

    public static ScoreListener getInstance(){
        if(instance == null){
            new ScoreListener();
        }

        return instance;
    }
}
