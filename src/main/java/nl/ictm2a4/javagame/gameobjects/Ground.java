package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.LevelEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ground extends GameObject {

    @JSONLoader(JSONString = "ground")
    public Ground(Integer gridX, Integer gridY) {
        super(gridX * LevelLoader.GRIDWIDTH, gridY * LevelLoader.GRIDHEIGHT, LevelLoader.GRIDWIDTH, LevelLoader.GRIDHEIGHT);
        setCollidable(false);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(FileLoader.getInstance().getGroundTile(connectedFacesSum(this.hasConnectedFaces(Ground.class))),
            getX(), getY(),
            LevelLoader.getInstance().getCurrentLevel().get());
    }

    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(Ground.class, FileLoader.getInstance().getGroundTile(15)) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX,mouseY);
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                if (this.gridX > 0 &&
                    (LevelLoader.WIDTH / LevelLoader.GRIDWIDTH) - 1 > gridX &&
                    gridY > 0 &&
                    (LevelLoader.HEIGHT / LevelLoader.GRIDHEIGHT) - 1 > gridY)
                    LevelEditor.getInstance().getLevel().addGameObject(new Ground(gridX, gridY));
            }

            @Override
            public void onDrag(int mouseX, int mouseY) {
                super.onDrag(mouseX,mouseY);
                this.onPlace(mouseX, mouseY);
            }
        };
    }
}
