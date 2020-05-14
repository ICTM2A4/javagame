package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;

import javax.swing.*;
import java.awt.*;

public class AchievementPopup extends JPanel {

    private String tekst;
    private int startY = -40;

    public AchievementPopup(String tekst) {
        super();

        this.tekst = tekst;

        setPreferredSize(new Dimension(140,40));
        setLayout(new FlowLayout());

        setVisible(true);

        this.tekst = tekst;

        setBounds(LevelLoader.WIDTH, -40, 140, 40);
        setOpaque(false);
        GameScreen.getInstance().getFixed().add(this, JLayeredPane.DRAG_LAYER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("achievement-background.png"), 0, 0, this);
        g.drawString("Achievement earned!", 12, 20);
        g.drawString(this.tekst, 12,30);
    }

    public boolean render(int frame) {
        int startX = LevelLoader.WIDTH;

        if (frame < 30)
            startY += 2;
        if (frame > 110)
            startY -= 2;

        setBounds(startX, startY, 140, 40);
        return (frame > 150);
    }

}
