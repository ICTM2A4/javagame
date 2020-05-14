package nl.ictm2a4.javagame.services.scores;

import java.time.LocalDateTime;

public class Score {
    public Score(int id, long sa, LocalDateTime ts, int uid, String un, int soi, String son){
        ID = id;
        ScoreAmount = sa;
        Timestamp = ts;
        UserID = uid;
        UserName = un;
        ScoredOnID = soi;
        ScoredOnName = son;
    }

    public int ID;
    public long ScoreAmount;
    public LocalDateTime Timestamp;
    public int UserID;
    public String UserName;
    public int ScoredOnID;
    public String ScoredOnName;
}
