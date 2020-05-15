package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import nl.ictm2a4.javagame.services.levels.Level;
import nl.ictm2a4.javagame.services.levels.LevelService;
import nl.ictm2a4.javagame.uicomponents.CButton;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class LevelSelectScreen extends JPanel implements ActionListener {

    private CButton back;
    private GridBagConstraints gbc;

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

    public LevelSelectScreen() {
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        int hGap = 0;
        int vGap = 0;
        gbc.insets = new Insets(hGap, vGap, hGap, vGap);

        displayGUI();

        setVisible(true);
    }

    private void displayGUI () {
        this.setPreferredSize(new Dimension((420), (540)));
        setLayout ( new GridBagLayout () );

        createLeftStrip();
        createTopStrip();
        createCenterStrip();
        createBottomStrip();
        createRightStrip();

        setVisible(true);
    }
    public void createCenterStrip() {
        JPanel centerStrip = getPanel();
        addComp ( this, centerStrip , 1, 1, 1, 1
                , GridBagConstraints.BOTH, 360, 480 );
        centerStrip.setLayout(new FlowLayout());
        createLevelSelector(centerStrip);
    }

    public void createLevelSelector(JPanel panel) {
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setAlignmentY(Component.LEFT_ALIGNMENT);
        center.setPreferredSize(new Dimension(290, 480));
        center.setBackground(new Color(0, 0, 0, 0));
        panel.add(center);

        JLabel selectLevel = new JLabel("Select Level:");
        selectLevel.setForeground(Color.WHITE);
        center.add(selectLevel, gbc);
        JPanel jplevel = new JPanel();
        jplevel.setLayout(new GridLayout(0, 2, 5, 5));
        jplevel.setMinimumSize(new Dimension(105, 60));
        jplevel.revalidate();
        jplevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(jplevel, gbc);
        JScrollPane scrollFrame1 = new JScrollPane(jplevel);
        scrollFrame1.setBorder(BorderFactory.createEmptyBorder());

        jplevel.setAutoscrolls(true);
        jplevel.setBackground(new Color(0, 0, 0, 0));
        scrollFrame1.setBackground(new Color(0, 0, 0, 0));
        center.add(scrollFrame1, gbc);

        for(int i = 0; i < LevelLoader.DEFAULTLEVELAMOUNT; i++) {
            Optional<JSONObject> object = LevelLoader.getInstance().getLevelObject(i);
            if (object.isPresent()) {
                CButton button = new CButton("" + object.get().get("name"));
                jplevel.add(button, gbc);

                if (GameScreen.getInstance().getAchievedList().contains(i)) {
                    int finalI = i;
                    button.addActionListener(
                        e -> {
                            LevelLoader.getInstance().startLevel(finalI);
                        }
                    );
                } else {
                    button.setForeground(new Color(200,80,80));
                }
            }
        }

        JLabel custom_Levels = new JLabel("Select Custom Level:");
        custom_Levels.setForeground(Color.WHITE);
        center.add(custom_Levels, gbc);
        JPanel jpcustomLevel = new JPanel();
        jpcustomLevel.setLayout(new GridLayout(0, 2, 5, 5));
        jpcustomLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jpcustomLevel.setMinimumSize(new Dimension(160, 60));
        jpcustomLevel.setBackground(new Color(0, 0, 0, 0));
        jpcustomLevel.revalidate();
        center.add(jpcustomLevel, gbc);

        JScrollPane scrollFrame2 = new JScrollPane(jpcustomLevel);
        scrollFrame2.setBorder(BorderFactory.createEmptyBorder());
        jpcustomLevel.setAutoscrolls(true);
        scrollFrame2.setBackground(new Color(0, 0, 0, 0));
        center.add(scrollFrame2);

        var customLevels = new LevelService().GetLevels();

        for(Level level : customLevels){
            JPanel container = new JPanel();
            container.setLayout(new GridLayout(2, 0, 5, 5));
            container.setBackground(new Color(0, 0, 0, 0));

            CButton editLevel = new CButton("Edit level " + level.Name);
            CButton button = new CButton("Start " + level.Name);
            container.add(button);
            container.add(editLevel);
            jpcustomLevel.add(container);

            int loadLevel = level.ID;
            button.addActionListener(
                    e -> {
                        LevelLoader.getInstance().startLevel(loadLevel);
                    });
            editLevel.addActionListener(
                    e -> {
                        LevelLoader.getInstance().loadLevel(loadLevel);
                        GameScreen.getInstance().setPanel(new LevelEditor(), "Level Editor");
                    }
            );
        }

        back = new CButton("back");
        back.addActionListener(this);
        center.add(back, gbc);
        }
    public void createLeftStrip() {
        JPanel leftStrip =  getPanel();
        addComp ( this, leftStrip , 0, 0, 1, 3
                , GridBagConstraints.BOTH, 30, 520 );
        leftStrip.setLayout(new FlowLayout());
    }
    public void createRightStrip() {
        JPanel rightStrip = getPanel();
        addComp ( this, rightStrip , 2, 0, 1, 3
                , GridBagConstraints.BOTH, 30, 520 );
        rightStrip.setLayout(new FlowLayout());
    }
    public void createBottomStrip() {
        JPanel bottomStrip = getPanel();
        addComp ( this, bottomStrip , 1, 2, 1, 1
                , GridBagConstraints.BOTH, 360, 30 );
        bottomStrip.setLayout(new FlowLayout());
    }
    public void createTopStrip() {
        JPanel topStrip = getPanel();
        addComp ( this, topStrip , 1, 0, 1, 1
                , GridBagConstraints.BOTH, 360, 30 );
        topStrip.setLayout(new FlowLayout());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(FileLoader.loadImage("level-selector-background.png"), 0, 0, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            GameScreen.getInstance().setPanel(new MainMenu());
        }
    }
}
