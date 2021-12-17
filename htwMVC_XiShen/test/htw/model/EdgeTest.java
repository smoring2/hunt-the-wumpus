package htw.model;

import htw.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EdgeTest {

    Edge edge;
    Cell from = new Cell(0, 0);
    Cell to = new Cell(0, 1);

    @Before
    public void setUp() throws Exception {
        edge = new Edge(from, to);
    }

    @Test
    public void getFrom() {
        assertEquals(from, edge.getFrom());
    }

    @Test
    public void getTo() {
        assertEquals(to, edge.getTo());
    }

}