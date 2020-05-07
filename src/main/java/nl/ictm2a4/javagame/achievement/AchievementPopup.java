package nl.ictm2a4.javagame.achievement;

import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;
import nl.ictm2a4.javagame.screens.Level;

import javax.swing.*;
import java.awt.*;

public class AchievementPopup extends JPanel {

    private String tekst;

    public AchievementPopup(String tekst) {
        super();

        this.tekst = tekst;

        setPreferredSize(new Dimension(100,30));
        setLayout(new FlowLayout());

        setBounds(LevelLoader.WIDTH, 30, 100, 30);

        setVisible(true);

        add(new Label(tekst));
        System.out.println(tekst);
    }

    public boolean render(int frame) {

        int startX = LevelLoader.WIDTH;
        int startY = 30;

        if (frame < 30)
            startX -= frame;
        if (frame > 40)
            startX = startX - (frame - 30);

        setBounds(startX, startY, 100, 30);

        return true;
    }

}
