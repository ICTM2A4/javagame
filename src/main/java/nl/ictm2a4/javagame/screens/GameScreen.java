package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.achievement.AchievementHandler;
import nl.ictm2a4.javagame.cachievements.*;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.listeners.ScoreListener;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.loaders.Settings;
import nl.ictm2a4.javagame.services.achievements.Achievement;
import nl.ictm2a4.javagame.services.achievements.AchievementsService;
import nl.ictm2a4.javagame.services.levels.LevelService;
import nl.ictm2a4.javagame.services.scores.ScoreService;
import nl.ictm2a4.javagame.services.users.User;
import nl.ictm2a4.javagame.services.users.UserService;
import nl.ictm2a4.javagame.uicomponents.FPSCounter;
import nl.ictm2a4.javagame.raspberrypi.RaspberryPIController;
import nl.ictm2a4.javagame.uicomponents.HUD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameScreen extends JFrame implements KeyListener, Runnable {

    public static final String GAMENAME = "The Labyrinth";

    private final static int MAX_FPS = 30;
    private final static int MAX_FRAME_SKIPS = 5;
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    private DecimalFormat df = new DecimalFormat("0");
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
    private List<Integer> pressedKeys;
    public static JLayeredPane fixed;
    private List<Integer> achievedList;

    private Optional<User> currentUser;

    // Buffered achievements and custom levels.
    private List<Achievement> achievedAchievements;
    private List<Achievement> achievements;
    private List<nl.ictm2a4.javagame.services.levels.Level> customLevels;

    /**
     * Create and instance of the GameScreen
     */
    public GameScreen() {
        setTitle(GAMENAME);
        currentUser = Optional.empty();

        instance = this;

        new FileLoader();
        new LevelLoader();
        new RaspberryPIController();

        achievedAchievements = new ArrayList<>();
        achievedList = new ArrayList<>();

        tryLogin();
        refreshAchievements();
        refreshCustomLevels();

        pressedKeys = new ArrayList<>();

        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        fixed = this.getLayeredPane();
        fixed.setLayout(new OverlayLayout(fixed));
        setBackground(Color.BLACK);
      
        fixed.setBounds(0, 0, LevelLoader.WIDTH, LevelLoader.HEIGHT);

        setPanel(new MainMenu());

        LevelLoader.getInstance().loadLevel(1);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        addKeyListener(this);

        setVisible(true);

        // start event handlers

        achievedList = new ArrayList<>();
        achievedList.add(1);

        registerAchievements();
        registerListeners();
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
     * Set the panel of the GameScreen and reset the title
     * @param panel The new JPanel to set
     */
    public void setPanel(JPanel panel) {
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
        if (panel instanceof Level)
            GameScreen.getInstance().addOverlay(HUD.getInstance());

    }

    /**
     * Add a panel as an overlay
     * @param panel JPanel to add as overlay
     */
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

    /**
     * returns the pane: fixed
     * @return fixed JLayeredPane
     */
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

    /**
     * get list of pressedkeys
     * @return pressed list
     */
    public List<Integer> getPressedKeys() {
        return pressedKeys;
    }

    /**
     * get list of achievements
     * @return achieved list
     */
    public List<Integer> getAchievedList() {
        return this.achievedList;
    }

    /**
     * Set a level as achieved by it's id
     * @param id the level id
     */
    public void achieveLevel(int id) {
        if (!this.achievedList.contains(id))
            this.achievedList.add(id);
    }

    /**
     * starts the thread in the game
     */
    public void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Tick all other tick-methods
     */
    private void tick() {
        LevelLoader.getInstance().tick();
        AchievementHandler.getInstance().tick();

        if (PauseScreen.getInstance() != null) {
            PauseScreen.getInstance().tick();
        }
    }

    /**
     * registers achievements
     */
    private void registerAchievements() {
        new AchievementHandler();
        new LevelOneAchieved();
        new FirstDoorOpened();
        new LastLevelAchieved();
        new FirstLevelSaved();
        new BelowTenHealth();
        new TheStrongest();
        new FistFight();
        new FakeWallPassage();
        new Wizard();
    }

    /**
     * registers listeners
     */
    private void registerListeners(){
        EventManager.getInstance().registerListener(new ScoreListener());
    }

    /**
     * The main gameloop
     */
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

    /**
     * Make calculations for the GameLoop. The AVG FPS, amount of frames to skip
     */
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

    /**
     * Set the defaults for the timing elements
     */
    private void initTimingElements() {
        fpsStore = new double[FPS_HISTORY_NR];
        for (int i = 0; i < FPS_HISTORY_NR; i++) {
            fpsStore[i] = 0.0;
        }
    }

    /**
     * sets the current user
     * @param user User
     */
    public void setCurrentUser(User user){
        if (user == null)
            currentUser = Optional.empty();
        else
            currentUser = Optional.of(user);
    }
    // Access to buffered achievements, could be moved to the services

    /**
     * refeshes the achievements
     */
    public void refreshAchievements(){
        achievements = new AchievementsService().getAchievements();
        if (getCurrentUser().isPresent())
            achievedAchievements = new AchievementsService().getAchievements(getCurrentUser().get().getId());
    }

    /**
     * list of achievements
     * @return achievements
     */
    public List<Achievement> getAchievements(){
        return achievements;
    }

    /**
     * gets achievements
     * @param id level
     * @return stream
     */
    public Achievement getAchievement(int id){
        return achievements.stream().filter(a -> a.getId() == id).findFirst().get();
    }

    /**
     * sets the achieved achievements
     * @param aa list of achievements
     */
    public void setAchievedAchievements(List<Achievement> aa){
        if(aa != null)
            achievedAchievements = aa;
    }

    /**
     * Set a achievement as achieved
     * @param achievement Achievement to achieve
     */
    public void addAchievedAchievement(Achievement achievement){
        achievedAchievements.add(achievement);
    }

    /**
     * gets achieved achievements
     * @return A list of all achieved Achievements
     */
    public List<Achievement> getAchievedAchievements(){
        return achievedAchievements;
    }


    /**
     * Access to buffered custom levels
     */
    public void refreshCustomLevels(){
        customLevels = new LevelService().getLevels();
        if(customLevels == null){
            customLevels = new ArrayList<>();
        }

        if (getCurrentUser().isPresent()) {
            ScoreService service = new ScoreService();

            for (int i = 1; i <= LevelLoader.DEFAULTLEVELAMOUNT; i++) {
                int finalI = i;
                if (service.getScores().stream().anyMatch(score -> score.getUserID() == getCurrentUser().get().getId() && score.getScoredOnID() == finalI))
                    achievedList.add(i + 1);
            }
        }
    }

    /**
     * gets custom levels
     * @return custom levels
     */
    public List<nl.ictm2a4.javagame.services.levels.Level> getCustomLevels(){
        return customLevels;
    }

    /**
     * get the custom level id
     * @param id level id
     * @return Custom level service object
     */
    public nl.ictm2a4.javagame.services.levels.Level getCustomLevel(int id){
        var customLevel = customLevels.stream().filter(a -> a.getId() == id).findFirst();
        return customLevel.orElse(null);
    }

    /**
     * adds a custom level
     * @param level level
     */
    public void addCustomLevel(nl.ictm2a4.javagame.services.levels.Level level){
        customLevels.add(level);
    }

    /**
     * update a custom level
     * @param id level id
     * @param level level
     */
    public void updateCustomLevel(int id, nl.ictm2a4.javagame.services.levels.Level level){
        int index = customLevels.stream().filter(cl -> cl.getId() == id).findFirst().get().getId();

        customLevels.set(index, level);
    }

    /**
     * gets the current user
     * @return current user
     */
    public Optional<User> getCurrentUser(){
        return currentUser;
    }

    /**
     * get api token
     * @return token
     */
    public String getApiToken(){
        if (currentUser.isPresent())
            return currentUser.get().getToken();
        return "";
    }

    /**
     * tries to log in 
     */
    private void tryLogin() {

        var login = new UserService().authenticate(Settings.getInstance().getUsername(),
            Settings.getInstance().getPassword());

        if(login != null && login.getToken() != null && !login.getToken().equals("")){
            setCurrentUser(login);

            // Get achieved achievements upon startup
            var aa = new AchievementsService().getAchievements(login.getId());
            setAchievedAchievements(aa);
        }
    }
}