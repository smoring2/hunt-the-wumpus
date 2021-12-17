package htw.model;

import htw.PlayerMode;

/**
 * class RoomMaze
 * rows represents the number of row
 * cols represents the number of column
 * ramainingWalls represents the number of walls that remain in this maze
 * isWrapped represents whether the maze is a wrapped one
 */
public class RoomMaze extends AbstractMaze {
    public RoomMaze(int rows, int cols, int remainingWalls, boolean isWrapped, int batCells, int pitCells, int seed, PlayerMode playermode) {
        super(rows, cols, remainingWalls, isWrapped, batCells, pitCells, seed, playermode);
    }
}
