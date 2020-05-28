package nl.ictm2a4.javagame.services.scores;

import nl.ictm2a4.javagame.services.ApiService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScoreService extends ApiService {
    private String baseUrl = super.baseUrl + "/api/scores";

    /** Gets a specific score from the API by the ID
     *
     * @param id
     * @return The score
     */
    public Score getScore(int id){
        var response = sendRequest(baseUrl + "/" + id, "GET","");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        var scoreJson = (JSONObject) response.getBody();

        return convertJsonToScore(scoreJson);
    }

    /** Gets the list of all the scores in the API
     *
     * @return A list of all the scores in the API
     */
    public List<Score> getScores(){
        var response = sendRequest(baseUrl, "GET","");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToScoresList((JSONArray) response.getBody().get("Values"));
    }

    /** Gets a list of scores in the API
     *
     * @param limit, The maximum amount of entries in the list
     * @return The list of scores
     */
    public List<Score> getScores(int limit){
        var response = sendRequest(baseUrl, "GET","");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToScoresList((JSONArray) response.getBody().get("Values"));
    }

    /** Gets a list of scores in the API
     *
     * @param limit, The maximum amount of entries in the list
     * @param sort, Sorting direction, "ASC" or "DESC"
     * @return The list of scores
     */
    public List<Score> getScores(int limit, String sort){
        var response = sendRequest(baseUrl + "?limit=" + limit + "&sort=" + sort, "GET","");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToScoresList((JSONArray) response.getBody().get("Values"));
    }

    /** Gets a list of the scores achieved on a given level by the level's ID
     *
     * @param lid, the level's ID
     * @return The list of scores
     */
    public List<Score> getScoresPerLevel(int lid){
        var response = sendRequest(baseUrl + "?lid=" + lid, "GET","");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToScoresList((JSONArray) response.getBody().get("Values"));
    }

    /** Adds the given score to the API
     *
     * @param score
     * @return The added score if succesful
     */
    public Score addScore(Score score){

        var scoreJson = convertScoreToJson(score);
        var response = sendRequest(baseUrl, "POST", scoreJson.toString());

        if(response == null || response.getResponseCode() != 201){
            return null;
        }

        var returnScoreJson = (JSONObject) response.getBody();

        return convertJsonToScore(returnScoreJson);
    }

    // JSON conversie

    /** Converts a JSONarray of scores to a list of scores
     *
     * @param scoresJson
     * @return the list of scores
     */
    private List<Score> convertJsonToScoresList(JSONArray scoresJson){
        var scores = new ArrayList<Score>();

        for(var score : scoresJson){
            scores.add(convertJsonToScore((JSONObject) score));
        }

        return scores;
    }

    /** Converts a JSONObject of a score to a score
     *
     * @param scoreJson
     * @return The score
     */
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

    /** Converts a score to a JSONObject of the score
     *
     * @param score
     * @return the JSONObject of the score
     */
    private JSONObject convertScoreToJson(Score score){
        var scoreJson = new JSONObject();
        scoreJson.put("id", score.getId());
        scoreJson.put("scoreAmount", score.getScoreAmount());
        scoreJson.put("timestamp", "1996-12-12T12:12:23"); //, score.Timestamp); Formattering gaat niet helemaal goed //TODO: wutt
        scoreJson.put("userID", score.getUserID());
        scoreJson.put("scoredOnID", score.getScoredOnID());

        return scoreJson;
    }
}
