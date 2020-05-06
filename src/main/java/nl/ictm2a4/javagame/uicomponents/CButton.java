package nl.ictm2a4.javagame.uicomponents;

import nl.ictm2a4.javagame.loaders.FileLoader;

import javax.swing.*;
import java.awt.*;

public class CButton extends JButton {

    public CButton(String title) {
        this(title, 140);
    }

    public CButton(String title, int width) {
        super(title);
        this.setPreferredSize(new Dimension(width, 30));
        this.setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("button-background.png"), 0, 0, this);
        g.drawString(this.getText(), 20, 20);
    }

    public void add() {
    }
}
