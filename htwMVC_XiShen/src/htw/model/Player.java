package htw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * class Player
 * int goldCount represents the gold the player has
 * currPosition is the current position of the player
 */
public class Player implements PlayerInterface {

    private Cell currPosition;
    private int posIndex;
    private PlayerStatus status;
    private int arrows;
    List<Cell> exploredCells = new ArrayList<>();

    public Player(int arrows) {
        this.arrows = arrows;
        status = PlayerStatus.LIVE;
        posIndex = 0;
    }

    public Player() {
        this.arrows = 0;
        status = PlayerStatus.LIVE;
        posIndex = 0;
    }

    @Override
    public void setCurrPosition(Cell c) {
        currPosition = c;
        exploredCells.add(c);
    }

    @Override
    public Cell getCurrPosition() {
        return currPosition;
    }

    @Override
    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    @Override
    public PlayerStatus getStatus() {
        return status;
    }

    @Override
    public void setArrows(int arrows) {
        this.arrows = arrows;
    }

    @Override
    public int getArrows() {
        return this.arrows;
    }

    @Override
    public int getPosIndex() {
        return posIndex;
    }

    @Override
    public void setPosIndex(int posIndex) {
        this.posIndex = posIndex;
    }

    @Override
    public List<Cell> getExploredCells() {
        return exploredCells;
    }

    @Override
    public String toString() {
        return "Player{" +
                ", arrows number: " + arrows +
                ", status = " + status +
                ", at the cell " + posIndex +
                ", currPosition=" + currPosition +
                '}';
    }
}
