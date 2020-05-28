package nl.ictm2a4.javagame.services.achievements;

import nl.ictm2a4.javagame.services.ApiService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AchievementsService extends ApiService {
    private String baseUrl = super.baseUrl + "/api/achievements";

    /** Gets a specific achievement from the API by the ID
     *
     * @param id
     * @return The achievement
     */
    public Achievement getAchievement(int id){
        var response = sendRequest(baseUrl + "/" + id, "GET", "");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        var achivementJson = (JSONObject) response.getBody();

        return convertJsonToAchievement(achivementJson);
    }

    /** Gets the list of all the achievements in the API
     *
     * @return A list of all the achievements in the API
     */
    public List<Achievement> getAchievements(){
        var response = sendRequest(baseUrl, "GET", "");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToAchievementsList((JSONArray) response.getBody().get("Values"));
    }

    /** Gets the achieved achievements of a user by their user ID.
     *
     * @param uid
     * @return The list of achievements achieved by the user
     */
    public List<Achievement> getAchievements(int uid){
        var response = sendRequest(baseUrl + "?uid=" + uid, "GET", "");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToAchievementsList((JSONArray) response.getBody().get("Values"));
    }

    /** Adds the given achievement to the API
     *
     * @param achievement
     * @return the added achievement if succesful, null if unsuccesful
     */
    public Achievement addAchievement(Achievement achievement){
        var achievementJson = convertAchievementToJson(achievement);
        var response = sendRequest(baseUrl, "POST", achievementJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response == null || response.getResponseCode() != 201) {
            return null;
        }

        return convertJsonToAchievement((JSONObject) response.getBody());
    }

    /** Updates the given achievement in the API
     *
     * @param achievement
     * @return true if succesful, false if unsuccesful
     */
    public Boolean updateAchievement(Achievement achievement){
        var achievementJson = convertAchievementToJson(achievement);
        var response = sendRequest(baseUrl + "/" + achievement.getId()   , "PUT", achievementJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response != null && response.getResponseCode() == 204){
            return true;
        }

        return false;
    }

    /** Adds an achievement to a user in the API
     *
     * @param achievementID
     * @param userID
     * @return true if succesful, false if unsuccesful
     */
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

    /** Adds an achievement to a user in the API
     *
     * @param achievementID
     * @param userID
     * @return true if succesful, false if unsuccesful
     */
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

    // JSON conversion

    /** Converts a JSONarray of achievements to a list of achievements
     *
     * @param achievementsJson
     * @return the list of achievements
     */
    private List<Achievement> convertJsonToAchievementsList(JSONArray achievementsJson){
        var achievements = new ArrayList<Achievement>();

        for(var achievement : achievementsJson){
            achievements.add(convertJsonToAchievement((JSONObject) achievement));
        }

        return achievements;
    }

    /** Converts a JSONObject of an achievement to an achievement
     *
     * @param achievementJson
     * @return The achievement
     */
    private Achievement convertJsonToAchievement(JSONObject achievementJson){
        return new Achievement(
                ((Long)achievementJson.get("id")).intValue(),
                (String)achievementJson.get("name"),
                (String)achievementJson.get("description")
        );
    }

    /** Converts an achievement to a JSONObject of the achievement
     *
     * @param achievement
     * @return the JSONObject of the achievement
     */
    private JSONObject convertAchievementToJson(Achievement achievement){
        var levelJson = new JSONObject();
        levelJson.put("id", achievement.getId());
        levelJson.put("name", achievement.getName());
        levelJson.put("description", achievement.getDescription());

        return levelJson;
    }
}
