package htw.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class Cell represents a cell in this maze
 * variable x and y represents the coordinates
 * goldCount is the number of gold in one cell
 * cellType is the type of one cell, it can be NORMAL, or GOLD, or THIEF
 * List<Cell> neighbors is the cells that the cell can move to
 */
public class Cell {
    private int x;
    private int y;
    private CellType cellType;
    private CellKind cellKind;
    private List<Cell> neighbors;

    //constructor
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        cellType = CellType.NORMAL;
        cellKind = CellKind.CAVE;
        neighbors = new ArrayList<>();
    }

    public Cell() {
        this.x = -1;
        this.y = -1;
        cellType = CellType.NORMAL;
        cellKind = CellKind.CAVE;
        neighbors = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellType getCellType() {
        return cellType;
    }


    // set the neighbors of one cell
    public void setNeighbors(List<Cell> neighbors) {
        this.neighbors = neighbors;
    }

    public List<Cell> getNeighbors() {
        return neighbors;
    }

    public CellKind getCellKind() {
        return cellKind;
    }

    public void setCellKind(CellKind cellKind) {
        this.cellKind = cellKind;
    }

    // set the cell's type
    //if GOLD, assign one gold in it
    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }


    // override the equals() and hashcode() method
    @Override
    public boolean equals(Object c) {
        if (this == c) {
            return true;
        }
        if (c == null || getClass() != c.getClass()) {
            return false;
        }
        Cell cell = (Cell) c;
        return this.getX() == cell.getX() && this.getY() == cell.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", cellKind =" + cellKind +
                ", cellType=" + cellType +
                '}';
    }
}
