package nl.ictm2a4.javagame.services.users;

import nl.ictm2a4.javagame.services.ApiService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserService extends ApiService {
    private String baseUrl = super.baseUrl + "/api/users";

    public User authenticate(String username, String password){
        var userJson = convertUserToJson(new User(0, username, password, ""));

        var response = sendRequest(baseUrl + "/authenticate", "POST", userJson.toString());

        if(response == null || response.getResponseCode() != 200) {
            return null;
        }

        return convertJsonToUser(response.getBody());
    }

    public User register(User user){
        var userJson = convertUserToJson(user);

        var response = sendRequest(baseUrl + "/register", "POST", userJson.toString());

        if(response == null || response.getResponseCode() != 200) {
            return null;
        }

        return convertJsonToUser(response.getBody());
    }

    public List<User> getUsers(){
        var response = sendRequest(baseUrl, "GET","");

        if(response == null || response.getResponseCode() != 200){
            return null;
        }

        return convertJsonToUsersList((JSONArray) response.getBody().get("Values"));
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
        userJson.put("id", user.getId());
        userJson.put("username", user.getUsername());
        userJson.put("password", user.getPassword());
        userJson.put("token", user.getToken());


        return userJson;
    }
}
