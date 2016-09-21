package com.ust.form;

import javax.swing.*;

/**
 * Created by pc1 on 21/09/2016.
 */
public class GUI extends JFrame {
    private JLabel logoPanel;
    private JTextArea textArea1;
    private JTextField textField1;
    private JButton BROWSEButton;
    private JTextField textField2;
    private JButton BROWSEButton1;
    private JButton RESETButton;
    private JCheckBox trainCheckBox;
    private JCheckBox testCheckBox;
    private JButton STARTButton;
    private JPanel mainPanel;

    public GUI(){
        super("DOCUMENT GENRE DETECTOR AND SUBCATEGORIZER");

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String args[]){
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
}

