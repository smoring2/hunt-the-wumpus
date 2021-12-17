package htw.model;

import htw.PlayerMode;

/**
 * class PerfectMaze
 * rows represents the number of row
 * cols represents the number of column
 * the number of ramainingWalls of a perfect maze can be decided by the maze's properties
 * isWrapped represents whether the maze is a wrapped one
 */
public class PerfectMaze extends AbstractMaze {
    public PerfectMaze(int rows, int cols, boolean isWrapped, int batCells, int pitCells, int seed, PlayerMode playerMode) {
        super(rows, cols, isWrapped, batCells, pitCells, seed, playerMode);
    }

}
