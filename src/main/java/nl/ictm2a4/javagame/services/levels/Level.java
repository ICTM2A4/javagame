package nl.ictm2a4.javagame.services.levels;

public class Level {

    private int id, creatorID;
    private String name, description, content, creatorUserName;

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
