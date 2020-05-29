package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverScreen extends JPanel implements ActionListener {

    CButton backToMainMenu, restart;

    public GameOverScreen() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(5,0,5,0);
        gbc.anchor = GridBagConstraints.WEST;
        this.setPreferredSize(new Dimension(360, 360));

        JLabel gameOver = new JLabel("Game Over");
        gameOver.setForeground(Color.WHITE);
        add(gameOver, gbc);
        restart = new CButton("Restart");
        restart.addActionListener(this);
        add(restart, gbc);
        backToMainMenu = new CButton("Back to Main menu");
        backToMainMenu.addActionListener(this);
        add(backToMainMenu, gbc);

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level.png"), 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restart) {
            GameScreen.getInstance().setPanel(LevelLoader.getInstance().getCurrentLevel().get());
            LevelLoader.getInstance().getCurrentLevel().get().restart();
        }
        if (e.getSource() == backToMainMenu) {
            GameScreen.getInstance().setPanel(new MainMenu());
        }
    }


}
