package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen extends JPanel implements ActionListener {

    private JLabel username, password, password2;
    private JTextField Jusername, Jpassword, Jpassword2;
    private CButton register, back;

    public RegisterScreen() {
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

        password2 = new JLabel("Repeat password:");
        add(password2, gbc);
        Jpassword2 = new JTextField(10);
        Jpassword2.addActionListener(this);
        add(Jpassword2, gbc);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == back) {
            GameScreen.getInstance().setPanel(new LoginScreen());
        }
        if (e.getSource() == register) {
            String wachtwoord = password.getText();
            String wachtwoord2 = password2.getText();
            String naam = username.getText();
            if (!wachtwoord.equals(wachtwoord2)) {
                //STOP
            }

        }
    }
}
