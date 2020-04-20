package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.enums.PlayerStatus;
import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LevelEditor extends JPanel implements ActionListener {

    private final int hGap = 0;
    private final int vGap = 0;
    private GridBagConstraints gbc;
    private JButton save, cancel;
    private JLabel preview, items;
    private JTextField level_Name;
    private ArrayList <Image> editorItems;

    public LevelEditor() {
        gbc = new GridBagConstraints ();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets( hGap, vGap, hGap, vGap );
        editorItems = new ArrayList<>();
        editorItems.add(FileLoader.getInstance().getGroundTile(0));
        editorItems.add(FileLoader.getInstance().getCoinImage(0));
        editorItems.add(FileLoader.getInstance().getPlayerImage(PlayerStatus.IDLE, PlayerStatus.Direction.RIGHT, 0));

        displayGUI();

    }

    private void displayGUI () {

        this.setPreferredSize(new Dimension((LevelLoader.width + 47), (LevelLoader.height + 200)));
        setLayout ( new GridBagLayout () );

       createNameField();

        JPanel blackPanel = getPanel ( Color.BLACK );
        addComp ( this, blackPanel, 0, 1, 1, 1
                , GridBagConstraints.BOTH, LevelLoader.width, LevelLoader.height );

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
        JPanel itemlist  = getPanel ( Color.white );
        addComp ( this, itemlist, 1, 0, 1, 3
                , GridBagConstraints.BOTH, 47, LevelLoader.height + 200 );
        itemlist.setLayout(new FlowLayout());
        items = new JLabel("Items");
        itemlist.add(items);
        for(Image image  : editorItems) {
            itemlist.add(new JLabel(new ImageIcon(image)));
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == save) {

        }
        if(e.getSource() == cancel) {

        }
    }
}
