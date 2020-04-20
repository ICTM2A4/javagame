package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelEditor extends JPanel implements ActionListener {

    private final int hGap = 0;
    private final int vGap = 0;
    private JPanel top, level, right, bottom;
    private GridBagConstraints gbc;

    public LevelEditor() {
        gbc = new GridBagConstraints ();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets( hGap, vGap, hGap, vGap );
        displayGUI();
    }

    private void displayGUI () {

        this.setPreferredSize(new Dimension((int) (LevelLoader.width + 100), (int) (LevelLoader.height + 200)));
        setLayout ( new GridBagLayout () );

        JPanel nameField = getPanel (Color.white);
        addComp(this, nameField, 0, 0, 1, 1,
                GridBagConstraints.BOTH, LevelLoader.width, 100);

        JPanel blackPanel = getPanel ( Color.BLACK );
        addComp ( this, blackPanel, 0, 1, 1, 1
                , GridBagConstraints.BOTH, LevelLoader.width, LevelLoader.height );

        JPanel grayPanel = getPanel ( Color.white );
        addComp ( this, grayPanel, 0, 2, 1, 1
                , GridBagConstraints.BOTH, LevelLoader.width, 100 );

        JPanel bluePanel = getPanel ( Color.white );
        addComp ( this, bluePanel, 1, 0, 1, 3
                , GridBagConstraints.BOTH, 100, LevelLoader.height + 200 );

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

    }
}
