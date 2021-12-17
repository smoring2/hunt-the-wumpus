package htw.view;

import htw.controller.*;
import htw.model.MazeInterface;

public interface View {
    Controller getController();

    void showMessageDialog(String s);

    void restoreMenu();

    void showGameArea();

    void setController(Controller c);

    void startShoot();

    void stopShoot();

    MazeInterface createMaze(String[] args);

    void setRandomSeed(int randomSeed);

    void displayStatusMessage(String msg);

    void startGame();

    void showPlayerTurn(int playerTurn);

}
