package nl.ictm2a4.javagame.screens;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlaceDoorLeverKeyDialog extends JDialog implements ActionListener {
    private JLabel doorCodeLabel;
    private JTextField doorCodeTextField;
    private JButton okButton, cancelButton;

    private boolean confirmed;

    public PlaceDoorLeverKeyDialog(JFrame frame){
        super(frame, true);
        setSize(480, 240);

        JLabel doorCodeLabel = new JLabel("Code");
        add(doorCodeLabel);

        JTextField doorCodeTextField = new JTextField();
        add(doorCodeTextField);

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(this::onOkClicked);
        add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this::onCancelClicked);
        add(cancelButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {}

    public int getDoorCode(){
        int doorCode = 0;

        try{
            doorCode = Integer.parseInt(doorCodeLabel.getText());
        } catch (Exception ex){
            // doorCode zo laten
        }

        return doorCode;
    }

    public boolean getConfirmed(){
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
