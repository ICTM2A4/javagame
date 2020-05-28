package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.cevents.LevelSavedEvent;
import nl.ictm2a4.javagame.event.EventManager;
import nl.ictm2a4.javagame.gameobjects.*;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.GameObjectsLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.uicomponents.CButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

public class LevelEditor extends JPanel implements ActionListener, MouseMotionListener {

    private static LevelEditor instance;

    private GridBagConstraints gbc;
    private JButton save, cancel;
    private JTextField levelName;
    private ArrayList<LevelEditorItem> levelEditorItems;
    private Class<? extends GameObject> current;
    private ArrayList<JButton> itemButtons;
    private Level level;

    public LevelEditor() {
        instance = this;
        gbc = new GridBagConstraints ();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        int hGap = 0;
        int vGap = 0;
        gbc.insets = new Insets(hGap, vGap, hGap, vGap);

        levelEditorItems = new ArrayList<>();

        for(Class<? extends GameObject> gameObjectClass : GameObjectsLoader.getInstance().getObjectList()) {
            try {
                Method method = gameObjectClass.getMethod("getLevelEditorSpecs");
                levelEditorItems.add((LevelEditorItem) method.invoke(null));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        displayGUI();
    }

    private void displayGUI () {
        this.setPreferredSize(new Dimension((LevelLoader.WIDTH + 2*47), (LevelLoader.HEIGHT + 80)));
        setLayout ( new GridBagLayout () );
        setBackground(Color.DARK_GRAY);

        level = LevelLoader.getInstance().getCurrentLevel().get();
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

    /**
     * removes objects
     * @param level
     */
    public void removeObjects(Level level) {
        ArrayList<GameObject> removeList = new ArrayList<>();

        for(LevelEditorItem item : levelEditorItems) {
            level.getGameObjects().stream().filter(gameObject -> gameObject.getClass().isAssignableFrom(item.getGameObject())).forEach(gameObject -> {
                if(item.checkRemove(gameObject))
                    removeList.add(gameObject);
            });
        }
        removeList.forEach(level::removeGameObject);
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
            try {
                if (level.saveLevel()){
                    JOptionPane.showMessageDialog(this, "Het Level is opgeslagen");
                    EventManager.getInstance().callEvent(new LevelSavedEvent(level.getId()));
                } else {
                    JOptionPane.showMessageDialog(this, "Het Level is niet opgeslagen");
                }
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Het Level is niet opgeslagen");
            }
        }
        if(e.getSource() == cancel) {
            if (LevelLoader.getInstance().getLevelObject(level.getId()).isEmpty())
                LevelLoader.getInstance().loadLevel(1);
            GameScreen.getInstance().setPanel(new preLevelEditorScreen());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {
        loopOverAll(e, false);
    }

    public class LevelEditorMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            loopOverAll(e, true);
        }
    }

    /**
     * loops over all objects from object loader
     * @param e is mouse event
     * @param place
     */
    private void loopOverAll(MouseEvent e, boolean place) {
        if (LevelLoader.getInstance().getCurrentLevel().isEmpty())
            return;

        level = LevelLoader.getInstance().getCurrentLevel().get();
        Optional<GameObject> find = level.fromCoordsToArray(e.getX(), e.getY()).filter(gameObject -> gameObject.getClass().isAssignableFrom(current) ).findAny();

        for(LevelEditorItem item : levelEditorItems) {
            if (current.isAssignableFrom(item.getGameObject())) {
                if (SwingUtilities.isLeftMouseButton(e)) { // left mouse button
                    if (find.isEmpty()) {
                        if (place)
                            item.onPlace(e.getX(), e.getY());
                        else
                            item.onDrag(e.getX(), e.getY());
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) { // right mouse button
                    item.onRemove(find);
                }
            }
        }

        level.repaint();
        level.regenerateWalls();
        removeObjects(level);
    }

    public abstract static class LevelEditorItem {

        private Class<? extends GameObject> gameObject;
        private Image image;
        public int gridX, gridY;
        private boolean requireGround, requireWall;

        /**
         *
         * @param gameObject is gameobject
         * @param image is image
         */
        public LevelEditorItem(Class<? extends GameObject> gameObject, Image image) {
            this.gameObject = gameObject;
            this.image = image;
        }

        /**
         * places object in leveleditor
         * @param mouseX
         * @param mouseY
         */
        public void onPlace(int mouseX, int mouseY) {
            gridX = Math.round(mouseX / LevelLoader.GRIDWIDTH);
            gridY = Math.round(mouseY / LevelLoader.GRIDHEIGHT);
        }

        /**
         * look is object may be placed
         * @param mouseX the x of the mouse
         * @param mouseY the y of the mouse
         * @return boolean
         */
        public boolean allowedToPlace(int mouseX, int mouseY) {
            if (requireWall && !onWall(mouseX, mouseY)) return false;
            if (requireGround && !onGround(mouseX, mouseY)) return false;
            return true;
        }

        /**
         * if mouse is dragged
         * @param mouseX the x of the mouse
         * @param mouseY the y of the mouse
         */
        public void onDrag(int mouseX, int mouseY) {
            gridX = Math.round(mouseX / LevelLoader.GRIDWIDTH);
            gridY = Math.round(mouseY / LevelLoader.GRIDHEIGHT);
        }

        /**
         * if object needs to be removed
         * @param find finds object
         */
        public void onRemove(Optional<GameObject> find) {
            find.ifPresent(getInstance().getLevel()::removeGameObject);
        }

        /**
         * checks if is allowed to remove
         * @param gameObject
         * @return boolean
         */
        public boolean checkRemove(GameObject gameObject) {
            return !this.allowedToPlace(Math.round(gameObject.getX() / LevelLoader.GRIDWIDTH) * LevelLoader.GRIDWIDTH,
                Math.round(gameObject.getY() / LevelLoader.GRIDHEIGHT) * LevelLoader.GRIDHEIGHT);
        }

        /**
         * gets gameobject
         * @return gameobject
         */
        public Class<? extends GameObject> getGameObject() {
            return gameObject;
        }

        /**
         * gets image
         * @return image
         */
        public Image getImage() {
            return image;
        }

        /**
         * if true wall may be placed
         * @param requireWall if true requires wall
         * @return boolean
         */
        public LevelEditorItem setRequireWall(boolean requireWall) {
            this.requireWall = requireWall;
            return this;
        }

        /**
         * if called ground is required
         * @param requireGround if true requires ground
         * @return boolean
         */
        public LevelEditorItem setRequireGround(boolean requireGround) {
            this.requireGround = requireGround;
            return this;
        }

        /**
         * if mouse is on ground return true
         * @param mouseX
         * @param mouseY
         * @return boolean
         */
        private boolean onGround(int mouseX, int mouseY) {
            return getInstance().getLevel().fromCoordsToArray(mouseX, mouseY).anyMatch(gameObject -> gameObject instanceof Ground);
        }

        /**
         * if mouse is on wall return true
         * @param mouseX
         * @param mouseY
         * @return boolean
         */
        private boolean onWall(int mouseX, int mouseY) {
            return getInstance().getLevel().fromCoordsToArray(mouseX, mouseY).anyMatch(gameObject -> gameObject instanceof Wall);
        }
    }

    /**
     * creates screen
     */
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
        for(LevelEditorItem item : levelEditorItems) {
            JButton button = new JButton();
            button.add(new JLabel(new ImageIcon(item.getImage())));
            itemButtons.add(button);

            button.addActionListener(
                e -> {
                    current = item.getGameObject();
                    disableAllButtons();
                    button.setBackground(Color.DARK_GRAY);
                }
            );

            disableAllButtons();
            current = levelEditorItems.get(0).getGameObject();
            itemlist.add(button);
            button.setBackground(Color.DARK_GRAY);
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

    public static LevelEditor getInstance() {
        if (instance == null) {
            new LevelEditor();
        }
        return instance;
    }

    public Level getLevel() {
        return this.level;
    }
}
