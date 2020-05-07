package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.achievement.AchievementHandler;
import nl.ictm2a4.javagame.cachievements.LevelOneAchieved;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JFrame implements KeyListener, Runnable {

    public static final String GAMENAME = "JavaGame";

    private Thread thread;
    private  boolean isRunning;
    private int fps = 30;
    private long targetTime = 1000 / fps;

    private static GameScreen instance;
    private String title = GAMENAME;
    private List<Integer> pressedKeys;
    public static JPanel fixed;
    private List<Integer> achievedList;

    public GameScreen() {
        setTitle(title);

        instance = this;

        new FileLoader();
        new LevelLoader();
        pressedKeys = new ArrayList<>();
        setBackground(Color.BLACK);

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        fixed = new JPanel();
        fixed.setLayout(new GridBagLayout());
        fixed.setBackground(Color.BLACK);
        getContentPane().add(fixed);

        //TODO: paint fps

        setPanel(new MainMenu());
        LevelLoader.getInstance().loadLevel(0);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        addKeyListener(this);
        setVisible(true);

        achievedList = new ArrayList<>();
        achievedList.add(0);

        registerAchievements();

        this.start();
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
     * Reset the screen title to the default title
     */
    public void resetTitle() {
        setTitle(title);
    }

    /**
     * Set the panel of the GameScreen and update the title
     * @param panel The new JPanel to set
     * @param title The title to append to the gametitle
     */
    @Deprecated
    public void setPanel(JPanel panel, String title) {
        fixed.removeAll();
        fixed.add(panel);

        fixed.revalidate();

        requestFocus();
        fixed.repaint();
    }

    /**
     * Set the panel of the GameScreen and reset the title
     * @param panel The new JPanel to set
     */
    public void setPanel(JPanel panel) {
        setPanel(panel, "");
        resetTitle();
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

    public List<Integer> getAchievedList() {
        return this.achievedList;
    }

    public void achieveLevel(int id) {
        if (!this.achievedList.contains(id))
            this.achievedList.add(id);
    }

    @Override
    public void run() {
        long start, elapsed, wait;
        while(isRunning) {
            start = System.nanoTime();

            tick();

            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;

            if(wait <= 0) {
                wait = 5;
            }

            try {
                Thread.sleep(wait);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setFps(int fps) {
        this.fps = fps;
        this.targetTime = 1000 / this.fps;
    }

    public int getFps() {
        return fps;
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void tick() {
        LevelLoader.getInstance().tick();
    }

    private void registerAchievements() {
        new AchievementHandler();
        new LevelOneAchieved();
    }
}