package nl.ictm2a4.javagame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameScreen extends JFrame implements ActionListener {

    private Level level;
    private int width, height;

    public GameScreen(Level level) {
        super("JavaGame - " + level.getName());

        this.level = level;
        this.width = Main.width;
        this.height = Main.height;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new BorderLayout());
        setContentPane(level);
        pack();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
