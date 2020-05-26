package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PauseScreen extends JPanel implements ActionListener {

    private ArrayList<CButton> buttons;
    private static PauseScreen instance;
    private long start;

    public PauseScreen() {
        instance = this;
        start = System.currentTimeMillis();
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;

        this.setPreferredSize(new Dimension(360, 360));
        buttons = new ArrayList<>();

        String[] buttonNames = {"Resume", "Restart", "Settings", "Back to main menu"};

        for(String name : buttonNames) {
            CButton button = new CButton(name);
            buttons.add(button);
            button.addActionListener(this);
            button.setMargin(new Insets(0, 0, 0, 0));
            add(button, gbc);
        }

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level.png"), 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == buttons.get(0)) { // resume
            LevelLoader.getInstance().resume();
            setVisible(false);
        }
        if(e.getSource() == buttons.get(1)) { // restart
            LevelLoader.getInstance().getCurrentLevel().get().restart();
            GameScreen.getInstance().setPanel(LevelLoader.getInstance().getCurrentLevel().get());
        }
        if(e.getSource() == buttons.get(3)) { // quit
            LevelLoader.getInstance().stopLevel();
        }
        if (e.getSource() == buttons.get(2)) { //Settings
            setVisible(false);
            GameScreen.getInstance().addOverlay(new SettingScreen(this));
        }
    }

    public void tick() {
        if (start + 1000 <= System.currentTimeMillis() && GameScreen.getInstance().getPressedKeys().contains(KeyEvent.VK_ESCAPE)) {
            LevelLoader.getInstance().resume();
            setVisible(false);
        }
    }

    public static PauseScreen getInstance() {
        return instance;
    }

}
