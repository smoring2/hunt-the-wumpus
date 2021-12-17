package htw.model;

import htw.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CellTest {

    Cell cell;

    @Before
    public void setUp() throws Exception {
        cell = new Cell(0, 0);
    }

    @Test
    public void getX() {
        assertEquals(0, cell.getX());
    }

    @Test
    public void getY() {
        assertEquals(0, cell.getY());
    }

    @Test
    public void setNeighbors() {
        List<Cell> l = new ArrayList<>();
        l.add(new Cell(1, 1));
        cell.setNeighbors(l);
        assertEquals(l, cell.getNeighbors());
    }

    @Test
    public void getNeighbors() {
        assertEquals(new ArrayList<>(), cell.getNeighbors());
    }

    @Test
    public void setCellType() {
        cell.setCellType(CellType.PIT);
        assertEquals(CellType.PIT, cell.getCellType());
    }

    @Test
    public void testSetCellKind() {
        cell.setCellKind(CellKind.CAVE);
        assertEquals(cell.getCellKind(), CellKind.CAVE);

    }
}