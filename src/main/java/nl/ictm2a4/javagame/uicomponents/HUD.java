package nl.ictm2a4.javagame.uicomponents;

import nl.ictm2a4.javagame.gameobjects.Pickup;
import nl.ictm2a4.javagame.gameobjects.Player;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HUD extends JPanel {

    private static HUD instance;
    private int maxHealth = 100;
    private int prevHealth = 100;

    private Player player;

    public HUD() {
        super();
        instance = this;
        setLayout(new GridBagLayout());

        setPreferredSize(new Dimension(LevelLoader.WIDTH, LevelLoader.HEIGHT));
        setOpaque(false);
        setBackground(new Color(0,0,0,0));
        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        paintHealth(g);
        paintInventory(g);
    }

    public void tick() {
        if (prevHealth != player.getHealth()) {
            player.setHealth(player.getHealth() - 1);
            repaint();
        }
    }

    private void paintHealth(Graphics g) {
        int startY = LevelLoader.HEIGHT - 68;
        int startX = 30;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(startX + 4, startY, 160, 4);
        g.fillRect(startX, startY + 4, 4, 16);
        g.fillRect(startX + 164, startY + 4, 4, 16);
        g.fillRect(startX + 4, startY + 20, 160, 4);

        int healthWidth = (int) Math.round(160 * ((double)player.getHealth() / (double)maxHealth));

        g.setColor(new Color(17, 194, 96));
        g.fillRect(startX + 4, startY + 4, healthWidth, 16);
        g.setColor(new Color(9, 128, 62));
        g.fillRect(startX + 4 + 20, startY + 12, healthWidth - 20, 8);
    }

    private void paintInventory(Graphics g) {
        int startX = 30;
        int startY = LevelLoader.HEIGHT - 30;

        int inventorySlots = 5;

        List<Pickup> inventory = LevelLoader.getInstance().getCurrentLevel().get().getPlayer().getInventory();

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2));

        for(int i = 0; i < inventorySlots; i++) {
            g2.drawRect(startX + (i * 34), startY, 28, 28);
            if (i < inventory.size()) {
                g2.drawImage(inventory.get(i).getImage(), startX + (i * 34) + 2, startY + 2, 20, 20, new Color(0,0,0,0), this);
            }
        }
    }

    public void reset() {
        this.prevHealth = 100;
        player = LevelLoader.getInstance().getCurrentLevel().get().getPlayer();
    }

    public void setHealth(int health) {
        this.prevHealth = health;
    }

    public void removeHealth(int healthRemoval) {
        this.prevHealth -= healthRemoval;
    }

    public static HUD getInstance() {
        if (instance == null)
            new HUD();
        return instance;
    }
}
