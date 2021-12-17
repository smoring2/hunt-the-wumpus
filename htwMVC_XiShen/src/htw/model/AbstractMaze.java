package htw.model;

import htw.PlayerMode;

import java.util.*;

/**
 * Class AbstractMaze can creat a maze, and get possible moves given position
 * and make a player to move. It can find a normal route and another route with maximum gold
 * from the starter to the wumpus
 */
public class AbstractMaze implements MazeInterface {
    private final int rows;
    private final int cols;
    private final int remainingWalls;
    private final boolean isWrapped;
    private final int batCells;
    private final int pitCells;
    private final double chanceInBAT = 0.5;
    private final double chanceInBATPIT = 0.5;
    private final int seed;
    private final PlayerMode playerMode;

    private PlayerInterface player1;
    private PlayerInterface player2;
    private Cell startCell;
    private Cell wumpusCell;
    private final Random rand;


    //cellMap to store a cell with index i and index j
    private HashMap<Integer, HashMap<Integer, Cell>> cellMap = new HashMap<>();

    //allCells contains all the cells in this maze
    private final List<Cell> allCells = new ArrayList<>();
    // Store all the cells and the index
    private HashMap<Cell, Integer> cellIndex = new HashMap<>();
    //allEdges contains all the walls between two cells
    private final List<Edge> allEdges = new ArrayList<>();
    //deletedEdges contains all the walls that are removed from this maze
    private final List<Edge> deletedEdges = new ArrayList<>();
    //remainingEdges contains all the walls that remain in this maze
    private final List<Edge> remainingEdges = new ArrayList<>();
    private List<Cell> exploredCells = new ArrayList<>();

    private int maxWalls;
    private int allWalls;
    private boolean player1turn = true;

    private void getMinWalls(int rows, int cols, boolean isWrapped) {
        int numOfEdges = isWrapped ? 2 * (rows + 1) * (cols + 1) - (2 * rows + 1) - (2 * cols + 1) :
                2 * rows * cols - rows - cols;
        this.allWalls = numOfEdges;
        this.maxWalls = numOfEdges - rows * cols + 1;
    }


    // constructor for RoomMaze and WrappingRoomMaze
    public AbstractMaze(int rows, int cols, int remainingWalls, boolean isWrapped,
                        int batCells, int pitCells, int seed,
                        PlayerMode playerMode) {
        this.getMinWalls(rows, cols, isWrapped);
        checkValidness(rows, cols, remainingWalls, isWrapped,
                batCells, pitCells);

        this.rows = rows;
        this.cols = cols;
        this.remainingWalls = remainingWalls;
        this.isWrapped = isWrapped;
        this.batCells = batCells;
        this.pitCells = pitCells;
        this.seed = seed;
        rand = new Random(this.seed);
        this.playerMode = playerMode;
        creatMaze();
    }

    private void checkValidness(int rows, int cols, int remainingWalls, boolean isWrapped,
                                int batCells, int pitCells) {

        if (rows <= 0 || cols <= 0 || remainingWalls < 0
                || remainingWalls > maxWalls
                || batCells < 0 || pitCells < 0
                || batCells > maxWalls || pitCells > maxWalls) {
            throw new IllegalArgumentException("Invalid maze");
        }
    }

    //constructor for PerfectMaze
    public AbstractMaze(int rows, int cols, boolean isWrapped,
                        int batCells, int pitCells, int seed, PlayerMode playerMode) {
        this.getMinWalls(rows, cols, isWrapped);
        checkValidness(rows, cols, this.maxWalls, isWrapped,
                batCells, pitCells);

        this.rows = rows;
        this.cols = cols;
        this.isWrapped = isWrapped;
        this.remainingWalls = this.maxWalls;
        this.batCells = batCells;
        this.pitCells = pitCells;
        this.seed = seed;
        rand = new Random(this.seed);
        this.playerMode = playerMode;
        creatMaze();
    }

