package nl.ictm2a4.javagame.services.users;

import nl.ictm2a4.javagame.services.ApiService;
import nl.ictm2a4.javagame.services.scores.Score;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserService extends ApiService {
    private String baseUrl = super.baseUrl + "/api/users";

    public User authenticate(String username, String password){
        var userJson = convertUserToJson(new User(0, username, password, ""));

        var response = sendRequest(baseUrl + "/authenticate", "POST", userJson.toString());

        if(response == null || response.responseCode != 200) {
            return null;
        }

        return convertJsonToUser((JSONObject) response.body);
    }

    public User register(User user){
        var userJson = convertUserToJson(user);

        var response = sendRequest(baseUrl + "/register", "POST", userJson.toString());

        if(response == null || response.responseCode != 200) {
            return null;
        }

        return convertJsonToUser((JSONObject) response.body);
    }

    public List<User> getUsers(){
        var response = sendRequest(baseUrl, "GET","");

        if(response == null || response.responseCode != 200){
            return null;
        }

        var users = convertJsonToUsersList((JSONArray) response.body.get("Values"));

        return users;
    }

    // JSON conversie
    public List<User> convertJsonToUsersList(JSONArray usersJson){
        var users = new ArrayList<User>();

        for(var user : usersJson){
            users.add(convertJsonToUser((JSONObject) user));
        }

        return users;
    }

    private User convertJsonToUser(JSONObject userJson){
        return new User(
                ((Long)userJson.get("id")).intValue(),
                (String)userJson.get("userName"),
                (String)userJson.get("password"),
                (String)userJson.get("token")
        );
    }

    private JSONObject convertUserToJson(User user){
        var userJson = new JSONObject();
        userJson.put("id", user.id);
        userJson.put("username", user.username);
        userJson.put("password", user.password);
        userJson.put("token", user.token);


        return userJson;
    }
}
