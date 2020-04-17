package nl.ictm2a4.javagame;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EndPoint extends GameObject {

    private final int animateDelay = 3;
    private final int imageAmount = 7;

    private List<Image> images;
    private int currentImage = 0;
    private int animateCount = 0;

    public EndPoint(Level level, int gridX, int gridY) {
        super(
            level,
            gridX * Main.gridWidth + ((Main.gridWidth - 16) / 2),
            gridY * Main.gridHeight + ((Main.gridHeight - 16) / 2) - 2,
            16,16);
        images = new ArrayList<>();
    }

    @Override
    public void draw(Graphics g) {
        animateCount++;
        if (animateCount - animateDelay == 0) {
            animateCount = 0;
            currentImage++;
            if (currentImage == imageAmount) currentImage = 0;
        }

        g.drawImage(images.get(currentImage),getX(),getY(), Main.screen.getLevel());
    }

    @Override
    public void loadImage() {
        for(int i = 0; i < imageAmount; i++)
            images.add(Main.loadImage("textures/coin-"+i+".png"));
    }

    @Override
    public boolean checkCollideSingle(GameObject gameObject, int x, int y) {
        boolean result = super.checkCollideSingle(gameObject,x,y);

        if (result) {
            this.getLevel().setVisible(false);
            System.out.println("Je hebt gewonnen");
        }

        return result;
    }
}
