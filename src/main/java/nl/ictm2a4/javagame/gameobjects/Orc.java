package nl.ictm2a4.javagame.gameobjects;

import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.JSONLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.Level;
import nl.ictm2a4.javagame.screens.LevelEditor;
import nl.ictm2a4.javagame.uicomponents.HUD;

import java.awt.*;

public class Orc extends Mob {

    private int hitInterval = 200; // ms
    private long prev, startHitTime;
    private boolean startHit;

    @JSONLoader(JSONString = "orcs")
    public Orc(Integer gridX, Integer gridY) {
        super(((gridX * LevelLoader.GRIDWIDTH) + 4),
            ((gridY * LevelLoader.GRIDHEIGHT) + 2),
            16, 20, 48, 45, 80);
    }

    @Override
    public void draw(Graphics g) {
        super.draw(g);
        if (this.isAlive() && LevelLoader.getInstance().getCurrentLevel().isPresent()) {
            g.drawImage(FileLoader.getInstance().getOrcImage(getStatus(), getDirection(), getCurrentImage()),
                getX() - 8, getY() - 30,
                LevelLoader.getInstance().getCurrentLevel().get());
        }
    }

    @Override
    public boolean checkCollideSingle(GameObject otherGameObject, int x, int y) {
        if (!(otherGameObject instanceof Player) || !isAlive())
            return false;

        boolean result = super.withinRange(x, y);

        if (result) {
            if (prev + (hitInterval * 10) < System.currentTimeMillis()) {
                prev = System.currentTimeMillis();
                startHitTime = prev + 300;
                startHit = true;
            }
        }

        if (result) {
            if (startHitTime < System.currentTimeMillis() && startHit) {
                HUD.getInstance().removeHealth(getDamage());
                setStatus(PlayerStatus.FIGHTING);
                startHit = false;
            }
        }

        return result;
    }

    public static LevelEditor.LevelEditorItem getLevelEditorSpecs() {
        return new LevelEditor.LevelEditorItem(Orc.class, FileLoader.getInstance().getOrcImage(PlayerStatus.IDLE, PlayerStatus.Direction.RIGHT, 0)) {
            @Override
            public void onPlace(int mouseX, int mouseY) {
                super.onPlace(mouseX, mouseY);
                Level level = LevelEditor.getInstance().getLevel();
                if (!this.allowedToPlace(mouseX, mouseY)) return;
                level.addGameObject(new Orc(gridX, gridY));
            }
        }.setRequireGround(true);
    }
}
