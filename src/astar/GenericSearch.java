package astar;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

import astar.Maze.MazeLocation;

public class GenericSearch {
    public static class Node<T> implements Comparable<Node<T>> {
        final T state;
        Node<T> parent;
        double cost;
        double heuristic;

        Node(T state, Node<T> parent, double cost, double heuristic) {
            this.state = state;
            this.parent = parent;
            this.cost = cost;
            this.heuristic = heuristic;
        }

        @Override
        public int compareTo(Node<T> other) {
            Double mine = cost + heuristic;
            Double theirs = other.cost + other.heuristic;
            return mine.compareTo(theirs);
        }
    }

    public static <T> List<T> nodeToPath(Node<T> node) {
        List<T> path = new ArrayList<>();
        path.add(node.state);
        while (node.parent != null) {
            node = node.parent;
            path.add(0, node.state);
        }
        return path;
    }

    public static int generateRandomNumber() {
        Random rand = new Random();
        return rand.nextInt(2) + 1;
    }

    public static <T> Node<MazeLocation> astar(MazeLocation initial, Predicate<MazeLocation> goalTest,
                                               Function<MazeLocation, List<MazeLocation>> successors,
                                               ToDoubleFunction<MazeLocation> heuristic,
                                               Double[][] weightValues, Double[][] weightNode,
                                               Double[][] heuristicValues) {
        PriorityQueue<Node<MazeLocation>> frontier = new PriorityQueue<>();
        frontier.offer(new Node<>(initial, null, 0.0, heuristic.applyAsDouble(initial)));
        Map<MazeLocation, Double> explored = new HashMap<>();

        while (!frontier.isEmpty()) {
            Node<MazeLocation> currentNode = frontier.poll();
            MazeLocation currentState = currentNode.state;

            if (goalTest.test(currentState)) {
                if (Objects.equals(initial, currentState)){
                    System.out.println("Start = Goal");
                    break;
                }
                int i = 0;

                System.out.println("Открытые вершины");
                while (!frontier.isEmpty()) {
                    i++;
                    Node<MazeLocation> node = frontier.poll();
                    if (i % 20 == 0) {
                        System.out.println();
                    }
                    System.out.printf("{%d,%d} ", node.state.row, node.state.column);
                }
                System.out.println();
                System.out.println();
                i = 0;
                System.out.println("Закрытые вершины");
                Set<MazeLocation> nodePrinted = new HashSet<>();
                for (Map.Entry<MazeLocation, Double> entry : explored.entrySet()) {
                    MazeLocation location = entry.getKey();
                    if (!nodePrinted.contains(location)) {
                        nodePrinted.add(location);
                        i++;
                        if (i % 20 == 0) {
                            System.out.println();
                        }
                        System.out.printf("{%d,%d} ", location.row, location.column);
                    }
                }
                return currentNode;
            }

            if (explored.containsKey(currentState)) {
                continue;
            }

            explored.put(currentState, currentNode.cost);

            for (MazeLocation child : successors.apply(currentState)) {
                double g = currentNode.cost + (double) generateRandomNumber(); // distance from the start node to the current node
                //double g = currentNode.cost + (double)1; // distance from the start node to the current node
                double h = heuristic.applyAsDouble(child); // heuristic estimation of the distance from the current node to the target node
                double f = g + h; // estimated total cost of the path from the start node to the destination node through the current node

                if (!explored.containsKey(child) || explored.get(child) > f) {
                    frontier.offer(new Node<>(child, currentNode, g, h));
                    // Updating weightValues, weightNode, and heuristicValues
                    weightValues[child.row][child.column] = g;
                    weightNode[child.row][child.column] = g - currentNode.cost;
                    heuristicValues[child.row][child.column] = h;
                }
            }
        }
        return null; // there is no solution
    }
}