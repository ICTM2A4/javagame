package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JPanel implements ActionListener {

    private JLabel username, password;
    private JTextField Jusername, Jpassword;
    private CButton login, register, back;
    private boolean loggedIn;
    private String usernametext, passwordtext;

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

        login = new CButton("Login");
        login.addActionListener(this);
        add(login, gbc);

        register = new CButton("register");
        register.addActionListener(this);
        add(register, gbc);

        back = new CButton("back");
        back.addActionListener(this);
        add(back, gbc);

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level.png"), 0, 0, this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            GameScreen.getInstance().setPanel(new MainMenu());
            usernametext = username.getText();
            passwordtext = password.getText();
        }
        if (e.getSource() == register) {
            GameScreen.getInstance().setPanel(new RegisterScreen());
        }
        if (e.getSource() == back) {
            GameScreen.getInstance().setPanel(new MainMenu());
        }
    }
}
