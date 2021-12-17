package htw.view;

import htw.controller.Controller;
import htw.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

class GamePanel extends JPanel {

    private final View view;
    private BufferedImage player2Image;
    //    private BufferedImage targetImage;
    private BufferedImage pitNearByImage;
    private BufferedImage wumpusNearByImage;
    private BufferedImage pitImage;
    private BufferedImage batImage;
    private BufferedImage wumpusImage;
    private BufferedImage caveImage1up;
    private BufferedImage caveImage1down;
    private BufferedImage caveImage1left;
    private BufferedImage caveImage1right;
    private BufferedImage caveImage3NoUp;
    private BufferedImage caveImage3NoDown;
    private BufferedImage caveImage3NoLeft;
    private BufferedImage caveImage3NoRight;
    private BufferedImage tunnelImageLeftUp;
    private BufferedImage tunnelImageLeftDown;
    private BufferedImage tunnelImageRightUp;
    private BufferedImage tunnelImageRightDown;
    private BufferedImage tunnelImageStraightHor;
    private BufferedImage tunnelImageStraightVer;
    private HashMap<Integer, BufferedImage> caveImageMaps = new HashMap<>();
    private BufferedImage playerImage;
    private BufferedImage caveImage0;
    private BufferedImage caveImage4;

    public GamePanel(View view) {
        this.view = view;
        try {
            playerImage = ImageIO.read(new File("res/images/player.png"));
            player2Image = ImageIO.read(new File("res/images/player-2.png"));

            pitImage = ImageIO.read(new File("res/images/slime-pit.png"));
            batImage = ImageIO.read(new File("res/images/superbat.png"));
            wumpusImage = ImageIO.read(new File("res/images/wumpus.png"));
            pitNearByImage = ImageIO.read(new File("res/images/slime-pit-nearby.png"));
            wumpusNearByImage = ImageIO.read(new File("res/images/wumpus-nearby.png"));
//            targetImage = ImageIO.read(new File("res/images/target.png"));

            caveImage0 = ImageIO.read(new File("res/images/roombase-0.png"));

            caveImage1up = ImageIO.read(new File("res/images/roombase-1-up.png"));
            caveImage1down = ImageIO.read(new File("res/images/roombase-1-down.png"));
            caveImage1left = ImageIO.read(new File("res/images/roombase-1-left.png"));
            caveImage1right = ImageIO.read(new File("res/images/roombase-1.png"));

            caveImage3NoUp = ImageIO.read(new File("res/images/roombase-3.png"));
            caveImage3NoDown = ImageIO.read(new File("res/images/roombase-3-no-down.png"));
            caveImage3NoLeft = ImageIO.read(new File("res/images/roombase-3-no-left.png"));
            caveImage3NoRight = ImageIO.read(new File("res/images/roombase-3-no-right.png"));

            caveImage4 = ImageIO.read(new File("res/images/roombase-4.png"));

            tunnelImageLeftUp = ImageIO.read(new File("res/images/hallway-left-up.png"));
            tunnelImageLeftDown = ImageIO.read(new File("res/images/hallway-left-down.png"));
            tunnelImageRightUp = ImageIO.read(new File("res/images/hallway-right-up.png"));
            tunnelImageRightDown = ImageIO.read(new File("res/images/hallway.png"));
            tunnelImageStraightHor = ImageIO.read(new File("res/images/hallway-straight.png"));
            tunnelImageStraightVer = ImageIO.read(new File("res/images/hallway-straight-vertical.png"));

            caveImageMaps.put(0, caveImage0);
            caveImageMaps.put(1, caveImage1right);
            caveImageMaps.put(10, caveImage1left);
            caveImageMaps.put(100, caveImage1down);
            caveImageMaps.put(1000, caveImage1up);
            caveImageMaps.put(11, tunnelImageStraightHor);
            caveImageMaps.put(101, tunnelImageRightDown);
            caveImageMaps.put(1001, tunnelImageRightUp);
            caveImageMaps.put(110, tunnelImageLeftDown);
            caveImageMaps.put(1010, tunnelImageLeftUp);
            caveImageMaps.put(1100, tunnelImageStraightVer);
            caveImageMaps.put(1110, caveImage3NoRight);
            caveImageMaps.put(1101, caveImage3NoLeft);
            caveImageMaps.put(1011, caveImage3NoDown);
            caveImageMaps.put(111, caveImage3NoUp);
            caveImageMaps.put(1111, caveImage4);

        } catch (IOException e) {
            System.out.println("file doesn't exist" + e);
        }
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(Parameters.HEIGHT, Parameters.WIDTH));
        this.addKeyListener(new TAdapter());
        this.addMouseListener(new mouseListener());
        this.setVisible(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Controller c = view.getController();
        if (c == null) {
            return;
        }

        List<Cell> exploredCells = c.getExploredCells();
        for (int i = 0; i < exploredCells.size(); i++) {
            drawCell(g, c, exploredCells.get(i));
        }
        Cell playerCell = c.getPlayerCell();
        g.drawImage(playerImage, playerCell.getY() * Parameters.CELL_HEIGHT + 24, playerCell.getX() * Parameters.CELL_WIDTH + 16, this);

        if (c.isTwoPlayerMode()) {
            Cell player2Cell = c.getPlayer2Cell();
            g.drawImage(player2Image, player2Cell.getY() * Parameters.CELL_HEIGHT + 24, player2Cell.getX() * Parameters.CELL_WIDTH + 16, this);
        }
    }

