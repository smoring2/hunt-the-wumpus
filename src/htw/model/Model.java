package htw.model;

import java.util.List;

public interface Model {
    List<Cell> getExploredCells();

    boolean isWin();

    boolean isGameOver();

    Cell getPlayerCell();

    Cell getWumpusCell();

    boolean isPitNearBy(Cell cell);

    boolean isWumpusNearBy(Cell cell);

    String move(Direction dir);

    String shoot(Direction dir, int dist);

    Cell getCurrentPlayerCell();

    void setMaze(MazeInterface maze);

    int getNumOfArrows();

    int getMazeRows();

    int getMazeCols();

    MazeInterface getMaze();

    void changeTurn();

    int getPlayerTurn();

    boolean isTwoPlayerMode();

    Cell getPlayer2Cell();

    PlayerInterface getCurrentPlayer();
}
