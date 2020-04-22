package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.gameobjects.*;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

public class LevelEditor extends JPanel implements ActionListener, MouseMotionListener {

    private final int hGap = 0;
    private final int vGap = 0;
    private GridBagConstraints gbc;
    private JButton save, cancel;
    private JTextField level_Name;
    private HashMap<Image, Class> editorItems;
    private Class current;
    private ArrayList<JButton> itemButtons;

    public LevelEditor() {
        gbc = new GridBagConstraints ();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets( hGap, vGap, hGap, vGap );
        editorItems = new HashMap<>();
        editorItems.put(FileLoader.getInstance().getGroundTile(15), Ground.class);
        editorItems.put(FileLoader.getInstance().getCoinImage(0), EndPoint.class);
        editorItems.put(FileLoader.getInstance().getPlayerImage(PlayerStatus.IDLE, PlayerStatus.Direction.RIGHT, 0), Player.class);
        editorItems.put(FileLoader.getInstance().getTorchImage(0), Torch.class);

        displayGUI();
    }

    private void displayGUI () {

        this.setPreferredSize(new Dimension((LevelLoader.width + 2*47), (LevelLoader.height + 150)));
        setLayout ( new GridBagLayout () );

        Level level = LevelLoader.getInstance().getCurrentLevel().get();
        level.setRenderShadows(false);

        createEmptyStrip();
        createNameField();
        addComp ( this, level, 1, 1, 1, 1
            , GridBagConstraints.BOTH, LevelLoader.width, LevelLoader.height );
        level.addMouseListener(new LevelEditorMouseListener());
        level.addMouseMotionListener(this);

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
        if(e.getSource() == save && !level_Name.getText().equals("")) {
            level.setName(level_Name.getText());
            level.saveLevel();
            JOptionPane.showMessageDialog(this, "Het Level is opgeslagen");
        }
        else if (e.getSource() == save && level_Name.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Je moet een naam invoeren");
        }
        if(e.getSource() == cancel) {
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
                            (LevelLoader.width / LevelLoader.gridWidth) - 1 > gridX &&
                            gridY > 0 &&
                            (LevelLoader.height / LevelLoader.gridHeight) - 1 > gridY)
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
                                (LevelLoader.width / LevelLoader.gridWidth) - 1 > gridX &&
                                gridY > 0 &&
                                (LevelLoader.height / LevelLoader.gridHeight) - 1 > gridY)
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
        JPanel buttons = getPanel ( Color.white );
        addComp ( this, buttons, 1, 2, 1, 1
            , GridBagConstraints.BOTH, LevelLoader.width, 50 );
        buttons.setLayout(new FlowLayout());
        save = new JButton("Save");
        save.addActionListener(this);
        buttons.add(save);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        buttons.add(cancel);
    }

    private void createNameField() {
        String current_level = "";
        JPanel nameField = getPanel (Color.white);
        addComp(this, nameField, 1, 0, 1, 1,
            GridBagConstraints.BOTH, LevelLoader.width, 50);
        nameField.setLayout(new FlowLayout());
        JLabel preview = new JLabel("level name:");
        nameField.add(preview);
        if(LevelLoader.getInstance().getCurrentLevel().isPresent()) {
            current_level = LevelLoader.getInstance().getCurrentLevel().get().getName();
        }
        level_Name = new JTextField(current_level, 10);
        nameField.add(level_Name);
    }

    private void createItems() {
        JPanel itemlist = getPanel(Color.white);
        addComp ( this, itemlist, 2, 0, 1, 3
            , GridBagConstraints.BOTH, 47, LevelLoader.height + 200 );
        itemlist.setLayout(new FlowLayout());

        JLabel items = new JLabel("Items");
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
        JPanel emptyStrip = getPanel( Color.white);
        addComp(this, emptyStrip, 0, 0, 1, 3
        , GridBagConstraints.BOTH, 47, LevelLoader.height + 200);
    }
}