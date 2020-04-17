package nl.ictm2a4.javagame;

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
import java.util.Optional;

public class GameScreen extends JFrame implements ActionListener, KeyListener {

    private LevelLoader levelLoader;
    private FileLoader fileLoader;
    private String title = "Java game";
    public List<Integer> pressedKeys;

    public GameScreen() {
        setTitle(title);

        this.levelLoader = new LevelLoader();
        this.fileLoader = new FileLoader();
        pressedKeys = new ArrayList<Integer>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        setLayout(new BorderLayout());

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

        LevelLoader.getInstance().getCurrentLevel().ifPresent(
            level -> level.getPlayer().ifPresent(
                player -> player.checkMove(e)));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(pressedKeys.contains(e.getKeyCode())){
            pressedKeys.remove(Integer.valueOf(e.getKeyCode()));
        }
    }

    public Optional<Level> getLevel() {
        return levelLoader.getCurrentLevel();
    }

    public void addTitle(String append) {
        setTitle(title + " - " + append);
    }
    public void resetTitle() {
        setTitle(title);
    }
}