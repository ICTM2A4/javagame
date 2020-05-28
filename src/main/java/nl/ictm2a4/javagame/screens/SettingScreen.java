package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.loaders.Settings;
import nl.ictm2a4.javagame.raspberrypi.RaspberryPIController;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Optional;

public class SettingScreen extends JPanel implements ActionListener {

    private ArrayList<CButton> buttons;
    private JPanel origin;
    private boolean[] values;
    private JTextField rpiIP;

    public SettingScreen(JPanel origin) {
        this.origin = origin;

        if (LevelLoader.getInstance().getCurrentLevel().isEmpty())
            return;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;

        String[] buttonNames = {"Back"};

        this.setPreferredSize(new Dimension(360, 360));

        values = new boolean[]{Settings.getInstance().isShowShadows(), Settings.getInstance().isAnimatedLights(), Settings.getInstance().isUseRPI()};
        String[] settingNames = new String[]{"shadows","Animated Lights","Use RaspberryPI"};

        for(int i = 0; i < settingNames.length; i++) {
            CButton button = new CButton("");
            JCheckBox checkBox = new JCheckBox(settingNames[i]);
            checkBox.setBackground(new Color(146, 115, 63));

            checkBox.setSelected(values[i]);

            button.add(checkBox);
            add(button, gbc);

            int finalI = i;
            checkBox.addItemListener(e -> {
                values[finalI] = (e.getStateChange() == ItemEvent.SELECTED);

            });
        }

        String raspberryIP = (Settings.getInstance().getRaspberryPiIp().equals("")) ? "RPIIP: 000.000.0.000:port" : Settings.getInstance().getRaspberryPiIp();

        CButton rpiIPField = new CButton("");
        rpiIP = new JTextField(raspberryIP, 10);
        rpiIP.setBackground(new Color(146, 115, 63));
        rpiIPField.add(rpiIP);
        add(rpiIPField, gbc);

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

            Settings.getInstance().setShowShadows(values[0]);
            Settings.getInstance().setAnimatedLights(values[1]);
            Settings.getInstance().setUseRPI(values[2]);
            Settings.getInstance().setRaspberryPiIp(rpiIP.getText());

            Settings.getInstance().save();

            new RaspberryPIController();

            Optional<Level> level = LevelLoader.getInstance().getCurrentLevel();
            if (level.isPresent()) {
                level.get().setRenderShadows(values[0]);
                level.get().setAnimateLights(values[1]);
            }


            if (origin instanceof MainMenu) {
                GameScreen.getInstance().setPanel(new MainMenu());
            } else {
                setVisible(false);
                PauseScreen.getInstance().setVisible(true);
            }
        }
    }
}
