package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.gameobjects.EndPoint;
import nl.ictm2a4.javagame.gameobjects.GameObject;
import nl.ictm2a4.javagame.gameobjects.Ground;
import nl.ictm2a4.javagame.gameobjects.Player;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class LevelEditor extends JPanel implements ActionListener {

    private final int hGap = 0;
    private final int vGap = 0;
    private GridBagConstraints gbc;
    private JButton save, cancel;
    private JLabel preview, items;
    private JTextField level_Name;
    private HashMap<Image, String> editorItems;
    private String current;
    private JPanel itemlist;

    public LevelEditor() {
        gbc = new GridBagConstraints ();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets( hGap, vGap, hGap, vGap );
        editorItems = new HashMap<>();
        editorItems.put(FileLoader.getInstance().getGroundTile(15), "ground");
        editorItems.put(FileLoader.getInstance().getCoinImage(0), "endpoint");
        editorItems.put(FileLoader.getInstance().getPlayerImage(PlayerStatus.IDLE, PlayerStatus.Direction.RIGHT, 0), "player");

        displayGUI();
    }

    private void displayGUI () {

        this.setPreferredSize(new Dimension((LevelLoader.width + 47), (LevelLoader.height + 200)));
        setLayout ( new GridBagLayout () );

        createNameField();

        LevelLoader.getInstance().loadLevel(2);
        Level level = LevelLoader.getInstance().getCurrentLevel().get();
        addComp ( this, level, 0, 1, 1, 1
            , GridBagConstraints.BOTH, LevelLoader.width, LevelLoader.height );
        level.addMouseListener(new LevelEditorMouseListener());

        createButtons();

        createItems();

        setVisible(true);
    }

    private void addComp(JPanel panel, JComponent comp
            , int x, int y, int gWidth
            , int gHeight, int fill
            , int weightx, int weighty) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = gWidth;
        gbc.gridheight = gHeight;
        gbc.fill = fill;

        comp.setMinimumSize(new Dimension(weightx, weighty));
        comp.setMaximumSize(new Dimension(weightx, weighty));
        comp.setPreferredSize(new Dimension(weightx, weighty));
        panel.add(comp, gbc);
    }

    private JPanel getPanel ( Color bColor ) {
        JPanel panel = new JPanel();
        panel.setOpaque ( true );
        panel.setBackground ( bColor );

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Level level = LevelLoader.getInstance().getCurrentLevel().get();
        if(e.getSource() == save) {
            level.setName("");
            level.saveLevel();
        }
        if(e.getSource() == cancel) {

        }
    }

    public class LevelEditorMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {

            int gridX = Math.round(e.getX() / 32);
            int gridY = Math.round(e.getY() / 32);

            Level level = LevelLoader.getInstance().getCurrentLevel().get();

            Optional<GameObject> find = level.fromCoords(gridX * LevelLoader.gridWidth, gridY * LevelLoader.gridHeight).filter(object -> object.getClass().getCanonicalName() == Ground.class.getCanonicalName());

            if (e.getButton() == 1) { // left mouse button
                if (!find.isPresent()) {
                    switch (current) {
                        case "ground": {level.addCollidable(new Ground(gridX,gridY)); break;}
                        case "endpoint": {level.addCollidable(new EndPoint(gridX, gridY)); break;}
                        case "player": {level.addCollidable(new Player(gridX, gridY)); break;}
                    }
                }
            }

            else if (e.getButton() == 3) { // right mouse button
                if (find.isPresent())
                    level.removeCollidable(find.get());
            }

            level.repaint();
            level.regenerateWalls();
        }
    }

    public class LevelItemsMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == 1) {
                int ypx = 25;
                for(Image image : editorItems.keySet()) {
                    if(e.getY() > ypx && e.getY() < (ypx += image.getHeight(itemlist))) {
                        System.out.println(image.getSource());
                        current = editorItems.get(image);
                    }
                }
            }
        }
    }

    private void createButtons() {
        JPanel buttons = getPanel ( Color.white );
        addComp ( this, buttons, 0, 2, 1, 1
            , GridBagConstraints.BOTH, LevelLoader.width, 100 );
        buttons.setLayout(new FlowLayout());
        save = new JButton("Save");
        save.addActionListener(this);
        buttons.add(save);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        buttons.add(cancel);
    }

    private void createNameField() {
        JPanel nameField = getPanel (Color.white);
        addComp(this, nameField, 0, 0, 1, 1,
            GridBagConstraints.BOTH, LevelLoader.width, 50);
        nameField.setLayout(new FlowLayout());
        preview = new JLabel("Preview");
        nameField.add(preview);
        level_Name = new JTextField("level name");
        nameField.add(level_Name);
    }

    private void createItems() {
        itemlist  = getPanel ( Color.white );
        itemlist.addMouseListener(new LevelItemsMouseListener());
        addComp ( this, itemlist, 1, 0, 1, 3
            , GridBagConstraints.BOTH, 47, LevelLoader.height + 200 );
        itemlist.setLayout(new FlowLayout());
        items = new JLabel("Items");
        itemlist.add(items);
        for(Image image  : editorItems.keySet()) {
            itemlist.add(new JLabel(new ImageIcon(image)));
        }
        current = "endpoint";
    }
}
