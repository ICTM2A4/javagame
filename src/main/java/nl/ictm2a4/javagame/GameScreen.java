package nl.ictm2a4.javagame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JFrame implements ActionListener, KeyListener {

    private Level level;
    private int width, height;
    public List<Integer> pressedKeys;


    public GameScreen(Level level) {
        super("JavaGame - " + level.getName());

        this.level = level;
        this.width = Main.width;
        this.height = Main.height;
        pressedKeys = new ArrayList<Integer>();

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
        if(!pressedKeys.contains(e.getKeyCode())){
            pressedKeys.add(e.getKeyCode());
        }

        Main.player.checkMove(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(pressedKeys.contains(e.getKeyCode())){
            pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
        }
    }

    public Level getLevel() {
        return level;
    }
}
