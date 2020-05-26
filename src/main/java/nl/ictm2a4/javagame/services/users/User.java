package nl.ictm2a4.javagame.services.users;

public class User {

    public User(int _id, String _username, String _password, String _token){
        id = _id;
        username = _username;
        password = _password;
        token = _token;
    }

    public int id;
    public String username;
    public String password;
    public String token;
}
