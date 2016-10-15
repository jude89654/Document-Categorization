package com.ust.form;

import com.ust.logs.JTextAreaOutputStream;
import com.ust.main.Main;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

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
    private JButton startButton;
    private JPanel mainPanel;
    private JLabel authorsLabel;
    private String welcomeText = "THANK YOU FOR USING THE SYSTEM";

    /**
     * CONSTRUCTOR THAT INITIALIZES THE GUI AND MAKES IT VISIBLE
     */
    public GUI() {
        super("DOCUMENT GENRE DETECTOR AND SUBCATEGORIZER");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
       // setVisible(true);

        DefaultCaret caret = (DefaultCaret)logsTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JTextAreaOutputStream outputStream = new JTextAreaOutputStream(logsTextArea);
        System.setOut(new PrintStream(outputStream));


        logsTextArea.setText(welcomeText);

        //actions for the browse button
        sourceBrowseButton.addActionListener(e -> {
            String path = getFolderPath();
            System.out.println("SOURCE:"+path);
            sourcePathTextField.setText(path);
            File projectFolder = new File(path);
        });

        // action if the browse button is clicked
        outputBrowseButton.addActionListener(e -> {
            String path = getFolderPath();
            outputPathTextField.setText(path);
        });

        RESETButton.addActionListener(e -> {
            logsTextArea.setText("");
            logsTextArea.setText(welcomeText);
        });


        //listerner for the start button

        startButton.addActionListener(e -> {
            if (!sourcePathTextField.getText().trim().equals("") &
                    !outputPathTextField.getText().trim().equals("")) {
                System.out.println("STARTING SYSTEM");
                String sourcePath = sourcePathTextField.getText();
                String destinationPath = outputPathTextField.getText();
                start(sourcePath,destinationPath);
                System.out.println("SYSTEM ENDED");
            }

        });
    }

    public static void main(String args[]) {
        GUI gui = new GUI();
        //show the GUI
        gui.setVisible(true);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Method used to get a strung path
     * @return
     */
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

    public void start(String projectPath, String destinationPath){
        Thread thread = new Thread(){
            public void run(){
                startButton.setEnabled(false);
                //TODO
                Main.start(projectPath,destinationPath,trainCheckBox.isSelected(),testCheckBox.isSelected(),true,true);
                startButton.setEnabled(true);
            }
        };
        thread.start();
    }
}

