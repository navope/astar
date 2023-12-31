package astar;

import java.awt.*;
import java.util.*;
import java.util.List;

import astar.GenericSearch.Node;

import javax.swing.*;

public class Maze {

    public enum Cell {
        EMPTY("_"),
        BLOCKED("x"),
        START("S"),
        GOAL("G"),
        PATH("*");

        private final String code;

        private Cell(String c) {
            code = c;
        }

        @Override
        public String toString() {
            return code;
        }
    }

    public static class MazeLocation {
        public final int row;
        public final int column;

        public MazeLocation(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + column;
            result = prime * result + row;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            MazeLocation other = (MazeLocation) obj;
            if (column != other.column) {
                return false;
            }
            if (row != other.row) {
                return false;
            }
            return true;
        }
    }

    private final int rows, columns;
    private final MazeLocation start, goal;
    private Cell[][] grid;

    public Maze(int rows, int columns, MazeLocation start, MazeLocation goal, double sparseness) {
        this.rows = rows;
        this.columns = columns;
        this.start = start;
        this.goal = goal;
        grid = new Cell[rows][columns];
        for (Cell[] row : grid) {
            Arrays.fill(row, Cell.EMPTY);
        }
        //fill the grid with blocked cells
        randomlyFill(sparseness);
        // fill the start and goal locations
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public Maze() {
        this(16, 16, new MazeLocation(0, 0), new MazeLocation(15, 15), 0.0);
    }

    private void randomlyFill(double sparseness) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (Math.random() < sparseness) {
                    grid[row][column] = Cell.BLOCKED;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                sb.append(cell);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    public boolean goalTest(MazeLocation ml) {
        return goal.equals(ml);
    }

    public List<MazeLocation> successors(MazeLocation ml) {
        List<MazeLocation> locations = new ArrayList<>();
        if (ml.row + 1 < rows && grid[ml.row + 1][ml.column] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row + 1, ml.column));
        }
        if (ml.row - 1 >= 0 && grid[ml.row - 1][ml.column] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row - 1, ml.column));
        }
        if (ml.column + 1 < columns && grid[ml.row][ml.column + 1] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row, ml.column + 1));
        }
        if (ml.column - 1 >= 0 && grid[ml.row][ml.column - 1] != Cell.BLOCKED) {
            locations.add(new MazeLocation(ml.row, ml.column - 1));
        }
        return locations;
    }

    public void mark(List<MazeLocation> path) {
        for (MazeLocation ml : path) {
            grid[ml.row][ml.column] = Cell.PATH;
        }
        grid[start.row][start.column] = Cell.START;
        grid[goal.row][goal.column] = Cell.GOAL;
    }

    public double manhattanDistance(MazeLocation ml) {
        int xdist = Math.abs(ml.column - goal.column);
        int ydist = Math.abs(ml.row - goal.row);
        return (xdist + ydist);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game Grid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Maze m = new Maze();
        Menu menu = new Menu();
        Maze m = new Maze(menu.getNumberOfVertices(), menu.getNumberOfEdges(), new MazeLocation(menu.getStart().x, menu.getStart().y),
                new MazeLocation(menu.getGoal().x, menu.getGoal().y), 0.2);

        Double[][] weightValues = new Double[m.rows][m.columns];
        Double[][] weightNode = new Double[m.rows][m.columns];
        Double[][] heuristicValues = new Double[m.rows][m.columns];

        Node<MazeLocation> solution3 = GenericSearch.astar(m.start, m::goalTest, m::successors, m::manhattanDistance, weightValues, weightNode, heuristicValues);
        if (solution3 == null) {
            System.out.println("No solution found using A*!");
        } else {
            List<MazeLocation> path3 = GenericSearch.nodeToPath(solution3);
            m.mark(path3);
            frame.getContentPane().add(new GameGrid(m.grid, weightValues, weightNode, heuristicValues, m.columns));
            frame.setMinimumSize(new Dimension(1000, 1038)); // setting the minimum window size
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}