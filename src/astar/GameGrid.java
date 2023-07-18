package astar;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class GameGrid extends JPanel {
    private Maze.Cell[][] grid;
    Double[][] weightValues;
    Double[][] heuristicValues;
    Double[][] weightNode;
    private final int GRID_SIZE;
    private static final int CELL_SIZE = 60;

    public GameGrid(Maze.Cell[][] grid, Double[][] weightValues, Double[][] weightNode, Double[][] heuristicValues, int gridSize) {
        this.grid = grid;
        this.heuristicValues = heuristicValues;
        this.weightNode = weightNode;
        this.weightValues = weightValues;
        GRID_SIZE = gridSize;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        // Отрисовка горизонтальных линий
        for (int i = 0; i <= GRID_SIZE; i++) {
            int x1 = 0;
            int y1 = i * CELL_SIZE;
            int x2 = GRID_SIZE * CELL_SIZE;
            int y2 = i * CELL_SIZE;
            g2d.drawLine(x1, y1, x2, y2);
        }

        // Отрисовка вертикальных линий
        for (int i = 0; i <= GRID_SIZE; i++) {
            int x1 = i * CELL_SIZE;
            int y1 = 0;
            int x2 = i * CELL_SIZE;
            int y2 = GRID_SIZE * CELL_SIZE;
            g2d.drawLine(x1, y1, x2, y2);
        }
        paintGrid(g2d, grid, weightValues, weightNode, heuristicValues);
    }

    public static void printMessage(Graphics2D g2d, int col, int row, FontMetrics fm, String text, Color color, int divY) {
        g2d.setColor(color); // цвет текста
        int textWidth = fm.stringWidth(text); // ширина текста в пикселях
        int textHeight = fm.getHeight(); // высота текста в пикселях
        int x = col * CELL_SIZE + (CELL_SIZE - textWidth) / 2; // координата x для вывода текста
        int y = row * CELL_SIZE + (CELL_SIZE - textHeight) / 2 + fm.getAscent() + divY; // координата y для вывода текста
        g2d.drawString(text, x, y); // вывод текста
    }

    public static void paintGrid(Graphics2D g2d, Maze.Cell[][] grid, Double[][] weightValues, Double[][] weightNode, Double[][] heuristicValues) {
        FontMetrics fm = g2d.getFontMetrics();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                switch (grid[row][col]) {
                    case BLOCKED -> {
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(col * CELL_SIZE + 1, row * CELL_SIZE + 1, CELL_SIZE - 1, CELL_SIZE - 1);
                    }
                    case START -> {
                        g2d.setColor(Color.BLUE);
                        g2d.fillRect(col * CELL_SIZE + 1, row * CELL_SIZE + 1, CELL_SIZE - 1, CELL_SIZE - 1);
                        g2d.setColor(Color.YELLOW); // цвет текста
                        printMessage(g2d, col, row, fm, "S", Color.YELLOW, 0);
                    }
                    case GOAL -> {
                        g2d.setColor(Color.BLUE);
                        g2d.fillRect(col * CELL_SIZE + 1, row * CELL_SIZE + 1, CELL_SIZE - 1, CELL_SIZE - 1);
                        g2d.setColor(Color.YELLOW); // цвет текста
                        printMessage(g2d, col, row, fm, "G", Color.YELLOW, 0);
                    }
                    case PATH -> {
                        if (weightNode[row][col] == 1) {
                            g2d.setColor(Color.YELLOW);
                        } else {
                            g2d.setColor(Color.ORANGE);
                        }
                        g2d.fillRect(col * CELL_SIZE + 1, row * CELL_SIZE + 1, CELL_SIZE - 1, CELL_SIZE - 1);
                        paintValues(g2d, grid, weightValues, heuristicValues);
                    }
                    default -> {
                        paintValues(g2d, grid, weightValues, heuristicValues);
                    }
                }
            }
        }
    }

    public static void paintValues(Graphics2D g2d, Maze.Cell[][] grid, Double[][] weightValues, Double[][] heuristicValues) {
        FontMetrics fm = g2d.getFontMetrics();
        String text;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                switch (grid[row][col]) {
                    case BLOCKED, START, GOAL:
                        continue;
                    case PATH:
                        text = String.format("%.1f", heuristicValues[row][col]);
                        printMessage(g2d, col, row, fm, text, Color.BLACK, -12);
                        text = String.format("%.1f", weightValues[row][col]);
                        printMessage(g2d, col, row, fm, text, Color.BLACK, 0);
                        text = String.format("%.1f", weightValues[row][col] + heuristicValues[row][col]);
                        printMessage(g2d, col, row, fm, text, Color.BLACK, 12);
                        break;
                    default:
                        text = String.format("%.1f", heuristicValues[row][col]);
                        if (Objects.equals(text, "n")) {
                            break;
                        }
                        printMessage(g2d, col, row, fm, text, Color.BLACK, -12);
                        text = String.format("%.1f", weightValues[row][col]);
                        printMessage(g2d, col, row, fm, text, Color.BLACK, 0);
                        text = String.format("%.1f", weightValues[row][col] + heuristicValues[row][col]);
                        printMessage(g2d, col, row, fm, text, Color.BLACK, 12);
                        break;
                }
            }
        }
    }
}
