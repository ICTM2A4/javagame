package nl.ictm2a4.javagame.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlaceDoorLeverKeyDialog extends JDialog implements ActionListener {
    private JLabel doorCodeLabel;
    private JTextField doorCodeTextField;
    private JButton okButton, cancelButton;
    private JComboBox doorCodeComboBox;
    private String[] colors = { "Yellow", "Red", "Blue", "Green" };

    private boolean confirmed;

    public PlaceDoorLeverKeyDialog(JFrame frame){
        super(frame, true);
        setSize(480, 240);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        doorCodeLabel = new JLabel("Code");
        add(doorCodeLabel);

        doorCodeComboBox = new JComboBox(colors);
        add(doorCodeComboBox);

        okButton = new JButton("Ok");
        okButton.addActionListener(this::onOkClicked);
        add(okButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this::onCancelClicked);
        add(cancelButton);

        setVisible(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {}

    public int getDoorCode(){
        return doorCodeComboBox.getSelectedIndex();
    }

    public boolean isConfirmed(){
        return confirmed;
    }

    public void onOkClicked(ActionEvent e){
        confirmed = true;
        setVisible(false);
    }

    public void onCancelClicked(ActionEvent e){
        confirmed = false;
        setVisible(false);
    }

}
