package astar;

import java.awt.*;
import java.util.Scanner;

public class Menu {

    private int numberOfVertices;
    private int numberOfEdges;
    private Point start;
    private Point goal;

    public Menu() {
        askUser();
    }

    public int getNumberOfVertices() {
        return numberOfVertices;
    }

    public int getNumberOfEdges() {
        return numberOfEdges;
    }

    public Point getStart() {
        return start;
    }

    public Point getGoal() {
        return goal;
    }

    private void askUser(){
        Scanner scanner = new Scanner(System.in);
        start = new Point();
        goal = new Point();
        int x;
        int y;
        System.out.println("Enter number of vertices(max 16)");
        numberOfVertices = scanner.nextInt();

        numberOfEdges = numberOfVertices;

        System.out.println("Enter position in interval ["+0+":"+(numberOfVertices-1)+"]");

        System.out.println("Enter start position x");
        x = scanner.nextInt();
        System.out.println("Enter start position y:");
        y = scanner.nextInt();

        start.setLocation(x,y);

        System.out.println("Enter goal position row:");
        x = scanner.nextInt();
        System.out.println("Enter goal position column:");
        y = scanner.nextInt();

        goal.setLocation(x,y);
    }
}
