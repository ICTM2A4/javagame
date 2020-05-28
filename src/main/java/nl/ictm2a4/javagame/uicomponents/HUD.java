package nl.ictm2a4.javagame.uicomponents;

import nl.ictm2a4.javagame.cevents.PlayerHealthLossEvent;
import nl.ictm2a4.javagame.cevents.RegenEvent;
import nl.ictm2a4.javagame.event.EventHandler;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.gameobjects.Pickup;
import nl.ictm2a4.javagame.gameobjects.Player;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.screens.Level;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HUD extends JPanel {

    private final int HEALINTERVAL = 100; //ms
    private final int REGENINTERVAL = 2000; //ms
    private static HUD instance;
    private int maxHealth = 100;
    private int prevHealth = 100;
    private long prevHeal, prevHitTime;
    private int health;
    private boolean calledEvent;

    private Optional<Player> optPlayer;

    public HUD() {
        super();
        instance = this;
        setLayout(new GridBagLayout());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(screenSize.width, screenSize.height);

        setPreferredSize(new Dimension(screenSize.width, screenSize.height));
        setOpaque(false);
        setBackground(new Color(0,0,0,0));
        setVisible(true);

        optPlayer = Optional.empty();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);

        if (optPlayer.isPresent()) {
            paintHealth(g);
            paintInventory(g);
        }
    }

    public void tick() {
        optPlayer = LevelLoader.getInstance().getCurrentLevel().flatMap(Level::getPlayer);

        if (optPlayer.isPresent()) {
            Player player = optPlayer.get();
            if (prevHealth < player.getHealth()) {
                player.setHealth(player.getHealth() - 4);
                health = player.getHealth();
                repaint();
                calledEvent = false;
            }
            if (prevHealth > player.getHealth()) {
                player.setHealth(player.getHealth() + 1);
                health = player.getHealth();
                repaint();
            }

            if (player.getHealth() < 100 &&
                prevHeal + HEALINTERVAL <= System.currentTimeMillis() && player.getHealth() > 0 &&
                prevHitTime + REGENINTERVAL <= System.currentTimeMillis()) {

                if (!calledEvent) {
                    EventManager.getInstance().callEvent(new RegenEvent(player.getHealth()));
                    calledEvent = true;
                }

                prevHealth++;
                prevHeal = System.currentTimeMillis();
            }
        }
    }

    private void paintHealth(Graphics g) {
        int startY = getHeight() - 88;
        int startX = 30;

        g.setColor(Color.DARK_GRAY);
        g.fillRect(startX + 4, startY, 160, 4);
        g.fillRect(startX, startY + 4, 4, 16);
        g.fillRect(startX + 164, startY + 4, 4, 16);
        g.fillRect(startX + 4, startY + 20, 160, 4);

        int healthWidth = (int) Math.round(160 * ((double)health / (double)maxHealth));

        g.setColor(new Color(17, 194, 96));
        g.fillRect(startX + 4, startY + 4, healthWidth, 16);
        g.setColor(new Color(9, 128, 62));
        g.fillRect(startX + 4 + 20, startY + 12, healthWidth - 20, 8);
    }

    private void paintInventory(Graphics g) {
        int startX = 30;
        int startY = getHeight() - 50;

        int inventorySlots = 5;
      
        Pickup[] inventory = LevelLoader.getInstance().getCurrentLevel().flatMap(Level::getPlayer).get()
            .getInventory().stream().filter(Pickup::isDisplayInInventory).toArray(Pickup[]::new);

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(2));

        for(int i = 0; i < inventorySlots; i++) {
            g2.drawRect(startX + (i * 34), startY, 28, 28);
            if (i < inventory.length) {
                g2.drawImage(inventory[i].getImage(), startX + (i * 34) + 2, startY + 2, 20, 20, new Color(0,0,0,0), this);
            }
        }
    }

    public void reset() {
        this.prevHealth = 100;
        this.health = 100;
    }

    public void removeHealth(int healthRemoval) {
        this.prevHealth -= healthRemoval;

        if (optPlayer.isEmpty())
            return;

        EventManager.getInstance().callEvent(new PlayerHealthLossEvent(healthRemoval, optPlayer.get().getHealth()));
        prevHitTime = System.currentTimeMillis();
    }

    public static HUD getInstance() {
        if (instance == null)
            new HUD();
        return instance;
    }
}

