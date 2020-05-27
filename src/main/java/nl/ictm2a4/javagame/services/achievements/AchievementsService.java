package nl.ictm2a4.javagame.services.achievements;

import nl.ictm2a4.javagame.services.ApiService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AchievementsService extends ApiService {
    private String baseUrl = super.baseUrl + "/api/achievements";

    public Achievement getAchievement(int id){
        var response = sendRequest(baseUrl + "/" + id, "GET", "");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        var achivementJson = (JSONObject) response.getBody();

        return convertJsonToAchievement(achivementJson);
    }

    public List<Achievement> getAchievements(){
        var response = sendRequest(baseUrl, "GET", "");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToAchievementsList((JSONArray) response.getBody().get("Values"));
    }

    public List<Achievement> getAchievements(int uid){
        var response = sendRequest(baseUrl + "?uid=" + uid, "GET", "");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToAchievementsList((JSONArray) response.getBody().get("Values"));
    }

    public Achievement addAchievement(Achievement achievement){
        var achievementJson = convertAchievementToJson(achievement);
        var response = sendRequest(baseUrl, "POST", achievementJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response == null || response.getResponseCode() != 201) {
            return null;
        }

        return convertJsonToAchievement((JSONObject) response.getBody());
    }

    public Boolean updateAchievement(Achievement achievement){
        var achievementJson = convertAchievementToJson(achievement);
        var response = sendRequest(baseUrl + "/" + achievement.getId()   , "PUT", achievementJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response != null && response.getResponseCode() == 204){
            return true;
        }

        return false;
    }

    public Boolean addAchievementToUser(int achievementID, int userID) {
        var userAchievementJson = new JSONObject();
        userAchievementJson.put("userID", userID);
        userAchievementJson.put("achievementID", achievementID);

        var response = sendRequest(baseUrl + "/user", "POST", userAchievementJson.toString());

        if(response != null && response.getResponseCode() == 200){
            return true;
        }

        return false;
    }

    public Boolean removeAchievementFromUser(int achievementID, int userID) {
        var userAchievementJson = new JSONObject();
        userAchievementJson.put("userID", userID);
        userAchievementJson.put("achievementID", achievementID);

        var response = sendRequest(baseUrl + "/user", "DELETE", userAchievementJson.toString());

        if(response != null && response.getResponseCode() == 200){
            return true;
        }

        return false;
    }

    // JSON conversie

    private List<Achievement> convertJsonToAchievementsList(JSONArray achievementsJson){
        var achievements = new ArrayList<Achievement>();

        for(var achievement : achievementsJson){
            achievements.add(convertJsonToAchievement((JSONObject) achievement));
        }

        return achievements;
    }

    private Achievement convertJsonToAchievement(JSONObject achievementJson){
        return new Achievement(
                ((Long)achievementJson.get("id")).intValue(),
                (String)achievementJson.get("name"),
                (String)achievementJson.get("description")
        );
    }

    private JSONObject convertAchievementToJson(Achievement achievement){
        var levelJson = new JSONObject();
        levelJson.put("id", achievement.getId());
        levelJson.put("name", achievement.getName());
        levelJson.put("description", achievement.getDescription());

        return levelJson;
    }
}
