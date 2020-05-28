package nl.ictm2a4.javagame.services.scores;

import java.time.LocalDateTime;

public class Score {

    private int id, userID, scoredOnID;
    private long scoreAmount;
    private LocalDateTime timestamp;
    private String userName, scoredOnName;

    /**
     *
     * @param id, The ID of the score
     * @param sa, The amount scored
     * @param ts, The score's timestamp
     * @param uid, The ID of the user associated with the score
     * @param un, The name of the user associated with the score
     * @param soi, The ID of the level associated with the score
     * @param son, The name of the level associated with the score
     */
    public Score(int id, long sa, LocalDateTime ts, int uid, String un, int soi, String son){
        this.id = id;
        scoreAmount = sa;
        timestamp = ts;
        userID = uid;
        userName = un;
        scoredOnID = soi;
        scoredOnName = son;
    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public int getScoredOnID() {
        return scoredOnID;
    }

    public long getScoreAmount() {
        return scoreAmount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getUserName() {
        return userName;
    }

    public String getScoredOnName() {
        return scoredOnName;
    }
}