    private void drawCell(Graphics g, Controller c, Cell cell) {

        BufferedImage cellImage;
        List<Cell> neighbors = cell.getNeighbors();
        if (neighbors == null || neighbors.size() == 0) {
            cellImage = caveImage0;
        } else if (neighbors.size() == 4) {
            cellImage = caveImage4;
        } else {
            int flag = 0;
            int up = 1000;
            int down = 100;
            int left = 10;
            int right = 1;
            for (Cell neighbor : neighbors) {
                if (neighbor.getX() == cell.getX()) {
                    if (neighbor.getY() == ((cell.getY() - 1 + c.getMazeRows()) % c.getMazeRows())) {
                        flag += left;
                    } else if (neighbor.getY() == ((cell.getY() + 1 + c.getMazeRows()) % c.getMazeRows())) {
                        flag += right;
                    }
                } else if (neighbor.getY() == cell.getY()) {
                    if (neighbor.getX() == ((cell.getX() - 1 + c.getMazeCols()) % c.getMazeCols())) {
                        flag += up;
                    } else if (neighbor.getX() == ((cell.getX() + 1 + c.getMazeCols()) % c.getMazeCols())) {
                        flag += down;
                    }
                }
            }
            cellImage = caveImageMaps.get(flag);
        }

        g.drawImage(cellImage, cell.getY() * Parameters.CELL_HEIGHT, cell.getX() * Parameters.CELL_WIDTH, this);
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case PIT:
                g.drawImage(pitImage, cell.getY() * Parameters.CELL_HEIGHT, cell.getX() * Parameters.CELL_WIDTH, this);
                break;
            case BAT:
                g.drawImage(batImage, cell.getY() * Parameters.CELL_HEIGHT + 17, cell.getX() * Parameters.CELL_WIDTH + 24, this);
        }
        if (cell == c.getWumpusCell()) {
            g.drawImage(wumpusImage, cell.getY() * Parameters.CELL_HEIGHT + 16, cell.getX() * Parameters.CELL_WIDTH + 16, this);
        }

        if (cell.getCellKind() == CellKind.CAVE && c.isPitNearBy(cell)) {
            g.drawImage(pitNearByImage, cell.getY() * Parameters.CELL_HEIGHT, cell.getX() * Parameters.CELL_WIDTH, this);
        }

        if (cell.getCellKind() == CellKind.CAVE && c.isWumpusNearBy(cell)) {
            g.drawImage(wumpusNearByImage, cell.getY() * Parameters.CELL_HEIGHT, cell.getX() * Parameters.CELL_WIDTH, this);
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent code) {
            Controller c = view.getController();
            if (c == null) {
                return;
            }
            try {
                if (c.isShooting()) {
                    switch (code.getKeyCode()) {
                        case KeyEvent.VK_ESCAPE:
                            view.displayStatusMessage("cancel shoot");
                            c.stopShoot();
                            break;
                        default:
                            view.showMessageDialog("Player is shooting! \n press ESC to cancel,");
                    }
                } else {
                    switch (code.getKeyCode()) {
                        case KeyEvent.VK_ESCAPE:
                            view.restoreMenu();
                            return;
                        case KeyEvent.VK_UP:
                            c.move(Direction.NORTH);
                            break;
                        case KeyEvent.VK_DOWN:
                            c.move(Direction.SOUTH);
                            break;
                        case KeyEvent.VK_LEFT:
                            c.move(Direction.WEST);
                            break;
                        case KeyEvent.VK_RIGHT:
                            c.move(Direction.EAST);
                            break;
                        case KeyEvent.VK_S:
                            c.startShoot();
                            break;
                        default:
                            System.out.println("Invalid Key! " + code);
                    }
                }
                repaint();
                c.checkStatus();
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid move!" + e.getStackTrace());
            }
        }
    }

    private class mouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            Controller c = view.getController();
            if (c == null) {
                return;
            }

            int x = e.getY() / Parameters.CELL_WIDTH;
            int y = e.getX() / Parameters.CELL_HEIGHT;
            c.clickMove(x, y);
            repaint();
            c.checkStatus();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
