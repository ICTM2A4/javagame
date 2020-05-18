package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.raspberrypi.RaspberryPIController;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainMenu extends JPanel implements ActionListener {

    private ArrayList<CButton> buttons;
    boolean isLoggedIn = false; //TODO: JOCHEM EVEN AANPASSEN

    public MainMenu() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;

        this.setPreferredSize(new Dimension(360, 360));
        buttons = new ArrayList<>();

        LoginScreen loginScreen = new LoginScreen();

        String[] buttonNames = {"Start","Select level","Level Builder", "Login", "Logout", "Exit"};

        for(String name : buttonNames) {
            CButton button = new CButton(name);
            buttons.add(button);
            button.addActionListener(this);
            button.setMargin(new Insets(0, 0, 0, 0));
            if (name.equals("Login") && isLoggedIn) {continue;}
            if (name.equals("Logout") && !isLoggedIn) continue;
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

        if(e.getSource() == buttons.get(0)) { // start
            LevelLoader.getInstance().startLevel();
        }
        if(e.getSource() == buttons.get(1)) { // select level
            GameScreen.getInstance().setPanel(new LevelSelectScreen());
        }
        if(e.getSource() == buttons.get(2)) { // level builder
            GameScreen.getInstance().setPanel(new preLevelEditorScreen());
        }
        if (e.getSource() == buttons.get(3)) {
            GameScreen.getInstance().addOverlay(new LoginScreen());
        }
        if (e.getSource() == buttons.get(4)) {
            isLoggedIn = false;//TODO JOCHEM AAN T WERK

        }
        if(e.getSource() == buttons.get(5)) { // exit
            //RaspberryPIController.getInstance().disconnect();
            System.exit(0);
        }

    }
}
