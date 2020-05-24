package nl.ictm2a4.javagame.services.achievements;

public class Achievement {
    public Achievement(int _id, String _name, String _description){
        id = _id;
        name = _name;
        description = _description;
    }

    public int id;
    public String name;
    public String description;
}
