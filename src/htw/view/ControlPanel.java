package htw.view;

import htw.controller.Controller;
import htw.model.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class ControlPanel extends JPanel {

    private final View view;
    private final JTextArea manualLabel;
    private final JTextArea currPlayerTextArea;
    private JButton exitBtn;
    private JButton playGameBtn;
    private JButton newGameBtn;


    private final JTextArea msgLabel;
    private final ButtonGroup shootDirRadioButtonGroup;
    private final JRadioButton shootDirUpRadioButton;
    private final JRadioButton shootDirDownRadioButton;
    private final JRadioButton shootDirLeftRadioButton;
    private final JRadioButton shootDirRightRadioButton;
    private final TextField shootDistTextField;
    private final JButton shootButton;

    public ControlPanel(View view) {
        this.view = view;
        this.exitBtn = new JButton("Exit");
        this.playGameBtn = new JButton("Play Game");
        this.newGameBtn = new JButton("New Game");
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.manualLabel = new JTextArea("Hunt the Wumpus!\nuse arrows or click nearby cells to move \n" +
                "press S to start shoot \npress ESC back to menu\n\n\n");
        manualLabel.setEditable(false);
        this.add(manualLabel);

        this.currPlayerTextArea = new JTextArea("Player 1's turn");
        currPlayerTextArea.setEditable(false);
        this.add(currPlayerTextArea);

        this.msgLabel = new JTextArea("Hunt the Wumpus!");
        msgLabel.setEditable(false);
        this.add(msgLabel);

        newGameBtn.addActionListener(new newGameListener());
        exitBtn.addActionListener(new exitListener());
        playGameBtn.addActionListener(new restartGameListener());
        this.add(playGameBtn);
        this.add(newGameBtn);
        this.add(exitBtn);


        this.shootDirRadioButtonGroup = new ButtonGroup();
        this.shootDirUpRadioButton = new JRadioButton("NORTH");
        this.shootDirDownRadioButton = new JRadioButton("SOUTH");
        this.shootDirLeftRadioButton = new JRadioButton("WEST");
        this.shootDirRightRadioButton = new JRadioButton("EAST");

        shootDirRadioButtonGroup.add(shootDirUpRadioButton);
        shootDirRadioButtonGroup.add(shootDirDownRadioButton);
        shootDirRadioButtonGroup.add(shootDirLeftRadioButton);
        shootDirRadioButtonGroup.add(shootDirRightRadioButton);

        this.add(new Label("Shoot - Direction"));
        this.add(this.shootDirUpRadioButton);
        this.add(this.shootDirDownRadioButton);
        this.add(this.shootDirLeftRadioButton);
        this.add(this.shootDirRightRadioButton);

        shootDirUpRadioButton.getModel().setEnabled(false);
        shootDirDownRadioButton.getModel().setEnabled(false);
        shootDirLeftRadioButton.getModel().setEnabled(false);
        shootDirRightRadioButton.getModel().setEnabled(false);

        this.shootDistTextField = new TextField();

        this.add(new Label("Shoot - Distance"));
        this.add(shootDistTextField);
        shootDistTextField.setEnabled(false);


        this.shootButton = new JButton("Shoot");
        this.shootButton.addActionListener(new ShootListener());
        this.add(shootButton);
        shootDistTextField.setEnabled(false);


    }

    public void showMessage(String msg) {
        this.msgLabel.setText(msg);
    }

    public void enableShootComponents() {
        shootDirUpRadioButton.getModel().setEnabled(true);
        shootDirDownRadioButton.getModel().setEnabled(true);
        shootDirLeftRadioButton.getModel().setEnabled(true);
        shootDirRightRadioButton.getModel().setEnabled(true);
        shootDistTextField.setEnabled(true);
        shootDistTextField.setEnabled(true);
    }


    public void disableShootComponents() {
        shootDirUpRadioButton.setSelected(false);
        shootDirDownRadioButton.setSelected(false);
        shootDirLeftRadioButton.setSelected(false);
        shootDirRightRadioButton.getModel().setSelected(false);

        shootDirUpRadioButton.getModel().setEnabled(false);
        shootDirDownRadioButton.getModel().setEnabled(false);
        shootDirLeftRadioButton.getModel().setEnabled(false);
        shootDirRightRadioButton.getModel().setEnabled(false);

        shootDistTextField.setText("");
        shootDistTextField.setEnabled(false);
    }

    public void showPlayerTurn(int playerTurn) {
        currPlayerTextArea.setText("Player " + playerTurn + "'s turn.\n with " + view.getController().getCurrentPlayerArrow() + " arrows left\n");
    }


    private class ShootListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            Controller c = view.getController();
            if (c == null) {
                return;
            }
            try {
                Direction dir;
                if (shootDirUpRadioButton.isSelected()) {
                    dir = Direction.NORTH;
                } else if (shootDirDownRadioButton.isSelected()) {
                    dir = Direction.SOUTH;
                } else if (shootDirLeftRadioButton.isSelected()) {
                    dir = Direction.WEST;
                } else if (shootDirRightRadioButton.isSelected()) {
                    dir = Direction.EAST;
                } else {
                    return;
                }

                int dist = Integer.parseInt(shootDistTextField.getText());
                if (dist <= 0) {
                    return;
                }
                c.shoot(dir, dist);
            } catch (Exception e) {
                System.out.println("shoot exception " + e);
                view.showMessageDialog("Invalid Shoot!");
                return;
            } finally {
                c.stopShoot();
            }

        }
    }

    private class exitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            System.exit(0);
        }
    }

    private class newGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            Controller c = view.getController();
            if (c == null) {
                return;
            }
            try {
                view.setRandomSeed(new Random().nextInt(Parameters.RANDOM_SEED_RANGE));
                c.initMaze();
            } catch (IllegalArgumentException e) {
                view.showMessageDialog("Invalid Maze!");
            }

        }
    }

    private class restartGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Controller c = view.getController();

            if (c == null) {
                return;
            }
            c.initMaze();
        }
    }
}

