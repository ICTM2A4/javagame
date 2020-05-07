package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class SettingScreen extends JPanel implements ActionListener {

    private ArrayList<CButton> buttons;
    private CButton gameSpeed, shadows, lights;
    String[] languages = new String[] {"gameSpeed", "1x", "2x", "4x", "8x"};

    public SettingScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;

        String[] buttonNames = {"Back"};

        this.setPreferredSize(new Dimension(360, 360));

        gameSpeed = new CButton("");
        gameSpeed.setFocusable(false);

        add(gameSpeed, gbc);

        JComboBox<String> gameSpeed = new JComboBox<>(languages);
        ActionListener cbAction = e -> {
            String s = (String) gameSpeed.getSelectedItem();

            switch (s) {
                case "1x" :
                    GameScreen.getInstance().setFps(30);
                    break;
                case "2x" :
                    GameScreen.getInstance().setFps(60);
                    break;
                case "4x" :
                    GameScreen.getInstance().setFps(120);
                    break;
                case "8x" :
                    GameScreen.getInstance().setFps(240);
                    break;
            }
        };
        gameSpeed.addActionListener(cbAction);
        gameSpeed.setBackground(new Color(0, 0, 0, 0));

        this.gameSpeed.add(gameSpeed);

        shadows = new CButton("");
        JCheckBox checkBox = new JCheckBox("shadows");
        checkBox.addItemListener(e ->
                LevelLoader.getInstance().getCurrentLevel().get().setRenderShadows(e.getStateChange() == ItemEvent.SELECTED)
        );
        shadows.add(checkBox);
        checkBox.setBackground(new Color(0, 0, 0, 0));
        checkBox.setSelected(LevelLoader.getInstance().getCurrentLevel().get().isRenderShadows());
        add(shadows, gbc);

        lights = new CButton("");
        JCheckBox checkBox2 = new JCheckBox("Animated Lights");
        checkBox2.addItemListener(e ->
                LevelLoader.getInstance().getCurrentLevel().get().setAnimateLights(e.getStateChange() == ItemEvent.SELECTED)
        );
        lights.add(checkBox2);
        checkBox2.setBackground(new Color(0, 0, 0, 0));
        checkBox2.setSelected(LevelLoader.getInstance().getCurrentLevel().get().isAnimateLights());
        add(lights, gbc);

        buttons = new ArrayList<>();

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
        if (e.getSource() == buttons.get(0)) {
            GameScreen.getInstance().setPanel(new PauseScreen());
        }
    }
}
