package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.achievement.Achievement;
import nl.ictm2a4.javagame.achievement.AchievementHandler;
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

    private CButton a1, back;

    public AchievementScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;

        this.setPreferredSize(new Dimension(360, 360));

        for (Achievement achievement: AchievementHandler.getInstance().getAchievements()) {
            a1 = new CButton("");
            JCheckBox checkBox = new JCheckBox(achievement.getClass().getSimpleName());
            checkBox.setEnabled(false);
            checkBox.setForeground(Color.BLACK);
            a1.add(checkBox);
            checkBox.setBackground(new Color(146, 115, 63));
            checkBox.setSelected(LevelLoader.getInstance().getCurrentLevel().get().isRenderShadows());
            if (achievement.isAchieved()) {
                checkBox.isSelected();
            }
            add(a1, gbc);
        }

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
