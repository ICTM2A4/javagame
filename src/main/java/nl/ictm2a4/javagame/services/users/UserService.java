package nl.ictm2a4.javagame.services.users;

import nl.ictm2a4.javagame.services.ApiService;
import org.json.simple.JSONObject;

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

    // JSON conversie
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
