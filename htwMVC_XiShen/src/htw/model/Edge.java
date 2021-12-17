package htw.model;

/**
 * class Edge present a wall in the maze
 * Cell from and Cell to are the cells at two side of the edge
 */
public class Edge {
    private Cell from;
    private Cell to;

    // constructor
    public Edge(Cell from, Cell to) {
        this.from = from;
        this.to = to;
    }

    public Cell getFrom() {
        return from;
    }

    public Cell getTo() {
        return to;
    }

//    // override the equals() and hashcode() methods
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null || this.getClass() != obj.getClass()) {
//            return false;
//        }
//        Edge edge = (Edge) obj;
//        return (from.equals(edge.getFrom()) && to.equals(edge.getTo()));
//    }
//
//    @Override
//    public int hashCode() {
//        return from.hashCode() + to.hashCode();
//    }

    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from +
                ", to=" + to +
                '}';
    }
}
