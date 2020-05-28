package nl.ictm2a4.javagame.services.levels;

public class Level {

    private int id, creatorID;
    private String name, description, content, creatorUserName;

    /**
     *
     * @param id, The ID of the Level
     * @param name, The name of the level
     * @param description, The description of the level
     * @param content, The content of the level, must be in JSON format
     * @param creatorid, The ID of the level's creator
     * @param creatorUserName, The username of the level's creator
     */
    public Level(int id, String name, String description, String content, int creatorid, String creatorUserName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.content = content;
        creatorID = creatorid;
        this.creatorUserName = creatorUserName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public int getCreatorID() {
        return creatorID;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
