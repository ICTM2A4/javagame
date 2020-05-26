package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.listeners.ScoreListener;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndScreen extends JPanel implements ActionListener {

    private CButton nextLevel, backToMainMenu, restart;
    int levelId = LevelLoader.getInstance().getCurrentLevel().get().getId();
    //long points = LevelLoader.getInstance().getCurrentLevel().get().getScore();

    public EndScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;
        this.setPreferredSize(new Dimension(360, 360));
        var points = ScoreListener.getInstance().scoreAmount;

        JLabel score = new JLabel("Score: " + points);
        score.setForeground(Color.WHITE);
        add(score, gbc);

        nextLevel = new CButton("Next Level");
        nextLevel.addActionListener(this);
        add(nextLevel, gbc);
        nextLevel.setVisible(LevelLoader.DEFAULTLEVELAMOUNT > levelId + 1);

        restart = new CButton("Restart");
        restart.addActionListener(this);
        add(restart, gbc);

        backToMainMenu = new CButton("Back to Main menu");
        backToMainMenu.addActionListener(this);
        add(backToMainMenu, gbc);

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level.png"), 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e ) {
        GameScreen.getInstance().achieveLevel(levelId + 1);

        if(e.getSource() == nextLevel) {
            LevelLoader.getInstance().startLevel(levelId + 1);
        }
        if(e.getSource() == backToMainMenu) {
            LevelLoader.getInstance().stopLevel();
        }
        if (e.getSource() == restart) {
            LevelLoader.getInstance().getCurrentLevel().get().restart();
            GameScreen.getInstance().setPanel(LevelLoader.getInstance().getCurrentLevel().get());
        }
    }
}
