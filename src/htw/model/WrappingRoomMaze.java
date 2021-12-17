package htw.model;

import htw.PlayerMode;

/**
 * class WrappingRoomMaze
 * rows represents the number of row
 * cols represents the number of column
 * ramainingWalls represents the number of walls that remain in this maze
 * isWrapped is true since it is a wrapped maze
 */
public class WrappingRoomMaze extends RoomMaze {
    public WrappingRoomMaze(int rows, int cols, int remainingWalls, int batCells, int pitCells, int seed, PlayerMode playermode) {
        super(rows, cols, remainingWalls, true, batCells, pitCells, seed, playermode);
    }
}
