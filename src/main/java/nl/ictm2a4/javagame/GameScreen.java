package nl.ictm2a4.javagame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameScreen extends JFrame implements ActionListener, KeyListener {

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

        addKeyListener(this);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Main.player.checkMove(e);

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }




}
