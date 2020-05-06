package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.gameobjects.*;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

public class LevelEditor extends JPanel implements ActionListener, MouseMotionListener {

    private GridBagConstraints gbc;
    private JButton save, cancel;
    private JTextField levelName;
    private HashMap<Image, Class> editorItems;
    private Class current;
    private ArrayList<JButton> itemButtons;

    public LevelEditor() {
        gbc = new GridBagConstraints ();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        int hGap = 0;
        int vGap = 0;
        gbc.insets = new Insets(hGap, vGap, hGap, vGap);
        editorItems = new HashMap<>();
        editorItems.put(FileLoader.getInstance().getGroundTile(15), Ground.class);
        editorItems.put(FileLoader.getInstance().getCoinImage(0), EndPoint.class);
        editorItems.put(FileLoader.getInstance().getPlayerImage(PlayerStatus.IDLE, PlayerStatus.Direction.RIGHT, 0), Player.class);
        editorItems.put(FileLoader.getInstance().getTorchImage(0), Torch.class);

        displayGUI();
    }

    private void displayGUI () {
        this.setPreferredSize(new Dimension((LevelLoader.WIDTH + 2*47), (LevelLoader.HEIGHT + 80)));
        setLayout ( new GridBagLayout () );
        setBackground(Color.DARK_GRAY);

        Level level = LevelLoader.getInstance().getCurrentLevel().get();
        level.setRenderShadows(false);

        createEmptyStrip();
        createNameField();
        addComp ( this, level, 1, 1, 1, 1
            , GridBagConstraints.BOTH, LevelLoader.WIDTH, LevelLoader.HEIGHT);
        level.addMouseListener(new LevelEditorMouseListener());
        level.addMouseMotionListener(this);

        createButtons();
        createItems();

        setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level-editor-background.png"), 0, 0, this);
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

    private JPanel getPanel () {
        JPanel panel = new JPanel();
        panel.setOpaque (true);
        panel.setBackground(new Color(0,0,0,0));

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Level level = LevelLoader.getInstance().getCurrentLevel().get();
        if(e.getSource() == save) {
            if (levelName.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Je moet een naam invoeren");
                return;
            }
            else if (level.getGameObjects().stream().filter(object -> object instanceof Player).findFirst().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Je moet een player plaatsen");
                return;
            }
            else if (level.getGameObjects().stream().filter(object -> object instanceof EndPoint).findFirst().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Je moet een eindpunt plaatsen");
                return;
            }

            level.setName(levelName.getText());
            level.saveLevel();
            JOptionPane.showMessageDialog(this, "Het Level is opgeslagen");
        }
        if(e.getSource() == cancel) {
            if (LevelLoader.getInstance().getLevelObject(level.getId()).get().get("player") == null)
                LevelLoader.getInstance().removeCustomLevelFile(level.getId());
                LevelLoader.getInstance().loadLevel(0);
            GameScreen.getInstance().setPanel(new MainMenu());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int gridX = Math.round(e.getX() / 32);
        int gridY = Math.round(e.getY() / 32);

        Level level = LevelLoader.getInstance().getCurrentLevel().get();
        Stream<GameObject> objectStream = level.fromCoordsToArray(e.getX(), e.getY());
        Optional<GameObject> find = objectStream.filter(gameObject -> gameObject.getClass().getCanonicalName().equals(current.getCanonicalName())).findAny();

        if (SwingUtilities.isLeftMouseButton(e)) { // left mouse button
            if (find.isEmpty()) {
                switch (current.getSimpleName().toLowerCase()) {
                    case "ground": {
                        if (gridX > 0 &&
                            (LevelLoader.WIDTH / LevelLoader.GRIDWIDTH) - 1 > gridX &&
                            gridY > 0 &&
                            (LevelLoader.HEIGHT / LevelLoader.GRIDHEIGHT) - 1 > gridY)
                            level.addGameObject(new Ground(gridX, gridY));
                        break;
                    }
                }
            }

        }
        else if (SwingUtilities.isRightMouseButton(e)) { // right mouse button
            find.ifPresent(level::removeGameObject);
        }

        level.repaint();
        level.regenerateWalls();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    public class LevelEditorMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {

            int gridX = Math.round(e.getX() / 32);
            int gridY = Math.round(e.getY() / 32);

            Level level = LevelLoader.getInstance().getCurrentLevel().get();

            Optional<GameObject> find = level.fromCoordsToArray(e.getX(), e.getY()).filter(gameObject -> gameObject.getClass().getCanonicalName().equals(current.getCanonicalName())).findAny();

            if (e.getButton() == 1) { // left mouse button
                if (find.isEmpty()) {
                    switch (current.getSimpleName().toLowerCase()) {
                        case "ground": {
                            if (gridX > 0 &&
                                (LevelLoader.WIDTH / LevelLoader.GRIDWIDTH) - 1 > gridX &&
                                gridY > 0 &&
                                (LevelLoader.HEIGHT / LevelLoader.GRIDHEIGHT) - 1 > gridY)
                                level.addGameObject(new Ground(gridX, gridY));
                            break;
                        }
                        case "endpoint": {
                            if(level.fromCoordsToArray(e.getX(), e.getY()).anyMatch(gameObject -> gameObject instanceof Ground)) {
                                Optional<GameObject> endpoint = level.getGameObjects().stream().filter(gameObject -> gameObject instanceof EndPoint).findFirst();
                                endpoint.ifPresent(level::removeGameObject);
                                level.addGameObject(new EndPoint(gridX,gridY));
                            }
                            break;
                        }
                        case "player": {
                            if(level.fromCoordsToArray(e.getX(), e.getY()).anyMatch(gameObject -> gameObject instanceof Ground)) {
                                Optional<GameObject> player = level.getGameObjects().stream().filter(gameObject -> gameObject instanceof Player).findFirst();
                                player.ifPresent(level::removeGameObject);
                                level.addGameObject(new Player(gridX, gridY));

                            }
                            break;
                        }
                        case "torch": {
                            if(level.fromCoordsToArray(e.getX(), e.getY()).anyMatch(gameObject -> gameObject instanceof Wall)) {
                                level.addGameObject(new Torch(gridX, gridY));
                            }
                            break;
                        }
                    }
                }
            }

            else if (e.getButton() == 3) { // right mouse button
                find.ifPresent(level::removeGameObject);
            }

            level.repaint();
            level.regenerateWalls();
        }
    }

    private void createButtons() {
        JPanel buttons = getPanel();
        addComp ( this, buttons, 1, 2, 1, 1
            , GridBagConstraints.BOTH, LevelLoader.WIDTH, 40 );
        buttons.setLayout(new FlowLayout());
        save = new CButton("Save");
        save.addActionListener(this);
        buttons.add(save);
        cancel = new CButton("Cancel");
        cancel.addActionListener(this);
        buttons.add(cancel);
    }

    private void createNameField() {
        String current_level = "";
        JPanel nameField = getPanel();
        addComp(this, nameField, 1, 0, 1, 1,
            GridBagConstraints.BOTH, LevelLoader.WIDTH, 40);
        nameField.setLayout(new FlowLayout());
        JLabel preview = new JLabel("level name:");
        preview.setForeground(Color.WHITE);
        nameField.add(preview);
        if(LevelLoader.getInstance().getCurrentLevel().isPresent()) {
            current_level = LevelLoader.getInstance().getCurrentLevel().get().getName();
        }
        levelName = new JTextField(current_level, 10);
        nameField.add(levelName);
    }

    private void createItems() {
        JPanel itemlist = getPanel();
        addComp ( this, itemlist, 2, 1, 1, 2
            , GridBagConstraints.BOTH, 47, LevelLoader.HEIGHT + 40 );
        itemlist.setLayout(new FlowLayout());

        JLabel items = new JLabel("Items");
        items.setForeground(Color.WHITE);
        itemlist.add(items);

        itemButtons = new ArrayList<>();
        for(Image image  : editorItems.keySet()) {
            JButton button = new JButton();
            button.add(new JLabel(new ImageIcon(image)));
            itemButtons.add(button);

            button.addActionListener(
                e -> {
                    current = editorItems.get(image);
                    disableAllButtons();
                    button.setBackground(Color.DARK_GRAY);
                }
            );

            itemlist.add(button);
            disableAllButtons();
            button.setBackground(Color.DARK_GRAY);
            current = editorItems.get(image);
        }
    }

    private void disableAllButtons() {
        itemButtons.forEach(b -> b.setBackground(Color.GRAY));
    }

    private void createEmptyStrip() {
        JPanel emptyStrip = getPanel();
        addComp(this, emptyStrip, 0, 0, 1, 3
        , GridBagConstraints.BOTH, 47, LevelLoader.HEIGHT + 80);
    }
}
