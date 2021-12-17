package htw.view;

import htw.controller.Controller;
import htw.model.MazeInterface;

import javax.swing.*;
import java.awt.*;

public class HTWView implements View {

    private final JFrame mainFrame;
    private final MainPanel mainPanel;
    private final GamePanel gamePanel;
    private final MenuPanel menuPanel;
    private final ControlPanel controlPanel;
    private final JPanel displayPanel;
    private final JScrollPane gameScrollPane;
    private Controller controller;

    public HTWView() {

        this.gamePanel = new GamePanel(this);
        this.gameScrollPane = new JScrollPane(gamePanel);
        gameScrollPane.setPreferredSize(new Dimension(Parameters.WIDTH, Parameters.HEIGHT));
        this.menuPanel = new MenuPanel(this);
        this.displayPanel = new JPanel();

        displayPanel.setLayout(new BorderLayout());
        this.controlPanel = new ControlPanel(this);
        this.mainPanel = new MainPanel(this);

        displayPanel.add(menuPanel);
        mainPanel.add(displayPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.EAST);


        this.mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mainFrame.setTitle("Hunt The Wumpus");
        mainFrame.setResizable(false);
        mainFrame.getContentPane().add(mainPanel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public void showMessageDialog(String msg) {
        JOptionPane.showMessageDialog(mainFrame,
                msg);
    }

    @Override
    public void restoreMenu() {
        displayStatusMessage("Hunt the Wumpus!");
        displayPanel.remove(gameScrollPane);
        displayPanel.add(menuPanel, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        menuPanel.requestFocus();
    }

    @Override
    public void showGameArea() {
        displayPanel.remove(menuPanel);
        gamePanel.repaint();
        displayPanel.add(gameScrollPane, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        gamePanel.requestFocus();
    }

    @Override
    public void setController(Controller c) {
        this.controller = c;
    }

    @Override
    public void startShoot() {
        controlPanel.enableShootComponents();
        gamePanel.repaint();
    }

    @Override
    public void stopShoot() {
        controlPanel.disableShootComponents();
        gamePanel.repaint();
        gamePanel.requestFocus();
    }

    @Override
    public MazeInterface createMaze(String[] args) {
        MazeInterface maze = menuPanel.createMaze();
        gamePanel.setPreferredSize(
                new Dimension(maze.getCols() * Parameters.CELL_WIDTH,
                        maze.getRows() * Parameters.CELL_HEIGHT));
        int x = maze.getPlayer().getCurrPosition().getY() * Parameters.HEIGHT / maze.getCols();
        int y = maze.getPlayer().getCurrPosition().getX() * Parameters.WIDTH / maze.getRows();

        gameScrollPane.getViewport().setViewPosition(new Point(x, y));
        return maze;
    }

    @Override
    public void setRandomSeed(int randomSeed) {
        menuPanel.setRandomSeed(randomSeed);
    }


    @Override
    public void displayStatusMessage(String msg) {
        controlPanel.showMessage(msg);
    }

    @Override
    public void startGame() {

    }

    @Override
    public void showPlayerTurn(int playerTurn) {
        controlPanel.showPlayerTurn(playerTurn);
    }
}
