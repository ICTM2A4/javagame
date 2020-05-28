package nl.ictm2a4.javagame.services.achievements;

public class Achievement {

    private int id;
    private String name, description;

    /**
     *
     * @param _id, The ID of the achievement
     * @param _name, The name of the achievement
     * @param _description, The description of the achievement
     */
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
