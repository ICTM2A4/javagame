package nl.ictm2a4.javagame.services.users;

public class User {

    private int id;
    private String username, password, token;

    /**
     *
     * @param _id: The user's ID, should only be filled in by the authentication service
     * @param _username: The user's name
     * @param _password: The user's password
     * @param _token: The user's token, should only be filled in by the authentication service
     */
    public User(int _id, String _username, String _password, String _token){
        id = _id;
        username = _username;
        password = _password;
        token = _token;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return (token != null) ? token : "";
    }
}
