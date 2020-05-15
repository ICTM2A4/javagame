package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.cachievements.FirstDoorOpened;
import nl.ictm2a4.javagame.cachievements.LastLevelAchieved;
import nl.ictm2a4.javagame.cachievements.LevelOneAchieved;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AchievementScreen extends JPanel implements ActionListener {

    private CButton a1, a2, a3, back;
    private boolean FirstLevelAchieved, LastLevelAchieved, FirstDoorOpened;

    public AchievementScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;

        this.setPreferredSize(new Dimension(360, 360));

        LevelOneAchieved achievement1 = new LevelOneAchieved();
        LastLevelAchieved achievement2 = new LastLevelAchieved();
        FirstDoorOpened achievement3 = new FirstDoorOpened();

        if (achievement1.isAchieved()) {
            FirstLevelAchieved = true;
        }
        if (achievement2.isAchieved()) {
            LastLevelAchieved = true;
        }
        if (achievement3.isAchieved()) {
            FirstDoorOpened = true;
        }

        a1 = new CButton("");
        JCheckBox checkBox = new JCheckBox("First level achieved");
        a1.add(checkBox);
        checkBox.setBackground(new Color(146, 115, 63));
        checkBox.setSelected(LevelLoader.getInstance().getCurrentLevel().get().isRenderShadows());
        if (FirstLevelAchieved) {
            checkBox.isSelected();
        }
        add(a1, gbc);

        a2 = new CButton("");
        JCheckBox checkBox2 = new JCheckBox("Last level achieved");
        a2.add(checkBox2);
        checkBox2.setBackground(new Color(146, 115, 63));
        checkBox2.setSelected(LevelLoader.getInstance().getCurrentLevel().get().isRenderShadows());
        if (LastLevelAchieved) {
            checkBox2.isSelected();
        }
        add(a2, gbc);

        a3 = new CButton("");
        JCheckBox checkBox3 = new JCheckBox("First door opened");
        a3.add(checkBox3);
        checkBox3.setBackground(new Color(146, 115, 63));
        checkBox3.setSelected(LevelLoader.getInstance().getCurrentLevel().get().isRenderShadows());
        if (FirstDoorOpened) {
            checkBox3.isSelected();
        }
        add(a3, gbc);

        back = new CButton("Back");
        back.addActionListener(this);
        add(back, gbc);

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level.png"), 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            GameScreen.getInstance().setPanel(new MainMenu());
        }
    }
}
