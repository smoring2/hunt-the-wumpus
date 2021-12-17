package htw.view;

import htw.PlayerMode;
import htw.controller.Controller;
import htw.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HTWTextView implements View {

    private Controller controller;
    private Scanner scanner;

    public HTWTextView() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public void showMessageDialog(String msg) {
        System.out.println("msg: " + msg);
    }

    @Override
    public void restoreMenu() {
        return;
    }

    @Override
    public void showGameArea() {
        return;
    }

    @Override
    public void setController(Controller c) {
        this.controller = c;
    }

    @Override
    public void startShoot() {
        System.out.println("\nToward cave? Plz input the direction");
    }

    @Override
    public void stopShoot() {
        return;
    }

    @Override
    public MazeInterface createMaze(String[] args) {
        MazeInterface maze = null;
        try {
            maze = initText(args);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Invalid maze!");
        }
        return maze;
    }

    public MazeInterface initText(String[] args) throws FileNotFoundException {
        if (args.length != 0 && (args[0].equals("transported") || args[0].equals("1"))) {
            scanner = new Scanner(new File("res/transported.txt"));
        } else if (args.length != 0 && (args[0].equals("falling") || args[0].equals("2"))) {
            scanner = new Scanner(new File("res/falling.txt"));
        } else if (args.length != 0 && (args[0].equals("eaten") || args[0].equals("3"))) {
            scanner = new Scanner(new File("res/eaten.txt"));
        } else if (args.length != 0 && (args[0].equals("win") || args[0].equals("4"))) {
            scanner = new Scanner(new File("res/win.txt"));
        }

        System.out.println("Enter the type of maze - \n 1 - Perfect Maze \n 2 - Room Maze");
        System.out.println(" 3  - Wrapping Room Maze");
        int type = scanner.nextInt();
        System.out.println(type);
        System.out.println("Enter whether the maze is wrapped - \n 1 - Yes \n 2 - No ");
        boolean isWrapped = (scanner.nextInt() == 1 ? true : false);
        System.out.println(isWrapped);
        System.out.println("Enter the number of rows: ");
        int rows = scanner.nextInt();
        System.out.println(rows);
        System.out.println("Enter the number of columns: ");
        int cols = scanner.nextInt();
        System.out.println(cols);
        int remainingWalls = 0;
        if (type == 2 || type == 3) {
            System.out.println("Enter the remaining walls for maze: ");
            remainingWalls = scanner.nextInt();
            System.out.println(remainingWalls);
        }
        System.out.println("Enter the amount of superbats cells: ");
        int batCells = scanner.nextInt();
        System.out.println(batCells);
        System.out.println("Enter the amount of bottomless pits cells: ");
        int pitCells = scanner.nextInt();
        System.out.println(pitCells);
        MazeInterface maze;
        switch (type) {
            case 1:
                try {
                    maze = new PerfectMaze(rows, cols, isWrapped, batCells, pitCells, 10, PlayerMode.SINGLE_PLAYER);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid maze.");
                    return null;
                }
                break;
            case 2:
                try {
                    maze = new RoomMaze(rows, cols, remainingWalls, isWrapped, batCells, pitCells, 10, PlayerMode.SINGLE_PLAYER);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid maze.");
                    return null;
                }
                break;

            case 3:
                try {
                    maze = new WrappingRoomMaze(rows, cols, remainingWalls, batCells, pitCells, 10, PlayerMode.SINGLE_PLAYER);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid maze.");
                    return null;
                }
                break;
            default:
                System.out.println("Invalid maze.");
                return null;
        }

        PlayerInterface player = new Player();
        System.out.println("Enter the amount of arrows the player have at the start: ");
        int arrows = scanner.nextInt();
        System.out.println(arrows);
        try {
            maze.setPlayer(player, 0, 0, arrows);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments.");
            return null;
        }

        try {
            maze.setWumpusCell(0, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid arguments.");
            return null;
        }
        return maze;
    }


    @Override
    public void setRandomSeed(int randomSeed) {
        return;
    }


    @Override
    public void displayStatusMessage(String msg) {
        System.out.println("status msg: " + msg);
    }


    @Override
    public void startGame() {

        if (controller == null) {
            return;
        }
        controller.initMaze();
        MazeInterface maze = controller.getMaze();
        //revoke the controller
        System.out.println(maze.printMaze());
        PlayerInterface player = maze.getPlayer();
        System.out.println(maze.setPlayerPos(maze.getPlayer().getCurrPosition()));
        while (player.getStatus().equals(PlayerStatus.LIVE)) {
            System.out.println("You are in cave " + maze.getPlayer().getPosIndex());
            System.out.println("Tunnels lead to the ");
            for (Direction direction : maze.getPossibleMoves()) {
                System.out.println(direction + " ");
            }
            System.out.println("Shoot or Move (S-M)?");
            String operation = scanner.next();
            System.out.println(operation);
            switch (operation) {
                case "S":
                case "SHOOT":
                    System.out.println("\nNo. of caves? Plz input an integer");
                    int dist = scanner.nextInt();
                    System.out.println(dist);
                    System.out.println("\nToward cave? Plz input the direction");
                    String direction = scanner.next();
                    System.out.println(direction);
                    switch (direction) {
                        case "N":
                        case "NORTH":
                            System.out.println(controller.shoot(Direction.NORTH, dist));
                            break;
                        case "S":
                        case "SOUTH":
                            System.out.println(controller.shoot(Direction.SOUTH, dist));
                            break;
                        case "E":
                        case "EAST":
                            System.out.println(controller.shoot(Direction.EAST, dist));
                            break;
                        case "W":
                        case "WEST":
                            System.out.println(controller.shoot(Direction.WEST, dist));
                            break;
                        default:
                            System.out.println("Invalid Shooting direction. Try Again!");
                    }
                    break;
                case "M":
                case "MOVE":
                    System.out.println("Where to? Plz input the direction");
                    direction = scanner.next();
                    System.out.println(direction);
                    switch (direction) {
                        case "N":
                        case "NORTH":
                            System.out.println(controller.move(Direction.NORTH));
                            break;
                        case "S":
                        case "SOUTH":
                            System.out.println(controller.move(Direction.SOUTH));
                            break;
                        case "E":
                        case "EAST":
                            System.out.println(controller.move(Direction.EAST));
                            break;
                        case "W":
                        case "WEST":
                            System.out.println(controller.move(Direction.WEST));
                            break;
                        default:
                            System.out.println("Invalid move. Try Again!");
                    }
                    break;
                default:
                    System.out.println("Invalid input.  Try Again!");
            }
        }

    }

    @Override
    public void showPlayerTurn(int playerTurn) {
        System.out.println("Player " + playerTurn + "'s turn.");
    }
}
