package nl.ictm2a4.javagame.screens;

import nl.ictm2a4.javagame.loaders.FileLoader;
import nl.ictm2a4.javagame.loaders.LevelLoader;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Optional;

public class LevelSelectScreen extends JPanel implements ActionListener {

    private JLabel selectLevel, custom_Levels;
    private JButton editLevel, back;
    private ArrayList<JButton> levels;
    private ArrayList<JButton> customLevels;
    private JPanel Jplevel, JpcustomLevel;
    private final int hGap = 0;
    private final int vGap = 0;
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
        center.setPreferredSize(new Dimension(360, 480));
        center.setBackground(new Color(0, 0, 0, 0));
        panel.add(center);
        Box.Filler hFill1 = new Box.Filler(new Dimension(0, 5),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill2 = new Box.Filler(new Dimension(0, 5),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill3 = new Box.Filler(new Dimension(0, 5),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill0 = new Box.Filler(new Dimension(0, 25),
                new Dimension(0, 10),
                new Dimension(0, 20));
        Box.Filler hFill4 = new Box.Filler(new Dimension(0, 25),
                new Dimension(0, 10),
                new Dimension(0, 20));

        selectLevel = new JLabel("Select Level:");
        selectLevel.setForeground(Color.WHITE);
        center.add(selectLevel);
        center.add(hFill0);
        Jplevel = new JPanel();
        Jplevel.setLayout(new GridLayout(0, 3, 5, 5));
        Jplevel.setMinimumSize(new Dimension(160, 60));
        Jplevel.revalidate();
        Jplevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(Jplevel);
        JScrollPane scrollFrame1 = new JScrollPane(Jplevel);
        scrollFrame1.setBorder(BorderFactory.createEmptyBorder());

        Jplevel.setAutoscrolls(true);
        Jplevel.setBackground(new Color(0, 0, 0, 0));
        scrollFrame1.setBackground(new Color(0, 0, 0, 0));
        center.add(scrollFrame1);

        levels = new ArrayList<>();
        for(int i = 0; i < LevelLoader.defaultLevelAmount; i++) {
            Optional<JSONObject> object = LevelLoader.getInstance().getLevelObject(i);
            if (object.isPresent()) {
                JButton button = new JButton();
                button.add(new JLabel("" + object.get().get("name")));
                button.setPreferredSize(new Dimension(10, 30));
                button.setMaximumSize(new Dimension(10, 30));
                levels.add(button);
                Jplevel.add(button);

                if (GameScreen.getInstance().getAchievedList().contains(i)) {
                    int finalI = i;
                    button.addActionListener(
                        e -> {
                            LevelLoader.getInstance().startLevel(finalI);
                        }
                    );
                } else {
                    button.setBackground(new Color(200,80,80));
                }
            }
        }

        center.add(hFill1);
        custom_Levels = new JLabel("Select Custom Level:");
        custom_Levels.setForeground(Color.WHITE);
        center.add(custom_Levels);
        center.add(hFill4);
        JpcustomLevel = new JPanel();
        JpcustomLevel.setLayout(new GridLayout(0, 3, 5, 5));
        JpcustomLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JpcustomLevel.setMinimumSize(new Dimension(160, 60));
        JpcustomLevel.setBackground(new Color(0, 0, 0, 0));
        JpcustomLevel.revalidate();
        center.add(JpcustomLevel);

        JScrollPane scrollFrame2 = new JScrollPane(JpcustomLevel);
        scrollFrame2.setBorder(BorderFactory.createEmptyBorder());
        JpcustomLevel.setAutoscrolls(true);
        scrollFrame2.setBackground(new Color(0, 0, 0, 0));
        center.add(scrollFrame2);

        customLevels = new ArrayList<>();
        for(int i = LevelLoader.defaultLevelAmount; i < LevelLoader.getInstance().getNewLevelId(); i++) {
            JPanel container = new JPanel();
            container.setLayout(new GridLayout(2, 0,5 ,5 ));
            container.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.setBackground(new Color(0, 0, 0, 0));

            editLevel = new JButton("Edit Level " + (1 + i - LevelLoader.defaultLevelAmount));

            JButton button = new JButton();
            button.add(new JLabel("level " + (1 + i - LevelLoader.defaultLevelAmount)));
            button.setPreferredSize(new Dimension(10, 17));
            button.setMaximumSize(new Dimension(10, 17));
            customLevels.add(button);
            container.add(button);
            container.add(editLevel);
            JpcustomLevel.add(container);

            int loadLevel = i;
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
        center.add(hFill3);
        back = new JButton("back");
        back.addActionListener(this);
        center.add(back);
        center.add(hFill4);
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
