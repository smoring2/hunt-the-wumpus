package htw.view;


import htw.controller.Controller;
import htw.GameMode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModeSelectMenu {

    private final JFrame mainFrame;
    private final JRadioButton gameModeGUIRadioButton;
    private final JRadioButton gameModeTextRadioButton;

    public ModeSelectMenu() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 3, 5, 10));
        ButtonGroup gameModeSlectRadioButtonGroup = new ButtonGroup();
        this.gameModeGUIRadioButton = new JRadioButton("GUI");
        this.gameModeTextRadioButton = new JRadioButton("Text");

        gameModeGUIRadioButton.setSelected(true);
        gameModeSlectRadioButtonGroup.add(gameModeGUIRadioButton);
        gameModeSlectRadioButtonGroup.add(gameModeTextRadioButton);

        panel.add(gameModeGUIRadioButton);
        panel.add(gameModeTextRadioButton);

        JButton playGameBtn = new JButton("Play Game");
        playGameBtn.addActionListener(new playGameListener());
        panel.add(playGameBtn);


        this.mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setTitle("Hunt The Wumpus");
        mainFrame.setResizable(false);
        mainFrame.getContentPane().add(panel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    private class playGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameMode gameMode;
            if(gameModeGUIRadioButton.isSelected()) {
                gameMode = GameMode.GUI;
            } else {
                gameMode = GameMode.TEXT;
            }
            Controller.init(gameMode);
            mainFrame.dispose();
        }
    }
}
