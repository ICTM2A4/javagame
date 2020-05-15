package nl.ictm2a4.javagame.services.levels;

public class Level {
    public Level(int id, String name, String description, String content, int creatorid, String creatorUserName){
        ID = id;
        Name = name;
        Description = description;
        Content = content;
        CreatorID = creatorid;
        CreatorUserName = creatorUserName;
    }

    public int ID;
    public String Name;
    public String Description;
    public String Content;
    public int CreatorID;
    public String CreatorUserName;
}
