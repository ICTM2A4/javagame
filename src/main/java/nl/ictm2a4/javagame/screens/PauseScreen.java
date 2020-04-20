package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseScreen extends JPanel implements ActionListener {

    private JButton resume, restart, quit;

    public PauseScreen() {
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
        resume = new JButton("Resume");
        resume.addActionListener(this);
        add(resume);
        add(hFill1);
        restart = new JButton("Restart");
        restart.addActionListener(this);
        add(restart);
        add(hFill2);
        quit = new JButton("Quit");
        quit.addActionListener(this);
        add(quit);

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level.png"), 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == resume) {
            LevelLoader.getInstance().resume();
            GameScreen.getInstance().setPanel(LevelLoader.getInstance().getCurrentLevel().get(), LevelLoader.getInstance().getCurrentLevel().get().getName());
        }
        if(e.getSource() == restart) {
            LevelLoader.getInstance().startLevel(1);
        }
        if(e.getSource() == quit) {
            LevelLoader.getInstance().stopLevel();
        }
    }
}
