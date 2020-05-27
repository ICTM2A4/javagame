package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.services.achievements.Achievement;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AchievementScreen extends JPanel implements ActionListener {

    private CButton back;

    public AchievementScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;

        this.setPreferredSize(new Dimension(360, 360));

       var gameScreen = GameScreen.getInstance();
        var achievedAchievements = gameScreen.getAchievedAchievements();
        var achievements = gameScreen.getAchievements();

        JPanel achievementPanel = new JPanel();
        achievementPanel.setLayout(new GridLayout(0, 2, 5, 30));
        add(achievementPanel);
        achievementPanel.setBackground(new Color(0,0,0,0));

        for(Achievement achievement : achievements){
            JButton achievementButton = new JButton("");
            achievementButton.setPreferredSize(new Dimension(120,120));
            achievementButton.setBackground(new Color(0,0,0,0));
            achievementButton.setBorder(BorderFactory.createEmptyBorder());
            JCheckBox checkBox = new JCheckBox(achievement.getName());
            checkBox.setEnabled(false);
            checkBox.setBackground(new Color(0,0,0,0));
            checkBox.setSelected(achievedAchievements.stream().anyMatch(a -> a.getId() == achievement.getId()));
            achievementButton.add(checkBox);

            achievementPanel.add(achievementButton, gbc);
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
