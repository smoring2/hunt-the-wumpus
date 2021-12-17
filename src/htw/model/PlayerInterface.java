package htw.model;

import java.util.List;

/**
 * this interface represents a player in this maze game
 */
public interface PlayerInterface {


    //return the player's current position
    Cell getCurrPosition();

    //set the player's current position
    void setCurrPosition(Cell c);

    void setStatus(PlayerStatus status);
    PlayerStatus getStatus();

    void setArrows(int arrows);
    int getArrows();
    int getPosIndex();
    void setPosIndex(int posIndex);

    List<Cell> getExploredCells();

}
