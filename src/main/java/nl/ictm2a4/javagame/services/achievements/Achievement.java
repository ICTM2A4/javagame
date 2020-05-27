package nl.ictm2a4.javagame.services.achievements;

public class Achievement {

    private int id;
    private String name, description;

    public Achievement(int _id, String _name, String _description){
        id = _id;
        name = _name;
        description = _description;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
