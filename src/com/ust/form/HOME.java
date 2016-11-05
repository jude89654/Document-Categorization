package com.ust.form;

import com.ust.GUI2.GUITrain;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pc1 on 18/10/2016.
 */
public class HOME extends JFrame {
    private JPanel panel1;
    private JButton trainButton;
    private JButton categorizeButton;

    public HOME(){
        super("MAIN MENU");
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUITrain guiTrain = new GUITrain();
                setVisible(false);
            }
        });
        categorizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUITest guiTest = new GUITest();
                setVisible(false);
            }
        });
    }
    public static void main(String args[]){
        HOME home = new HOME();
    }
}
