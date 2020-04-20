package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel implements ActionListener {

    private JButton start, selectlevel, levelbuilder, exit;

    public MainMenu() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(Component.LEFT_ALIGNMENT);
        this.setPreferredSize(new Dimension(360, 360));

        Box.Filler hFill1 = new Box.Filler(new Dimension(0,5),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill2 = new Box.Filler(new Dimension(0,5),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill3 = new Box.Filler(new Dimension(0,5),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill0 = new Box.Filler(new Dimension(0,25),
                new Dimension(0, 50),
                new Dimension(0, 75));

        add(hFill0);
        start = new JButton("Start");
        start.addActionListener(this);
        add(start);
        add(hFill1);
        selectlevel = new JButton("Select Level");
        selectlevel.addActionListener(this);
        add(selectlevel);
        add(hFill2);
        levelbuilder = new JButton("Level Builder");
        levelbuilder.addActionListener(this);
        add(levelbuilder);
        add(hFill3);
        exit = new JButton("Exit");
        exit.addActionListener(this);
        add(exit);

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level.png"), 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start) {
            LevelLoader.getInstance().startLevel(1);
            GameScreen.getInstance().setLocationRelativeTo(null);
        }
        if(e.getSource() == selectlevel) {
            System.out.println("Level Select start op");
        }
        if(e.getSource() == levelbuilder) {
            GameScreen.getInstance().setPanel(new LevelEditor(), "Level Editor");
        }
        if(e.getSource() == exit) {
            System.exit(0);
        }
    }
}
