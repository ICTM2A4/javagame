package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndScreen extends JPanel implements ActionListener {

    private JButton nextLevel, backToMainMenu;
    private JLabel score;
    int levelId = LevelLoader.getInstance().getCurrentLevel().get().getId();
    long points = LevelLoader.getInstance().getCurrentLevel().get().getScore();

    public EndScreen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(Component.LEFT_ALIGNMENT);
        this.setPreferredSize(new Dimension(360, 360));

        Box.Filler hFill1 = new Box.Filler(new Dimension(0,5),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill2 = new Box.Filler(new Dimension(0,5),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill0 = new Box.Filler(new Dimension(0,25),
                new Dimension(0, 50),
                new Dimension(0, 75));

        add(hFill0);
        score = new JLabel("score: " + points / 1000);
        add(score);
        add(hFill1);
        nextLevel = new JButton("Next Level");
        nextLevel.addActionListener(this);
        add(nextLevel);
        nextLevel.setVisible(LevelLoader.defaultLevelAmount > levelId + 1);
        add(hFill2);
        backToMainMenu = new JButton("Back to Main menu");
        backToMainMenu.addActionListener(this);
        add(backToMainMenu);

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level.png"), 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e ) {

        if(e.getSource() == nextLevel) {
            LevelLoader.getInstance().startLevel(levelId + 1);
        }
        if(e.getSource() == backToMainMenu) {
            LevelLoader.getInstance().stopLevel();
        }
    }
}
