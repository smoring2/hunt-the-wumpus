package htw.model;

import htw.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    PlayerInterface play;
    Cell pos = new Cell(0, 0);

    @Before
    public void setUp() throws Exception {
        play = new Player();
    }

    @Test
    public void setCurrPosition() {
        play.setCurrPosition(pos);
        assertEquals(pos, play.getCurrPosition());
    }

    @Test
    public void setStatus() {
        play.setStatus(PlayerStatus.LIVE);
        assertEquals(play.getStatus(), PlayerStatus.LIVE);
    }

    @Test
    public void getArrows() {
        play.setArrows(10);
        assertEquals(10, play.getArrows());
    }

    @Test
    public void getCurrPosition() {
        play.setCurrPosition(pos);
        assertEquals(pos, play.getCurrPosition());
    }

}