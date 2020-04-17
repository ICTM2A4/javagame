package nl.ictm2a4.javagame;

import nl.ictm2a4.javagame.screens.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame implements ActionListener {

    private JButton start, selectlevel, levelbuilder, exit;
    private static GameScreen screen;

    public MainMenu() {
        setLayout(new FlowLayout());
        setSize(320, 360);
        setTitle("Main Menu");

        start = new JButton("Start");
        start.addActionListener(this);
        add(start);
        selectlevel = new JButton("Select Bevel");
        selectlevel.addActionListener(this);
        add(selectlevel);
        levelbuilder = new JButton("Level Builder");
        levelbuilder.addActionListener(this);
        add(levelbuilder);
        exit = new JButton("Exit");
        exit.addActionListener(this);
        add(exit);

        setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start) {
            System.out.println("Level Start");
        }
        if(e.getSource() == selectlevel) {
            System.out.println("Level Select start op");
        }
        if(e.getSource() == levelbuilder) {
            System.out.println("Level builder start op");
        }
        if(e.getSource() == exit) {
            System.exit(0);
        }
    }
}
