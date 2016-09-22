package com.ust.form;

import com.ust.main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pc1 on 21/09/2016.
 */
public class GUI extends JFrame {
    private JLabel logoPanel;
    private JTextArea logsTextArea;
    private JTextField sourcePathTextField;
    private JButton sourceBrowseButton;
    private JTextField outputPathTextField;
    private JButton outputBrowseButton;
    private JButton RESETButton;
    private JCheckBox trainCheckBox;
    private JCheckBox testCheckBox;
    private JButton STARTButton;
    private JPanel mainPanel;
    private String welcomeText = "THANK YOU FOR USING THE MEANS SYSTEM";


    public GUI() {
        super("DOCUMENT GENRE DETECTOR AND SUBCATEGORIZER");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


        logsTextArea.setText(welcomeText);

        //actions for the browse button
        sourceBrowseButton.addActionListener(e -> {
            String path = getFolderPath();
            sourcePathTextField.setText(path);
        });
        outputBrowseButton.addActionListener(e -> {
            String path = getFolderPath();
            outputPathTextField.setText(path);
        });
        RESETButton.addActionListener(e -> {
            logsTextArea.setText("");
            logsTextArea.setText(welcomeText);
        });


        //listerner for the start button

        STARTButton.addActionListener(e -> {
            boolean test = testCheckBox.isSelected();
            boolean train = trainCheckBox.isSelected();
            if (sourcePathTextField.getText().trim().length() != 0 |
                    outputPathTextField.getText().trim().length() != 0) {
                String sourcePath = sourcePathTextField.getText();
                String destinationPath = outputPathTextField.getText();
                Main.categorize(new String[]{sourcePath,destinationPath},train,test);
            }
        });
    }

    public static void main(String args[]) {
        GUI gui = new GUI();
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
                if ("Metal".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFolderPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showDialog(new JFrame(), "SELECT FOLDER") == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getPath();
        } else {
            System.out.println("YOU MUST SELECT A FOLDER WHERE THE SOURCE DATA COMES FROM");
            JOptionPane.showMessageDialog(mainPanel, "YOU MUST SELECT A FOLER");
            //stopProgram();
            return "";
        }
    }

    public static void logMessage(String message){
       // logsTextArea.append(message+"\n");
    }
}