    /**
     * @param p player
     * @param x the player's starter cell's x
     * @param y the player's starter's cell's y
     */
    @Override
    public void setPlayer(PlayerInterface p, int x, int y, int arrows) {
        if (x < 0 || x >= rows || y < 0 || y >= cols) {
            throw new IllegalArgumentException("Invalid play's start cell");
        }
        if (arrows <= 0) {
            throw new IllegalArgumentException("Invalid arrow's amount");
        }
        x = rand.nextInt(rows);
        y = rand.nextInt(cols);
        startCell = cellMap.get(x).get(y);
        if (startCell.getCellType() != CellType.NORMAL || startCell.getCellKind() != CellKind.CAVE) {
            this.setPlayer(p, x, y, arrows);
        }
        player1 = p;
        player1.setCurrPosition(startCell);
        player1.setPosIndex(cellIndex.get(player1.getCurrPosition()));
        player1.setArrows(arrows);
        exploredCells.add(startCell);

        if (playerMode == PlayerMode.TWO_PLAYER) {
            player2 = new Player();
            player2.setArrows(arrows);
            player2.setCurrPosition(startCell);
            player2.setPosIndex(player2.getPosIndex());
        }

    }

    @Override
    public PlayerInterface getPlayer() {
        return player1;
    }

    @Override
    public int getRows() {
        return rows;
    }


    @Override
    public int getCols() {
        return cols;
    }


    @Override
    public int getRemainingWalls() {
        return remainingWalls;
    }

    @Override
    public List<Cell> getAllCells() {
        return allCells;
    }

    @Override
    public List<Edge> getAllEdges() {
        return allEdges;
    }

    @Override
    public int getBatCells() {
        return batCells;
    }

    @Override
    public int getPitCells() {
        return pitCells;
    }

    @Override
    public List<Edge> getDeletedEdges() {
        return deletedEdges;
    }

    @Override
    public boolean isWrapped() {
        return isWrapped;
    }

    /**
     * @param x the wumpus cell's x
     * @param y the wumpus cell's xy
     */
    @Override
    public void setWumpusCell(int x, int y) {
        if (x < 0 || x >= rows || y < 0 || y >= cols) {
            throw new IllegalArgumentException("Invalid wumpus cell");
        }
        x = rand.nextInt(rows);
        y = rand.nextInt(cols);
        wumpusCell = cellMap.get(x).get(y);
        if (wumpusCell.getCellKind() != CellKind.CAVE
                || wumpusCell.getCellType() != CellType.NORMAL
                || wumpusCell.equals(startCell)) {
            this.setWumpusCell(x, y);
        }
    }

    @Override
    public Cell getWumpusCell() {
        return wumpusCell;
    }

    @Override
    public Cell getStartCell() {
        return startCell;
    }

