package nl.ictm2a4.javagame.services.achievements;

import nl.ictm2a4.javagame.services.ApiService;
import nl.ictm2a4.javagame.services.levels.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AchievementsService extends ApiService {
    String baseUrl = "https://localhost:44320/api/achievements";

    public Achievement getAchievement(int id){
        var response = sendRequest(baseUrl + "/" + id, "GET", "");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var achivementJson = (JSONObject) response.body;

        return convertJsonToAchievement(achivementJson);
    }

    public List<Achievement> getAchievements(){
        var response = sendRequest(baseUrl, "GET", "");

        if(response == null || response.responseCode != 200){
            return null;
        }

        return convertJsonToAchievementsList((JSONArray) response.body.get("Values"));
    }

    public Achievement addAchievement(Achievement achievement){
        var achievementJson = convertAchievementToJson(achievement);
        var response = sendRequest(baseUrl, "POST", achievementJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response == null || response.responseCode != 201) {
            return null;
        }

        return convertJsonToAchievement((JSONObject) response.body);
    }

    public Boolean updateAchievement(Achievement achievement){
        var achievementJson = convertAchievementToJson(achievement);
        var response = sendRequest(baseUrl + "/" + achievement.id   , "PUT", achievementJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response != null && response.responseCode == 204){
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
        levelJson.put("id", achievement.id);
        levelJson.put("name", achievement.name);
        levelJson.put("description", achievement.description);

        return levelJson;
    }
}
