package htw.model;

import java.util.List;

public interface MazeInterface {

    //creat a maze
    void creatMaze();

    // assign bottomless pits to cells
    void assignPitCells();

    // assign superbats to cells
    void assignBatCells();

    //set the player at the starter cell
    void setPlayer(PlayerInterface player, int x, int y, int arrows);

    PlayerInterface getPlayer();

    // set the goal cell
    void setWumpusCell(int x, int y);

    /**
     * @return player's possible directions that can move to
     */
    List<Direction> getPossibleMoves();

    //Given a specific position, return the possible moving directions
    List<Direction> getPossibleMoves(Cell cell);


    // make the player move to the dir
    String makeAMove(Direction dir);

    String shootAArrow(Cell pos, Direction dir, int dist);

    String setPlayerPos(Cell pos);


    Cell getStartCell();

    Cell getWumpusCell();

    // PlayerInterface getPlayer();

    int getRows();

    int getCols();

    int getRemainingWalls();

    boolean isWrapped();

    int getBatCells();

    int getPitCells();

    List<Cell> getAllCells();

    List<Edge> getAllEdges();

    List<Edge> getDeletedEdges();

    //just for test
    String printMaze();

    boolean isGameOver();

    public boolean isPitNearBy(Cell cell);

    public boolean isWumpusNearBy(Cell cell);

    List<Cell> getExploredCells();

    boolean isTwoPlayerMode();

    void changeTurn();

    int getPlayerTurn();

    PlayerInterface getPlayer2();

    PlayerInterface getCurrentPlayer();
}

