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

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        var levelJson = (JSONObject) response.getBody();

        return convertJsonToLevel(levelJson);
    }

    public List<Level> getLevels(){
        var response = sendRequest(baseUrl, "GET", "");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToLevelsList((JSONArray) response.getBody().get("Values"));
    }

    public Level addLevel(Level level){
        var levelJson = convertLevelToJson(level);
        var response = sendRequest(baseUrl, "POST", levelJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response == null || response.getResponseCode() != 201) {
            return null;
        }

        var returnLevelJson = (JSONObject) response.getBody();

        return convertJsonToLevel(returnLevelJson);
    }

    public Boolean updateLevel(Level level){
        var levelJson = convertLevelToJson(level);
        var response = sendRequest(baseUrl + "/" + level.getId(), "PUT", levelJson.toString());

        // TODO: Afdwingen dat content in JSON formaat is

        if(response != null && response.getResponseCode() == 204){
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
        levelJson.put("id", level.getId());
        levelJson.put("name", level.getName());
        levelJson.put("description", level.getDescription());
        levelJson.put("content", level.getContent());
        levelJson.put("creatorID", level.getCreatorID());
        levelJson.put("creatorUserName", level.getCreatorUserName());

        return levelJson;
    }
}
