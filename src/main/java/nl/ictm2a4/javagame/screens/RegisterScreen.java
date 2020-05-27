package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.services.users.User;
import nl.ictm2a4.javagame.services.users.UserService;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterScreen extends JPanel implements ActionListener {

    private JLabel message;
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

        JLabel username = new JLabel("Username:");
        add(username, gbc);
        Jusername = new JTextField(10);
        Jusername.addActionListener(this);
        add(Jusername, gbc);

        JLabel password = new JLabel("Password:");
        add(password, gbc);
        Jpassword = new JPasswordField(10);
        Jpassword.addActionListener(this);
        add(Jpassword, gbc);

        JLabel password2 = new JLabel("Repeat password:");
        add(password2, gbc);
        Jpassword2 = new JPasswordField(10);
        Jpassword2.addActionListener(this);
        add(Jpassword2, gbc);

        message = new JLabel();
        add(message, gbc);

        register = new CButton("register");
        register.addActionListener(this);
        add(register, gbc);
        getRootPane().setDefaultButton(register);

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
            String wachtwoord = Jpassword.getText();
            String wachtwoord2 = Jpassword2.getText();
            String naam = Jusername.getText();
            if (!wachtwoord.equals("") && wachtwoord.equals(wachtwoord2) && !naam.equals("")) {
                var user = new User(0, naam, wachtwoord, "");

                var registeredUser = new UserService().register(user);

                if(registeredUser != null && registeredUser.getToken() != null && !registeredUser.getToken().equals("")){
                    GameScreen.getInstance().setCurrentUser(registeredUser); // Automatische login na registratie
                    GameScreen.getInstance().setPanel(new MainMenu());
                } else{
                    message.setText("Registration failed");
                }
            } else {
                message.setText("Please check your username and password");
            }
        }
    }
}
