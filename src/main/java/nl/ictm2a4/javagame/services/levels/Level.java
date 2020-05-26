package nl.ictm2a4.javagame.services.levels;

public class Level {
    public Level(int id, String name, String description, String content, int creatorid, String creatorUserName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.content = content;
        creatorID = creatorid;
        this.creatorUserName = creatorUserName;
    }

    public int id;
    public String name;
    public String description;
    public String content;
    public int creatorID;
    public String creatorUserName;
}
