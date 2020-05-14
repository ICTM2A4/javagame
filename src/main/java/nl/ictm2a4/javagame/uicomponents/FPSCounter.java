package nl.ictm2a4.javagame.uicomponents;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.GameScreen;

import javax.swing.*;
import java.awt.*;

public class FPSCounter extends JPanel {

    private static FPSCounter instance;
    private String fps;

    public FPSCounter() {
        super();

        instance = this;

        setPreferredSize(new Dimension(140,40));
        setLayout(new FlowLayout());

        setOpaque(false);
    }

    public void redo() {
        setVisible(true);
        setBounds(0, 30, 140, 40);
        GameScreen.getInstance().getFixed().add(this, JLayeredPane.POPUP_LAYER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.GREEN);
        g.drawString("fps: " + fps, 10, 20);
    }

    public static FPSCounter getInstance() {
        if (instance == null)
            new FPSCounter();
        return instance;
    }

    public void setAvgFPS(String fps) {
        this.fps = fps;
        repaint();
    }

    public double getAvgFPS() {
        return Double.parseDouble(fps);
    }
}
