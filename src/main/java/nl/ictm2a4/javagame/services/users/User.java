package nl.ictm2a4.javagame.services.users;

public class User {

    private int id;
    private String username, password, token;

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
