package nl.ictm2a4.javagame.screens;

import com.sun.tools.jconsole.JConsolePlugin;
import nl.ictm2a4.javagame.loaders.LevelLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LevelSelectScreen extends JPanel implements ActionListener {

    private JLabel selectLevel, custom_Levels;
    private JButton editLevel, back;
    private ArrayList<JButton> levels;
    private ArrayList<JButton> customLevels;
    private JPanel Jplevel, JpcustomLevel;


    public LevelSelectScreen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentY(Component.LEFT_ALIGNMENT);
        this.setPreferredSize(new Dimension(360, 480));

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
        add(selectLevel);
        add(hFill0);
        Jplevel = new JPanel();
        Jplevel.setLayout(new GridLayout(0, 3, 5, 5));
        Jplevel.setPreferredSize(new Dimension(160, 480));
        Jplevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(Jplevel);
        JScrollPane scrollFrame1 = new JScrollPane(Jplevel);
        Jplevel.setAutoscrolls(true);
        this.add(scrollFrame1);
        levels = new ArrayList<>();
        for(int i = 0; i < LevelLoader.defaultLevelAmount; i++) {
            JButton button = new JButton();
            button.add(new JLabel("level " + (1 + i)));
            button.setPreferredSize(new Dimension(10, 17));
            levels.add(button);
            Jplevel.add(button);

            int finalI = i;
            button.addActionListener(
                    e -> {
                        LevelLoader.getInstance().startLevel(finalI);
                    }
            );
        }
        add(hFill1);
        custom_Levels = new JLabel("Select Custom Level:");
        add(custom_Levels);
        add(hFill4);
        JpcustomLevel = new JPanel();
        JpcustomLevel.setLayout(new GridLayout(0, 3, 5, 5));
        JpcustomLevel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JpcustomLevel.setPreferredSize(new Dimension(160, 480));
        add(JpcustomLevel);

        JScrollPane scrollFrame2 = new JScrollPane(JpcustomLevel);
        JpcustomLevel.setAutoscrolls(true);
        this.add(scrollFrame2);

        customLevels = new ArrayList<>();
        for(int i = LevelLoader.defaultLevelAmount; i < LevelLoader.getInstance().getNewLevelId(); i++) {
            JPanel container = new JPanel();
            container.setLayout(new GridLayout(2, 0,5 ,5 ));
            container.setAlignmentX(Component.CENTER_ALIGNMENT);

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
        add(hFill3);
        back = new JButton("back");
        back.addActionListener(this);
        add(back);
        add(hFill4);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == back) {
            GameScreen.getInstance().setPanel(new MainMenu());
        }
    }
}