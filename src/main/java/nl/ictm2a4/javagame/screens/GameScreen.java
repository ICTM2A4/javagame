package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class GameScreen extends JFrame implements KeyListener {

    public static final String GAMENAME = "JavaGame";

    private static GameScreen instance;
    private String title = GAMENAME;
    private List<Integer> pressedKeys;
    public static JLayeredPane fixed;
    private List<Integer> achievedList;

    public GameScreen() {
        setTitle(title);

        instance = this;

        new FileLoader();
        new EventManager();
        new LevelLoader();
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

        achievedList = new ArrayList<>();
        achievedList.add(0);
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
        System.out.println(fixed.getComponentCount());
        fixed.revalidate();
        fixed.repaint();
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
}