package com.ust.GUI2;

import com.ust.form.HOME;
import com.ust.logs.JTextAreaOutputStream;
import com.ust.main.Main;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintStream;

/**
 * Created by pc1 on 21/09/2016.
 */
public class GUITrain extends JFrame {
    private JLabel logoPanel;
    private JTextArea logsTextArea;
    private JTextField sourcePathTextField;
    private JButton sourceBrowseButton;
    private JTextField outputPathTextField;
    private JButton outputBrowseButton;
    private JButton RESETButton;
    private JButton startButton;
    private JPanel mainPanel;
    private JLabel authorsLabel;
    private String welcomeText = "THANK YOU FOR USING THE SYSTEM";

    /**
     * CONSTRUCTOR THAT INITIALIZES THE GUITrain AND MAKES IT VISIBLE
     */
    public GUITrain() {
        super("DOCUMENT GENRE DETECTOR AND SUBCATEGORIZER");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
       setVisible(true);

        DefaultCaret caret = (DefaultCaret)logsTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JTextAreaOutputStream outputStream = new JTextAreaOutputStream(logsTextArea);
        System.setOut(new PrintStream(outputStream));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                HOME home = new HOME();

            }
        });

        logsTextArea.setText(welcomeText);

        //actions for the browse button
        sourceBrowseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String path = GUITrain.this.getFolderPath();
                System.out.println("SOURCE:" + path);
                sourcePathTextField.setText(path);
                File projectFolder = new File(path);
            }
        });


        RESETButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logsTextArea.setText("");
                logsTextArea.setText(welcomeText);
            }
        });


        //listerner for the start button

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!sourcePathTextField.getText().trim().equals("")) {
                    System.out.println("STARTING SYSTEM");
                    String sourcePath = sourcePathTextField.getText();
                    // String destinationPath = outputPathTextField.getText();
                    GUITrain.this.start(sourcePath);
                    System.out.println("SYSTEM ENDED");
                }

            }
        });
    }

    public static void main(String args[]) {
        GUITrain guiTrain = new GUITrain();
        //show the GUITrain
        guiTrain.setVisible(true);
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
     * Method used to get a string path
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

    public void start(final String projectPath){
        Thread thread = new Thread(){
            public void run(){
                startButton.setEnabled(false);
                //TODO
                Main.train(projectPath);
                startButton.setEnabled(true);
            }
        };
        thread.start();
    }
}

