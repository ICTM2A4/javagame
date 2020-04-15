package nl.ictm2a4.javagame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Level extends JPanel {

    private int id, width, height;
    private ArrayList<Collidable> collidables;

    public Level() {
        collidables = new ArrayList<>();
        setBackground(Color.black);

        width = Main.width;
        height = Main.height;

        this.setPreferredSize(new Dimension(width, height));

    }

    public ArrayList<Collidable> getCollidables() {
        return collidables;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(Collidable collidable : collidables)
            collidable.draw(g);
    }

    public void addCollidable(Collidable collidable) {
        if (!this.collidables.contains(collidable))
            this.collidables.add(collidable);
    }
}
