package nl.ictm2a4.javagame.services.scores;

import nl.ictm2a4.javagame.services.ApiService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScoreService extends ApiService {
    private String baseUrl = super.baseUrl + "/api/scores";

    public Score getScore(int id){
        var response = sendRequest(baseUrl + "/" + id, "GET","");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var scoreJson = (JSONObject) response.body;

        return convertJsonToScore(scoreJson);
    }

    public List<Score> getScores(){
        var response = sendRequest(baseUrl, "GET","");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var scores = convertJsonToScoresList((JSONArray) response.body.get("Values"));

        return scores;
    }

    public List<Score> getScores(int limit){
        var response = sendRequest(baseUrl, "GET","");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var scores = convertJsonToScoresList((JSONArray) response.body.get("Values"));

        return scores;
    }

    public List<Score> getScores(int limit, String sort){
        var response = sendRequest(baseUrl + "?limit=" + limit + "&sort=" + sort, "GET","");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var scores = convertJsonToScoresList((JSONArray) response.body.get("Values"));

        return scores;
    }

    public List<Score> getScoresPerLevel(int lid){
        var response = sendRequest(baseUrl + "?lid=" + lid, "GET","");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var scores = convertJsonToScoresList((JSONArray) response.body.get("Values"));

        return scores;
    }

    public Score addScore(Score score){

        var scoreJson = convertScoreToJson(score);
        var response = sendRequest(baseUrl, "POST", scoreJson.toString());

        if(response == null || response.responseCode != 201){
            return null;
        }

        var returnScoreJson = (JSONObject) response.body;

        return convertJsonToScore(returnScoreJson);
    }

    // JSON conversie

    private List<Score> convertJsonToScoresList(JSONArray scoresJson){
        var scores = new ArrayList<Score>();

        for(var score : scoresJson){
            scores.add(convertJsonToScore((JSONObject) score));
        }

        return scores;
    }

    private Score convertJsonToScore(JSONObject scoreJson){
        return new Score(
                ((Long) scoreJson.get("id")).intValue(),
                ((Long)scoreJson.get("scoreAmount")).intValue(),
                LocalDateTime.parse((String) scoreJson.get("timestamp")),
                ((Long) scoreJson.get("userID")).intValue(),
                (String) scoreJson.get("userName"),
                ((Long) scoreJson.get("scoredOnID")).intValue(),
                (String) scoreJson.get("scoredOnName")
        );
    }

    private JSONObject convertScoreToJson(Score score){
        var scoreJson = new JSONObject();
        scoreJson.put("id", score.id);
        scoreJson.put("scoreAmount", score.scoreAmount);
        scoreJson.put("timestamp", "1996-12-12T12:12:23"); //, score.Timestamp); Formattering gaat niet helemaal goed
        scoreJson.put("userID", score.userID);
        scoreJson.put("scoredOnID", score.scoredOnID);

        return scoreJson;
    }
}