    /**
     * creat all the cells and set their neighbor cells
     */
    private void creatCellsAndNeighbors() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell curr = new Cell(i, j);
                allCells.add(curr);
                int index = i * rows + j + 1;
                cellIndex.put(curr, index);
                if (!cellMap.containsKey(i)) {
                    cellMap.put(i, new HashMap<>());
                }
                cellMap.get(i).put(j, curr);
            }
        }
        int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell curr = cellMap.get(i).get(j);
                for (int[] dir : dirs) {
                    int newi = i + dir[0];
                    int newj = j + dir[1];
                    if (isWrapped || (newi >= 0 && newi < rows && newj >= 0 && newj < cols)) {
                        Cell neigh = cellMap.get((newi + rows) % rows).get((newj + cols) % cols);
                        curr.getNeighbors().add(neigh);
                    }
                }
            }
        }
    }

    /**
     * creat all the walls between two edges
     */
    private void createAllEdges() {
        creatCellsAndNeighbors();
        HashSet<Cell> visited = new HashSet<>();
        for (Cell curr : allCells) {
            List<Cell> neighs = curr.getNeighbors();
            for (Cell neigh : neighs) {
                if (!visited.contains(neigh)) {
                    Edge edge = new Edge(curr, neigh);
                    allEdges.add(edge);
                }
            }
            visited.add(curr);
        }
    }

    // Use union-find to make the maze
    //parents HashMap to store the cell and its parent cell
    private final HashMap<Cell, Cell> parents = new HashMap<>();

    //Find a cell's parent
    private Cell findParent(Cell p) {
        while (!parents.get(p).equals(p)) {
            p = parents.get(p);
        }
        return p;
    }

    //If two cells' parents are not the same one, union them
    private void union(Cell x, Cell y) {
        Cell px = findParent(x);
        Cell py = findParent(y);
        if (!px.equals(py)) {
            parents.put(px, py);
        }
    }

    //Two cells if can union together then remove the wall between them
    // Union-find algorithm can get a perfect maze
    // If remainingWalls's value is smaller than the remainingEdges list's size
    // delete the edge in the remainingEdges and add it to the deletedEdges till
    // remainingEdges' size is equal to remainingWalls
    private void createDeletedEdges() {
        createAllEdges();
        for (Cell cell : allCells) {
            parents.put(cell, cell);
        }
        int target = this.allWalls - this.maxWalls;
        while (target > 0) {
            Edge edge = allEdges.get(rand.nextInt(allEdges.size()));
            Cell x = edge.getFrom();
            Cell y = edge.getTo();
            Cell px = findParent(x);
            Cell py = findParent(y);
            if (!px.equals(py)) {
                union(x, y);
                deletedEdges.add(edge);
                target--;
            }
        }
//        for (Edge edge : allEdges) {
//            Cell x = edge.getFrom();
//            Cell y = edge.getTo();
//            Cell px = findParent(x);
//            Cell py = findParent(y);
//            if (!px.equals(py)) {
//                union(x, y);
//                deletedEdges.add(edge);
//            }
//        }

        for (Edge edge : allEdges) {
            if (!deletedEdges.contains(edge)) {
                remainingEdges.add(edge);
            }
        }

        if (remainingEdges.size() < this.remainingWalls) {
            throw new IllegalArgumentException("Too much remaining Walls");
        }
        int remainSize = remainingEdges.size();
        while (remainSize > this.remainingWalls) {
            Edge edge = remainingEdges.get(rand.nextInt(remainingEdges.size()));
            if (!deletedEdges.contains(edge)) {
                deletedEdges.add(edge);
                remainSize--;
            }
        }
//        for (Edge edge : remainingEdges) {
//            if (remainSize > this.remainingWalls) {
//                deletedEdges.add(edge);
//                remainSize--;
//            } else {
//                break;
//            }
//        }

    }


    //Set the cells are neighbors between any edges in the deletedEdges list
    //A cell is another cell's  neighbor means that they are connected
    @Override
    public void creatMaze() throws IllegalArgumentException {
        createDeletedEdges();
        for (Cell c : allCells) {
            c.setNeighbors(new ArrayList<>());
        }
        for (Edge edge : deletedEdges) {
            Cell x = edge.getFrom();
            Cell y = edge.getTo();
            x.getNeighbors().add(y);
            y.getNeighbors().add(x);
        }
        getCellsKind();
        if (numberOfCaves < pitCells) {
            throw new IllegalArgumentException("Too much pit cells");
        }
        if (numberOfCaves < batCells) {
            throw new IllegalArgumentException("Too much superbats cells");
        }
        assignBatCells();
        assignPitCells();
    }

    private int numberOfCaves = 0;
    private int numberOfTunnels = 0;

    private void getCellsKind() {
        for (Cell c : allCells) {
            if (c.getNeighbors().size() == 2) {
                c.setCellKind(CellKind.TUNNEL);
                numberOfTunnels++;
            } else {
                numberOfCaves++;
            }
        }
    }

    //Assign some cells BAT
    public void assignBatCells() {
        int batCounts = batCells;
        while (batCounts != 0) {
            Cell cell = allCells.get(rand.nextInt(allCells.size()));
            if (cell.getCellType().equals(CellType.NORMAL)
                    && cell.getCellKind().equals(CellKind.CAVE)
                    && !cell.equals(startCell) && !cell.equals(wumpusCell)) {
                cell.setCellType(CellType.BAT);
                batCounts--;
            }
        }
    }

    //Assign some cells a PIT
    @Override
    public void assignPitCells() {
        int pitCounts = pitCells;
        while (pitCounts != 0) {
            Cell cell = allCells.get(rand.nextInt(allCells.size()));
            if (cell.getCellType().equals(CellType.NORMAL)
                    && cell.getCellKind().equals(CellKind.CAVE)
                    && !cell.equals(startCell) && !cell.equals(wumpusCell)) {

                cell.setCellType(CellType.PIT);

                pitCounts--;
            }
        }
    }

    // get all the possible moves of the player's current position
    // if the current position have neighbors, and it can move to the neighbor
    @Override
    public List<Direction> getPossibleMoves() {
        PlayerInterface player = player1turn ? player1 : player2;
        return getPossibleMoves(player.getCurrPosition());
    }

    @Override
    public List<Direction> getPossibleMoves(Cell cell) {
        int x = cell.getX();
        int y = cell.getY();
        List<Direction> list = new ArrayList<>();


        //To North
        Cell nextPosition = cellMap.get((x - 1 + rows) % rows).get(y);
        if (cell.getNeighbors().contains(nextPosition)) {
            list.add(Direction.NORTH);
        }
        //To South
        nextPosition = cellMap.get((x + 1) % rows).get(y);
        if (cell.getNeighbors().contains(nextPosition)) {
            list.add(Direction.SOUTH);
        }
        //To West
        nextPosition = cellMap.get(x).get((y - 1 + cols) % cols);
        if (cell.getNeighbors().contains(nextPosition)) {
            list.add(Direction.WEST);
        }
        //To East
        nextPosition = cellMap.get(x).get((y + 1) % cols);
        if (cell.getNeighbors().contains(nextPosition)) {
            list.add(Direction.EAST);
        }
        return list;
    }

    //change the player's position by the given direction
    //if next position has gold, then the player can collect gold
    //if next position has a thief, then the player will lose gold
    public String makeAMove(Direction dir) {
        PlayerInterface player = player1turn ? player1 : player2;
//        PlayerInterface player = player1;
        Cell currPosition = player.getCurrPosition();
        List<Direction> dirs = new ArrayList<>(getPossibleMoves());
        if (!dirs.contains(dir)) {
            throw new IllegalArgumentException("Invalid Move");
        }
        int x = currPosition.getX();
        int y = currPosition.getY();
        Cell nextPosition = new Cell();
        switch (dir) {
            case NORTH:
                nextPosition = cellMap.get((x - 1 + rows) % rows).get(y);
                break;
            case SOUTH:
                nextPosition = cellMap.get((x + 1) % rows).get(y);
                break;
            case WEST:
                nextPosition = cellMap.get(x).get((y - 1 + cols) % cols);
                break;
            case EAST:
                nextPosition = cellMap.get(x).get((y + 1) % cols);
            default:
                break;
        }

        if (nextPosition.getCellKind().equals(CellKind.TUNNEL)) {
            player.setCurrPosition(nextPosition);
            exploredCells.add(nextPosition);
            player.setPosIndex(cellIndex.get(player.getCurrPosition()));
            List<Direction> nextDirs = new ArrayList<>(getPossibleMoves());
            for (Direction d : nextDirs) {
                if (!getOp(d).equals(dir)) {
                    return makeAMove(d);
                }
            }
        }
        return setPlayerPos(nextPosition);
    }

    private Cell makeAMove(Cell currPosition, Direction dir) {
        List<Direction> dirs = new ArrayList<>(getPossibleMoves(currPosition));
        if (!dirs.contains(dir)) {
            throw new IllegalArgumentException("Invalid Move");
        }
        int x = currPosition.getX();
        int y = currPosition.getY();
        Cell nextPosition = new Cell();
        switch (dir) {
            case NORTH:
                nextPosition = cellMap.get((x - 1 + rows) % rows).get(y);
                break;
            case SOUTH:
                nextPosition = cellMap.get((x + 1) % rows).get(y);
                break;
            case WEST:
                nextPosition = cellMap.get(x).get((y - 1 + cols) % cols);
                break;
            case EAST:
                nextPosition = cellMap.get(x).get((y + 1) % cols);
            default:
                break;
        }
        return nextPosition;
    }

    private Cell moveToCave(Cell currPosition, Direction dir) {
        List<Direction> dirs = new ArrayList<>(getPossibleMoves(currPosition));
        if (!dirs.contains(dir)) {
            throw new IllegalArgumentException("Invalid Move");
        }
        int x = currPosition.getX();
        int y = currPosition.getY();
        Cell nextPosition = new Cell();
        switch (dir) {
            case NORTH:
                nextPosition = cellMap.get((x - 1 + rows) % rows).get(y);
                break;
            case SOUTH:
                nextPosition = cellMap.get((x + 1) % rows).get(y);
                break;
            case WEST:
                nextPosition = cellMap.get(x).get((y - 1 + cols) % cols);
                break;
            case EAST:
                nextPosition = cellMap.get(x).get((y + 1) % cols);
            default:
                break;
        }
        if (nextPosition.getCellKind().equals(CellKind.TUNNEL)) {
            List<Direction> nextDirs = new ArrayList<>(getPossibleMoves(nextPosition));
            for (Direction d : nextDirs) {
                if (!getOp(d).equals(dir)) {
                    return moveToCave(nextPosition, d);
                }
            }
        }
        return nextPosition;
    }

    private String meetPIT(Cell pos) {
        PlayerInterface player = player1turn ? player1 : player2;
        String s = "";
        s += "You fall into a pit. Bye\n";
        s += "Better luck next time";
        player.setStatus(PlayerStatus.DEAD);
        player.setCurrPosition(pos);
        player.setPosIndex(cellIndex.get(player.getCurrPosition()));
        return s;
    }

    private String grabbedByBAT(Cell pos) {
        String s = "";
        s += "grabbed by superbats\n";

        int nextX = rand.nextInt(rows);
        int nextY = rand.nextInt(cols);
        Cell next = cellMap.get(nextX).get(nextY);
        while (next.getCellKind().equals(CellKind.TUNNEL)) {
            nextX = rand.nextInt(rows);
            nextY = rand.nextInt(cols);
            next = cellMap.get(nextX).get(nextY);
        }
        s += setPlayerPos(next);
        return s;
    }

    @Override
    public String setPlayerPos(Cell pos) {
        exploredCells.add(pos);
        PlayerInterface player = player1turn ? player1 : player2;
        CellType type = pos.getCellType();
        String s = "";

        switch (type) {
            case PIT:
                s += meetPIT(pos);
                break;
            case BAT:
                if (rand.nextDouble() <= chanceInBAT) {
                    s += grabbedByBAT(pos);
                } else {
                    s += "successfully duck superbats\n";
                    player.setCurrPosition(pos);
                    player.setPosIndex(cellIndex.get(player.getCurrPosition()));
                }
                break;
            case PITANDBAT:
                if (rand.nextDouble() <= chanceInBATPIT) {
                    s += grabbedByBAT(pos);
                } else {
                    s += meetPIT(pos);
                }
                break;

            default:
                // If next position has wumpus
                if (pos.equals(getWumpusCell())) {
                    s += "thanks for feeding the Wumpus!\n";
                    s += "Better luck next time";
                    player.setStatus(PlayerStatus.DEAD);
                }
                player.setCurrPosition(pos);
                player.setPosIndex(cellIndex.get(player.getCurrPosition()));
        }
        //If player is still alive, we check for nearby warnings
        boolean findPit = false;
        if (player.getStatus().equals(PlayerStatus.LIVE)) {
            List<Direction> dirs = getPossibleMoves();
            for (Direction dir : dirs) {
                Cell neigh = makeAMove(player.getCurrPosition(), dir);
                if (neigh.getCellType().equals(CellType.PIT)
                        || neigh.getCellType().equals(CellType.PITANDBAT)) {
                    if (!findPit) {
                        findPit = true;
                        s += "You feel a draft";
                    }
                }
                if (neigh.equals(getWumpusCell())) {
                    s += "You smell a Wumpus!\n";
                }
            }
        }
        return s;
    }

    public boolean isPitNearBy(Cell cell) {
        PlayerInterface player = player1turn ? player1 : player2;
        if (player.getStatus().equals(PlayerStatus.LIVE)) {
            List<Direction> dirs = getPossibleMoves(cell);
            for (Direction dir : dirs) {
                Cell neigh = moveToCave(cell, dir);//player.getCurrPosition(), dir);
                //Cell neigh = makeAMove(cell, dir);
                if (neigh.getCellType().equals(CellType.PIT)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWumpusNearBy(Cell cell) {
        PlayerInterface player = player1turn ? player1 : player2;

        if (player.getStatus().equals(PlayerStatus.LIVE)) {
            List<Direction> dirs = getPossibleMoves(cell);
            for (Direction dir : dirs) {
                Cell neigh = moveToCave(cell, dir);//player.getCurrPosition(), dir);
                // Cell neigh = makeAMove(cell, dir);
                if (neigh.equals(getWumpusCell())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void changeTurn() {
        if (playerMode == PlayerMode.TWO_PLAYER) {
            PlayerInterface theOtherPlayer = player1turn ? player2 : player1;
            if (theOtherPlayer.getStatus() == PlayerStatus.LIVE) {
                player1turn = !player1turn;
            }
        }
    }

    @Override
    public int getPlayerTurn() {
        return player1turn ? 1 : 2;
    }

    @Override
    public List<Cell> getExploredCells() {
        return exploredCells;
    }

    @Override
    public boolean isTwoPlayerMode() {
        return playerMode == PlayerMode.TWO_PLAYER;
    }

    @Override
    public PlayerInterface getPlayer2() {
        return player2;
    }

    @Override
    public PlayerInterface getCurrentPlayer() {
        return player1turn ? player1 : player2;
    }


    private Direction getOp(Direction direction) {
        switch (direction) {
            case EAST:
                return Direction.WEST;
            case WEST:
                return Direction.EAST;
            case NORTH:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.NORTH;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public String shootAArrow(Cell pos, Direction dir, int dist) {
        PlayerInterface player = player1turn ? player1 : player2;

        if (dist < 0 || player.getArrows() <= 0) {
            throw new IllegalArgumentException("Invalid shoot");
        }

        List<Direction> possibleDirs = getPossibleMoves(pos);
        String s = "";
        if (!possibleDirs.contains(dir)) {
            player.setArrows(player.getArrows() - 1);
            if (player.getArrows() == 0) {
                s += "You have run out of the arrows.\n";
                s += "Better luck next time";
                player.setStatus(PlayerStatus.DEAD);
            }
            return s;
        }
        return getArrowPosition(pos, dir, dist);
    }

    private String getArrowPosition(Cell curr, Direction direction, int dist) {
        PlayerInterface player = player1turn ? player1 : player2;

        String s = "";
        if (dist == 0 && curr.equals(wumpusCell)) {
            s += "Hee hee hee, you got the wumpus! \n";
            s += "Next time you won't be so lucky \n";
            player.setStatus(PlayerStatus.WIN);
            player.setArrows(player.getArrows() - 1);
            return s;
        }
        if (dist == 0 || getPossibleMoves(curr).size() == 0 || !getPossibleMoves(curr).contains(direction)) {
            player.setArrows(player.getArrows() - 1);
            if (player.getArrows() == 0) {
                player.setStatus(PlayerStatus.DEAD);
            }
            return "";

        }

        Cell next = makeAMove(curr, direction);
//        try {
//            next = makeAMove(curr, direction);
//        }catch (IllegalArgumentException exception){
//            return "";
//        }

        if (next.getCellKind().equals(CellKind.TUNNEL)) {
            List<Direction> dirs = getPossibleMoves(next);
            for (Direction dir : dirs) {
                if (!dir.equals(getOp(direction))) {
                    return s + getArrowPosition(next, dir, dist);
                }
            }
        } else {
            return s + getArrowPosition(next, direction, dist - 1);
        }
        return "";
    }

    @Override
    public String toString() {
        return "AbstractMaze{" +
                "rows=" + rows +
                ", cols=" + cols +
                ", remainingWalls=" + remainingWalls +
                ", isWrapped=" + isWrapped +
                ", deletedEdges=" + deletedEdges +
                '}';
    }


    //print the maze for test
    @Override
    public String printMaze() {
        char[][] board = new char[2 * rows + 1][2 * cols + 1];
        StringBuilder maze = new StringBuilder();
        //Arrays.fill(board, ' ');
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Cell curr = cellMap.get(i).get(j);
                Cell left = cellMap.get(i).get((j - 1 + cols) % cols);
                Cell up = cellMap.get((i - 1 + rows) % rows).get(j);
                board[i * 2][2 * j] = '*';
                if (!curr.getNeighbors().contains(left)) {
                    board[i * 2 + 1][2 * j] = '|';
                } else {
                    board[i * 2 + 1][2 * j] = ' ';
                }

                if (!curr.getNeighbors().contains(up)) {
                    board[i * 2][2 * j + 1] = '-';
                } else {
                    board[i * 2][2 * j + 1] = ' ';
                }

                if (curr.getCellType() == CellType.BAT) {
                    board[i * 2 + 1][j * 2 + 1] = 'B';
                } else if (curr.getCellType() == CellType.PIT) {
                    board[2 * i + 1][2 * j + 1] = 'P';
                } else if (curr.getCellType() == CellType.PITANDBAT) {
                    board[2 * i + 1][2 * j + 1] = 'A';
                } else if (curr.equals(wumpusCell)) {
                    board[2 * i + 1][2 * j + 1] = 'W';
                } else {
                    board[2 * i + 1][2 * j + 1] = ' ';
                }

                if (i == rows - 1) {
                    board[i * 2 + 2][2 * j] = '*';
                    Cell down = cellMap.get((i + 1) % rows).get(j);
                    if (!curr.getNeighbors().contains(down)) {
                        board[2 * i + 2][2 * j + 1] = '-';
                    } else {
                        board[2 * i + 2][2 * j + 1] = ' ';
                    }
                }

                if (j == cols - 1) {
                    board[i * 2][2 * j + 2] = '*';
                    Cell right = cellMap.get(i).get((j + 1) % cols);
                    if (!curr.getNeighbors().contains(right)) {
                        board[2 * i + 1][2 * j + 2] = '|';
                    } else {
                        board[2 * i + 1][2 * j + 2] = ' ';
                    }
                }
            }
        }
        board[2 * rows][2 * cols] = '*';
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                maze.append(board[i][j]);
            }
            maze.append('\n');
        }
        return maze.toString();
    }

    @Override
    public boolean isGameOver() {
        if (playerMode == PlayerMode.SINGLE_PLAYER) {
            return !player1.getStatus().equals(PlayerStatus.LIVE);
        } else {
            return (!player1.getStatus().equals(PlayerStatus.LIVE)) && (!player2.getStatus().equals(PlayerStatus.LIVE));
        }
    }

}
