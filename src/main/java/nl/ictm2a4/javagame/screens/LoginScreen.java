package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LoginScreen extends JPanel implements ActionListener {

    private JLabel username, password;
    private JTextField Jusername, Jpassword;
    private ArrayList<CButton> buttons;

    public LoginScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;

        this.setPreferredSize(new Dimension(360, 360));

        username = new JLabel("Username:");
        add(username, gbc);
        Jusername = new JTextField(10);
        Jusername.addActionListener(this);
        add(Jusername, gbc);

        password = new JLabel("Password:");
        add(password, gbc);
        Jpassword = new JTextField(10);
        Jpassword.addActionListener(this);
        add(Jpassword, gbc);

        buttons = new ArrayList<>();

        String[] buttonNames = {"Login","Register", "Back"};

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

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == buttons.get(0)) {
            GameScreen.getInstance().setPanel(new MainMenu());
            String usernametext = username.getText();
            String passwordtext = password.getText();
        }
        if (e.getSource() == buttons.get(1)) {
            GameScreen.getInstance().setPanel(new RegisterScreen());
        }
        if (e.getSource() == buttons.get(2)) {
            GameScreen.getInstance().setPanel(new MainMenu());
        }
    }
}
