package nl.ictm2a4.javagame.services.levels;

import nl.ictm2a4.javagame.services.ApiService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LevelService extends ApiService {
    private String baseUrl = super.baseUrl + "/api/levels";

    public Level getLevel(int id){
        var response = sendRequest(baseUrl + "/" + id, "GET", "");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var levelJson = (JSONObject) response.body;

        return convertJsonToLevel(levelJson);
    }

    public List<Level> getLevels(){
        var response = sendRequest(baseUrl, "GET", "");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var levels = convertJsonToLevelsList((JSONArray) response.body.get("Values"));

        return levels;
    }

    public Level addLevel(Level level){
        var levelJson = convertLevelToJson(level);
        var response = sendRequest(baseUrl, "POST", levelJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response == null || response.responseCode != 201) {
            return null;
        }

        var returnLevelJson = (JSONObject) response.body;

        return convertJsonToLevel(returnLevelJson);
    }

    public Boolean UpdateLevel(Level level){
        var levelJson = convertLevelToJson(level);
        var response = sendRequest(baseUrl + "/" + level.ID, "PUT", levelJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response != null && response.responseCode == 204){
            return true;
        }

        return false;
    }

    // JSON conversie

    private List<Level> convertJsonToLevelsList(JSONArray levelJson){
        var levels = new ArrayList<Level>();

        for(var level : levelJson){
            levels.add(convertJsonToLevel((JSONObject) level));
        }

        return levels;
    }

    private Level convertJsonToLevel(JSONObject levelJson){
        return new Level(
                ((Long)levelJson.get("id")).intValue(),
                (String)levelJson.get("name"),
                (String)levelJson.get("description"),
                (String)levelJson.get("content"),
                ((Long)levelJson.get("creatorID")).intValue(),
                (String)levelJson.get("creatorUserName")
        );
    }

    private JSONObject convertLevelToJson(Level level){
        var levelJson = new JSONObject();
        levelJson.put("id", level.ID);
        levelJson.put("name", level.Name);
        levelJson.put("description", level.Description);
        levelJson.put("content", level.Content);
        levelJson.put("creatorID", level.CreatorID);
        levelJson.put("creatorUserName", level.CreatorUserName);

        return levelJson;
    }
}
