package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.achievement.AchievementHandler;
import nl.ictm2a4.javagame.cachievements.FirstDoorOpened;
import nl.ictm2a4.javagame.cachievements.LastLevelAchieved;
import nl.ictm2a4.javagame.cachievements.LevelOneAchieved;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.uicomponents.FPSCounter;
import nl.ictm2a4.javagame.raspberrypi.RaspberryPIController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JFrame implements KeyListener, Runnable {

    public static final String GAMENAME = "JavaGame";

    public static final boolean USE_RPI = false; // True for RPI connection

    private final static int MAX_FPS = 30;
    private final static int MAX_FRAME_SKIPS = 5;
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

//    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
    private DecimalFormat df = new DecimalFormat("0");  // 2 dp
    private final static int STAT_INTERVAL = 1000; //ms
    private final static int FPS_HISTORY_NR = 10;
    private long lastStatusStore = 0;
    private long statusIntervalTimer = 0l;
    private long totalFramesSkipped = 0l;
    private long framesSkippedPerStatCycle = 0l;

    private int frameCountPerStatCycle = 0;
    private long totalFrameCount = 0l;
    private double fpsStore[];
    private long statsCount = 0;
    private double averageFps = 0.0;

    private Thread thread;
    private boolean isRunning;

    private static GameScreen instance;
    private String title = GAMENAME;
    private List<Integer> pressedKeys;
    public static JLayeredPane fixed;
    private List<Integer> achievedList;

    public GameScreen() {
        setTitle(title);

        instance = this;

        new FileLoader();
        new LevelLoader();
        new RaspberryPIController();

        pressedKeys = new ArrayList<>();

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        fixed = this.getLayeredPane();
        fixed.setLayout(new OverlayLayout(fixed));
        setBackground(Color.BLACK);
      
        fixed.setBounds(0, 0, LevelLoader.WIDTH, LevelLoader.HEIGHT);

        setPanel(new MainMenu());

        LevelLoader.getInstance().loadLevel(0);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        addKeyListener(this);

        setVisible(true);

        // start event handlers

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
        JPanel temp = new JPanel();
        temp.setLayout(new GridBagLayout());
        temp.setBackground(new Color(0, 0, 0, 0));
        temp.setOpaque(false);
        temp.add(panel);
        temp.revalidate();
        temp.setBounds(((LevelLoader.WIDTH / 2) - (panel.getWidth() / 2)), ((LevelLoader.HEIGHT / 2) - (panel.getHeight() / 2)), panel.getWidth(), panel.getHeight());

        fixed.add(temp, 0);

        FPSCounter.getInstance().redo();

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

    public void addOverlay(JPanel panel) {
        JPanel temp = new JPanel();
        temp.setBackground(new Color(0, 0, 0, 0));
        temp.setOpaque(false);
        temp.setLayout(new GridBagLayout());
        temp.add(panel);
        temp.revalidate();
        temp.setBounds(((LevelLoader.WIDTH / 2) - (panel.getWidth() / 2)), ((LevelLoader.HEIGHT / 2) - (panel.getHeight() / 2)), panel.getWidth(), panel.getHeight());
        fixed.add(temp, JLayeredPane.POPUP_LAYER);
        fixed.revalidate();
        fixed.repaint();
    }

    public JLayeredPane getFixed() {
        return fixed;
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

    @Deprecated
    public void setFps(int fps) {

    }

    public double getFPS() {
        return FPSCounter.getInstance().getAvgFPS();
    }

    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void tick() {
        LevelLoader.getInstance().tick();
        AchievementHandler.getInstance().tick();
    }

    private void registerAchievements() {
        new AchievementHandler();
        new LevelOneAchieved();
        new FirstDoorOpened();
        new LastLevelAchieved();
    }

    @Override
    public void run() {
        initTimingElements();

        long beginTime;     // the time when the cycle begun
        long timeDiff;      // the time it took for the cycle to execute
        int sleepTime;      // ms to sleep (<0 if we're behind)
        int framesSkipped;  // number of frames being skipped

        while (isRunning) {
            beginTime = System.currentTimeMillis();
            framesSkipped = 0;

            tick();
            repaint();

            timeDiff = System.currentTimeMillis() - beginTime;
            sleepTime = (int) (FRAME_PERIOD - timeDiff);

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
            }

            while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                // thread is running behind
                tick();
                sleepTime += FRAME_PERIOD;
                framesSkipped++;
            }

            if (framesSkipped > 0) {
                System.out.println("Skipped " + framesSkipped + " frames");
            }
            framesSkippedPerStatCycle += framesSkipped;
            storeStats();
        }
    }

    private void storeStats() {
        frameCountPerStatCycle++;
        totalFrameCount++;

        statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

        if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
            double actualFps = (double)(frameCountPerStatCycle / (STAT_INTERVAL / 1000));

            fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;
            statsCount++;

            double totalFps = 0.0;
            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                totalFps += fpsStore[i];
            }

            if (statsCount < FPS_HISTORY_NR) {
                averageFps = totalFps / statsCount;
            } else {
                averageFps = totalFps / FPS_HISTORY_NR;
            }
            totalFramesSkipped += framesSkippedPerStatCycle;
            framesSkippedPerStatCycle = 0;
            statusIntervalTimer = 0;
            frameCountPerStatCycle = 0;

            statusIntervalTimer = System.currentTimeMillis();
            lastStatusStore = statusIntervalTimer;

            FPSCounter.getInstance().setAvgFPS(df.format(averageFps));
        }
    }

    private void initTimingElements() {
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
        }
    }
}