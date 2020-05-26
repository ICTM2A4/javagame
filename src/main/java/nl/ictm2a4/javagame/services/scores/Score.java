package nl.ictm2a4.javagame.services.scores;

import java.time.LocalDateTime;

public class Score {
    public Score(int id, long sa, LocalDateTime ts, int uid, String un, int soi, String son){
        this.id = id;
        scoreAmount = sa;
        timestamp = ts;
        userID = uid;
        userName = un;
        scoredOnID = soi;
        scoredOnName = son;
    }

    public int id;
    public long scoreAmount;
    public LocalDateTime timestamp;
    public int userID;
    public String userName;
    public int scoredOnID;
    public String scoredOnName;
}
