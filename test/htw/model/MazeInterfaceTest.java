package htw.model;

import htw.PlayerMode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MazeInterfaceTest {

    MazeInterface maze;

    @Before
    public void setUp() throws Exception {
        maze = new RoomMaze(1, 3, 0, false, 0, 0, 10, PlayerMode.SINGLE_PLAYER);
        //   System.out.println(maze.printMaze());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUp1() {
        maze = new RoomMaze(-1, 3, 3, false, 2, 2, 10, PlayerMode.SINGLE_PLAYER);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUp2() {
        maze = new RoomMaze(1, 3, 10, false, 2, 3, 10, PlayerMode.SINGLE_PLAYER);
    }


    @org.junit.Test
    public void getPitCells() {
        int pitCell = 0;
        for (Cell cell : maze.getAllCells()) {
            if (cell.getCellType() == CellType.PIT
                    || cell.getCellType() == CellType.PITANDBAT) {
                pitCell++;
            }
        }
        assertEquals(maze.getPitCells(), pitCell);
    }

    @org.junit.Test
    public void testGetBatCells() {

        assertEquals(maze.getBatCells(), 0);
    }

    @org.junit.Test
    public void testGetPitCells() {

        assertEquals(maze.getPitCells(), 0);
    }

    @org.junit.Test
    public void getBatCells() {
        int batCell = 0;
        for (Cell cell : maze.getAllCells()) {
            if (cell.getCellType() == CellType.BAT
                    || cell.getCellType() == CellType.PITANDBAT) {
                batCell++;
            }
        }
        assertEquals(maze.getBatCells(), batCell);
    }


    @org.junit.Test
    public void getRows() {
        assertEquals(1, maze.getRows());
    }

    @org.junit.Test
    public void getCols() {
        assertEquals(3, maze.getCols());
    }

    @org.junit.Test
    public void getRemainingWalls() {
        assertEquals(0, maze.getRemainingWalls());
    }

    @org.junit.Test
    public void isWrapped() {
        assertEquals(false, maze.isWrapped());
    }

    @org.junit.Test
    public void getAllCells() {
        assertEquals(3, maze.getAllCells().size());
    }

    @org.junit.Test
    public void getAllEdges() {
        assertEquals(2, maze.getAllEdges().size());
    }

    @org.junit.Test
    public void getDeletedEdges() {
        assertEquals(2, maze.getDeletedEdges().size());
    }
}