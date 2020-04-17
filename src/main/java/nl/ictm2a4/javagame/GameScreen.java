package nl.ictm2a4.javagame;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

public class GameScreen extends JFrame implements ActionListener, KeyListener {

    private LevelLoader levelLoader;
    private FileLoader fileLoader;
    private String title = "Java game";

    public GameScreen() {
        setTitle(title);

        this.levelLoader = new LevelLoader();
        this.fileLoader = new FileLoader();

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
        LevelLoader.getInstance().getCurrentLevel().ifPresent(
            level -> level.getPlayer().ifPresent(
                player -> player.checkMove(e)));
    }

    @Override
    public void keyReleased(KeyEvent e) {

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
