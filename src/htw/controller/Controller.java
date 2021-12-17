package htw.controller;

import htw.GameMode;
import htw.model.*;
import htw.view.*;


import java.util.List;

import static javax.swing.SwingUtilities.invokeLater;

public class Controller {

    private final View view;
    private final Model model;
    private boolean isShooting;


    public Controller(Model model, View view) {
        this.view = view;
        this.model = model;
    }

    public static void main(String[] args) {
        invokeLater(() -> {
            new ModeSelectMenu();
        });
    }

    public static void init(GameMode gameMode) {
        invokeLater(() -> {

            View view;
            if (gameMode == GameMode.GUI) {
                view = new HTWView();
            } else {
                view = new HTWTextView();
            }
            Model model = new HTWModel();
            Controller c = new Controller(model, view);
            view.setController(c);
            c.startGame();

        });
    }

    private void startGame() {
        view.startGame();
    }

    public List<Cell> getExploredCells() {
        return model.getExploredCells();
    }

    public boolean isWin() {
        return model.isWin();
    }

    public boolean isGameOver() {
        return model.isGameOver();
    }

    public Cell getPlayerCell() {
        return model.getPlayerCell();
    }

    public Cell getWumpusCell() {
        return model.getWumpusCell();

    }

    public boolean isPitNearBy(Cell cell) {
        return model.isPitNearBy(cell);
    }

    public boolean isWumpusNearBy(Cell cell) {
        return model.isWumpusNearBy(cell);
    }

    public boolean isShooting() {
        return isShooting;
    }

    public void stopShoot() {
        isShooting = false;
        view.stopShoot();
        updateNumOfArrow();
        checkStatus();
    }

    private void updateNumOfArrow() {
        view.showPlayerTurn(model.getPlayerTurn());
    }

    public String move(Direction dir) {
        String str = "";
        try {
            str = model.move(dir);
            view.displayStatusMessage("Player " + model.getPlayerTurn() + " moved towards " + dir + "\n" + str);

            model.changeTurn();
            view.showPlayerTurn(model.getPlayerTurn());
            Cell cell = getPlayerCell();
            if (cell.getCellKind() == CellKind.CAVE && isPitNearBy(cell)) {
                view.displayStatusMessage("you feel a draft");
            }

            if (cell.getCellKind() == CellKind.CAVE && isWumpusNearBy(cell)) {
                view.displayStatusMessage("you smell the wumpus");
            }

        } catch (Exception e) {
            System.out.println("Invalid move!");
        }
        return str;
    }

    public void startShoot() {
        isShooting = true;
        view.startShoot();
        view.displayStatusMessage("start shoot, \nuse control panel to select dir and dist, \npress ESC to cancel");

    }

    public String shoot(Direction dir, int dist) {
        String str = model.shoot(dir, dist);
        view.displayStatusMessage("Player " + model.getPlayerTurn() + " shoot \n towards " + dir + " with dist: " + dist);
        checkStatus();
        model.changeTurn();
        view.showPlayerTurn(model.getPlayerTurn());
        return str;
    }

    public void checkStatus() {
        if (isWin()) {
            view.showMessageDialog("YOU WIN! Press ESC to continue");
        } else if (isGameOver()) {
            view.showMessageDialog("You are dead, game over! Press ESC to continue");
        }
    }

    public void clickMove(int x, int y) {
        Cell cell = model.getCurrentPlayerCell();
        Direction dir = null;
        if (x == cell.getX()) {
            if (y == cell.getY() - 1) {
                dir = Direction.WEST;
            } else if (y == cell.getY() + 1) {
                dir = Direction.EAST;
            }
        } else if (y == cell.getY()) {
            if (x == cell.getX() - 1) {
                dir = Direction.NORTH;
            } else if (x == cell.getX() + 1) {
                dir = Direction.SOUTH;
            }
        }
        if (dir != null) {
            move(dir);
        }
    }

    public void initMaze() {
        try {
            MazeInterface maze = view.createMaze(new String[]{});
            model.setMaze(maze);
            updateNumOfArrow();
            view.showGameArea();
        } catch (Exception e) {
            System.out.println("creating maze exception " + e);
            view.showMessageDialog("Invalid Maze!");
        }
    }

    public int getMazeRows() {
        return model.getMazeRows();
    }

    public int getMazeCols() {
        return model.getMazeCols();
    }

    public MazeInterface getMaze() {
        return model.getMaze();
    }

    public boolean isTwoPlayerMode() {
        return model.isTwoPlayerMode();
    }

    public Cell getPlayer2Cell() {
        return model.getPlayer2Cell();
    }

    public int getCurrentPlayerArrow() {
        return model.getCurrentPlayer().getArrows();
    }
}
