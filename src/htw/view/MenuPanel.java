package htw.view;

import htw.PlayerMode;
import htw.model.*;

import javax.swing.*;
import java.awt.*;

class MenuPanel extends JPanel {

    private final View view;
    private TextField numOfRowTextField;
    private TextField numOfColTextField;
    private TextField numOfRemainingWallTextField;
    private TextField numOfSuperBatCellTextField;
    private TextField numOfBottomlessPitTextField;
    private TextField randomSeedTextField;
    private ButtonGroup mazeTypeButtonGroup;
    private JRadioButton mazeTypePerfectButton;
    private JRadioButton mazeTypeRoomButton;
    private ButtonGroup mazeWrappedButtonGroup;
    private JRadioButton mazeWrappedButton;
    private JRadioButton mazeNotWrappedButton;
    private TextField playerNumOfArrowTextField;
    private ButtonGroup playerNumRadioButtonGroup;
    private JRadioButton onePlayerRadioButton;
    private JRadioButton twoPlayerRadioButton;


    public MenuPanel(View view) {
        this.view = view;
        this.setBackground(Color.lightGray);
        this.setFocusable(true);

        JPanel gameSettingPanel = initGameSettingPanel();

        this.add(gameSettingPanel);

        this.setVisible(true);
    }

    private JPanel initGameSettingPanel() {
        JPanel gameSettingPanel = new JPanel();
        gameSettingPanel.setLayout(new BoxLayout(gameSettingPanel, BoxLayout.Y_AXIS));
        JPanel mazeSettingPanel = initMazeSettingPanel();
        JPanel playerSettingPanel = initPlayerSettingPanel();

        gameSettingPanel.add(mazeSettingPanel, BorderLayout.NORTH);
        gameSettingPanel.add(new JSeparator(), BorderLayout.CENTER);
        gameSettingPanel.add(playerSettingPanel);
        gameSettingPanel.add(new JSeparator(), BorderLayout.WEST);
        gameSettingPanel.add(new Label("Random Seed"));
        this.randomSeedTextField = new TextField("10");
        gameSettingPanel.add(this.randomSeedTextField);
        return gameSettingPanel;
    }

    private JPanel initPlayerSettingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 5, 10));

        panel.add(new Label("Player - Num of Arrows"));
        this.playerNumOfArrowTextField = new TextField("2");
        panel.add(this.playerNumOfArrowTextField);
        return panel;
    }

    private JPanel initMazeSettingPanel() {
        JPanel mazeSettingPanel = new JPanel();
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 3, 5, 10));
        this.mazeTypeButtonGroup = new ButtonGroup();
        this.mazeTypePerfectButton = new JRadioButton("perfect");
        this.mazeTypeRoomButton = new JRadioButton("room");

        mazeTypeRoomButton.setSelected(true);
        mazeTypeButtonGroup.add(mazeTypePerfectButton);
        mazeTypeButtonGroup.add(mazeTypeRoomButton);

        panel.add(new Label("Maze - Type"));
        panel.add(this.mazeTypePerfectButton);
        panel.add(mazeTypeRoomButton);


        this.mazeWrappedButtonGroup = new ButtonGroup();
        this.mazeWrappedButton = new JRadioButton("wrapped");
        this.mazeNotWrappedButton = new JRadioButton("not wrapped");

        mazeWrappedButtonGroup.add(mazeWrappedButton);
        mazeWrappedButtonGroup.add(mazeNotWrappedButton);
        mazeNotWrappedButton.setSelected(true);


        panel.add(new Label("Maze - Wrapped?"));
        panel.add(this.mazeWrappedButton);
        panel.add(mazeNotWrappedButton);

        this.playerNumRadioButtonGroup = new ButtonGroup();
        this.onePlayerRadioButton = new JRadioButton("one player");
        this.twoPlayerRadioButton = new JRadioButton("two players");

        playerNumRadioButtonGroup.add(onePlayerRadioButton);
        playerNumRadioButtonGroup.add(twoPlayerRadioButton);
        onePlayerRadioButton.setSelected(true);

        panel.add(new Label("How many players?"));
        panel.add(onePlayerRadioButton);
        panel.add(twoPlayerRadioButton);


        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridLayout(6, 2, 5, 10));
        newPanel.add(new Label("Maze - Num of Row"));
        this.numOfRowTextField = new TextField("5");
        newPanel.add(numOfRowTextField);
        newPanel.add(new Label("Maze - Num of Col"));
        this.numOfColTextField = new TextField("5");
        newPanel.add(this.numOfColTextField);


        newPanel.add(new Label("Maze - Num of superbat cells"));
        this.numOfSuperBatCellTextField = new TextField("1");
        newPanel.add(this.numOfSuperBatCellTextField);

        newPanel.add(new Label("Maze - Num of bottomless pits"));
        this.numOfBottomlessPitTextField = new TextField("1");
        newPanel.add(this.numOfBottomlessPitTextField);

        newPanel.add(new Label("Maze - Num of remaining walls"));
        this.numOfRemainingWallTextField = new TextField("5");
        newPanel.add(this.numOfRemainingWallTextField);

        mazeSettingPanel.add(panel);
        mazeSettingPanel.add(newPanel);

        return mazeSettingPanel;
    }

    public MazeInterface createMaze() {
        int type = mazeTypePerfectButton.isSelected() ? 1 : 2;
        boolean isWrapped = (mazeWrappedButton.isSelected());
        if (type == 2 && isWrapped) {
            type = 3;
        }
        PlayerMode playerMode = PlayerMode.SINGLE_PLAYER;
        if (twoPlayerRadioButton.isSelected()) {
            playerMode = PlayerMode.TWO_PLAYER;
        }
        int rows = Integer.parseInt(numOfRowTextField.getText());
        int cols = Integer.parseInt(numOfColTextField.getText());
        int remainingWalls = 0;
        if (type == 2 || type == 3) {
            remainingWalls = Integer.parseInt(numOfRemainingWallTextField.getText());
        }
        int batCells = Integer.parseInt(numOfSuperBatCellTextField.getText());
        int pitCells = Integer.parseInt(numOfBottomlessPitTextField.getText());

        int randomSeed = Integer.parseInt(randomSeedTextField.getText());


        MazeInterface maze;
        switch (type) {
            case 1:
                try {
                    maze = new PerfectMaze(rows, cols, isWrapped, batCells, pitCells, randomSeed, playerMode);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid maze.");
                    return null;
                }
                break;
            case 2:
                try {
                    maze = new RoomMaze(rows, cols, remainingWalls, isWrapped, batCells, pitCells, randomSeed, playerMode);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid maze.");
                    return null;
                }
                break;

            case 3:
                try {
                    maze = new WrappingRoomMaze(rows, cols, remainingWalls, batCells, pitCells, randomSeed, playerMode);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid maze.");
                    return null;
                }
                break;
            default:
                System.out.println("Invalid maze.");
                return null;
        }

        PlayerInterface player = new Player();
        int arrows = Integer.parseInt(playerNumOfArrowTextField.getText());
        try {
            maze.setPlayer(player, 0, 0, arrows);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments.");
            return null;
        }

        try {
            maze.setWumpusCell(0, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments.");
            return null;
        }
        return maze;
    }

    public void setRandomSeed(int randomSeed) {
        randomSeedTextField.setText("" + randomSeed);
    }
}

