package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JFrame implements ActionListener, KeyListener {

    private static GameScreen instance;
    private String title = "Java game";
    private List<Integer> pressedKeys;

    public GameScreen() {
        setTitle(title);

        instance = this;

        new FileLoader();
        new LevelLoader();
        pressedKeys = new ArrayList<Integer>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new BorderLayout());
        setContentPane(new MainMenu());
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(pressedKeys.contains(e.getKeyCode())){
            pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
        }
    }

    /**
     * Append text to the screen title, seperated by an -
     * @param append Text to append
     */
    public void addTitle(String append) {
        setTitle(title + " - " + append);
    }

    /**
     * Reset the screen title to the default title
     */
    public void resetTitle() {
        setTitle(title);
    }

    /**
     * Get the instance of the GameScreen
     * @return GameScreen instance
     */
    public static GameScreen getInstance() {
        return instance;
    }

    public List<Integer> getPressedKeys() {
        return pressedKeys;
    }
}