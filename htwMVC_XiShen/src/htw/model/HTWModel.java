package htw.model;

import htw.model.Model;

import java.util.List;

public class HTWModel implements Model {
    private MazeInterface maze;

    @Override
    public List<Cell> getExploredCells() {
        return maze.getExploredCells();
    }

    @Override
    public boolean isWin() {
        return maze.getCurrentPlayer().getStatus() == PlayerStatus.WIN;
    }

    @Override
    public boolean isGameOver() {
        return maze.isGameOver();
    }

    @Override
    public Cell getPlayerCell() {
        return maze.getPlayer().getCurrPosition();
    }

    @Override
    public Cell getWumpusCell() {
        return maze.getWumpusCell();
    }

    @Override
    public boolean isPitNearBy(Cell cell) {
        return maze.isPitNearBy(cell);
    }

    @Override
    public boolean isWumpusNearBy(Cell cell) {
        return maze.isWumpusNearBy(cell);
    }

    @Override
    public String move(Direction dir) {
        return maze.makeAMove(dir);
    }

    @Override
    public String shoot(Direction dir, int dist) {
        return maze.shootAArrow(getCurrentPlayerCell(), dir, dist);
    }

    @Override
    public Cell getCurrentPlayerCell() {
        return maze.getCurrentPlayer().getCurrPosition();
    }

    @Override
    public void setMaze(MazeInterface maze) {
        this.maze = maze;
    }

    @Override
    public int getNumOfArrows() {
        return maze.getCurrentPlayer().getArrows();
    }

    @Override
    public int getMazeRows() {
        return maze.getRows();
    }

    @Override
    public int getMazeCols() {
        return maze.getCols();
    }

    @Override
    public MazeInterface getMaze() {
        return maze;
    }

    @Override
    public void changeTurn() {
        maze.changeTurn();
    }

    @Override
    public int getPlayerTurn() {
        return maze.getPlayerTurn();
    }

    @Override
    public boolean isTwoPlayerMode() {
        return maze.isTwoPlayerMode();
    }

    @Override
    public Cell getPlayer2Cell() {
        return maze.getPlayer2().getCurrPosition();
    }

    @Override
    public PlayerInterface getCurrentPlayer() {
        return maze.getCurrentPlayer();
    }
}
