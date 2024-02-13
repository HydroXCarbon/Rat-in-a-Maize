package Project1_135.util;

import Project1_135.model.Cell;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.exit;

public class Command {

    private final static Random rand = new Random();

    private final static ArrayDeque<int[]> path = new ArrayDeque<>();

    private static boolean foundFood = false;

    private static ArrayList<ArrayList<Cell>> maze;

    // index 0 = row, index 1 = column
    private static int[] mazeSize = new int[2];

    public Command(ArrayList<ArrayList<Cell>> maze) {
        Command.maze = maze;
    }

    // direction: 0 = top, 1 = right, 2 = bottom, 3 = left
    public static void tryMove(int direction) {
        int[] ratIndex = findRatIndex();
        int[] nextCellIndex = calculateNextCellIndex(ratIndex, direction);
        boolean movable = checkMovable(ratIndex, nextCellIndex, direction);
        if (movable) {
            move(ratIndex, nextCellIndex);
            if(checkFood(nextCellIndex)) {
                System.out.println("+++++ Found food +++++");
            };
        }else{
            System.out.println("Cannot Move");
        }
        showData();
    }

    public static void autoMode(int count) {
        // Using DFS to find the route
        System.out.printf("\n===== Finding Food %d =====\n",count);
        int[] ratIndex = findRatIndex();
        showData();
        dfs(ratIndex);
        if(foundFood){
            // Show path
            System.out.println("Rat path");
            for(int[] i : path){
                int row = i[0];
                int col = i[1];
                int direction = i[2];
                int intStatus = maze.get(row).get(col).getStatus();
                String strStatus = "";
                String strDirection = "";
                switch(intStatus){
                    case 0:
                        strStatus = "0";
                        break;
                    case 1:
                        strStatus = "1";
                        break;
                    case 2:
                        strStatus = "R";
                        break;
                    case 3:
                        strStatus = "F";
                        break;
                }
                switch(direction){
                    case -1:
                        strDirection = "Start";
                        break;
                    case 0:
                        strDirection = "Up";
                        break;
                    case 1:
                        strDirection = "Right";
                        break;
                    case 2:
                        strDirection = "Down";
                        break;
                    case 3:
                        strDirection = "Left";
                        break;
                }
                System.out.printf("%-7s : -> (row %d, col %d, %s)\n",strDirection,row ,col ,strStatus);
                move(ratIndex, path.getLast());
            }
            resetData();
            autoMode(count+1);
        }else{
            System.out.println("No solution !!");
            resetData();
            exit(0);
        }
    }

    private static void resetData(){
        // Clear visited status
        for(ArrayList<Cell> r : maze){
            for(Cell c : r){
                c.setVisited(false);
            }
        }

        // Clear foundFood
        foundFood = false;

        // Clear path
        path.clear();
    }

    public static boolean dfs(int[] currentIndex){
        // If food has already been found, stop the recursion
        if (foundFood) {
            return true;
        }

        path.add(currentIndex);
        ArrayDeque<int[]> stack = new ArrayDeque<>();

        // If rat found food return
        if(checkFood(currentIndex)){
            foundFood = true;
            return true;
        }

        // direction: 0 = top, 1 = right, 2 = bottom, 3 = left
        // Start searching each direction
        ArrayList<int[]> tempStack = new ArrayList<>();
        maze.get(currentIndex[0]).get(currentIndex[1]).setVisited(true);
        for (int i = 0; i < 4; i++) {
            int[] nextCellIndex = calculateNextCellIndex(currentIndex, i);
            boolean movable = checkMovable(currentIndex, nextCellIndex, i);
            if (movable) {
                if(!maze.get(nextCellIndex[0]).get(nextCellIndex[1]).getVisited()){
                    nextCellIndex[2] = i;
                    tempStack.add(nextCellIndex);
                }
            }
        }

        // If rat can't move return (backtracking)
        if(tempStack.isEmpty()){
            if (!path.isEmpty()) {
                path.removeLast();
            }
            return false;
        }

        // Random add direction to stack
        while(!tempStack.isEmpty()){
            int randomIndex = rand.nextInt(tempStack.size());
            int[] removedElement = tempStack.remove(randomIndex);
            stack.add(removedElement);
        }

        // Move to next cell
        while(!stack.isEmpty()){
            int[] next = stack.remove();
            if(dfs(next)){
                return true;
            }
        }
        return false;
    }

    public static int[] calculateNextCellIndex(int[] ratIndex, int direction) {
        int[] nextCellIndex = ratIndex.clone();
        switch (direction) {
            case 0:
                nextCellIndex[0]--;
                break;
            case 1:
                nextCellIndex[1]++;
                break;
            case 2:
                nextCellIndex[0]++;
                break;
            case 3:
                nextCellIndex[1]--;
                break;
        }
        return nextCellIndex;
    }

    private static boolean checkFood(int[] currentIndex){
        if (maze.get(currentIndex[0]).get(currentIndex[1]).getStatus() == 3) {
            return true;
        }
        return false;
    }

    private static void move(int[] ratIndex, int[] nextCellIndex) {
        // status: 0 = wall, 1 = ground,  2 = ground with rat, 3 = ground with Food
        // Move
        maze.get(ratIndex[0]).get(ratIndex[1]).setStatus(1);
        maze.get(nextCellIndex[0]).get(nextCellIndex[1]).setStatus(2);
    }

    // direction: 0 = top, 1 = right, 2 = bottom, 3 = left
    private static boolean checkMovable(int[] ratIndex, int[] nextCellIndex, int direction) {

        // Check if the rat can move to the direction (In current cell)
        if (nextCellIndex[0] < 0 || nextCellIndex[0] >= mazeSize[0] || nextCellIndex[1] < 0 || nextCellIndex[1] >= mazeSize[1]) {
            return false;
        }

        // Check if the rat can move to the direction (In direction cell)
        int nextCellStatus = maze.get(nextCellIndex[0]).get(nextCellIndex[1]).getStatus();

        if (nextCellStatus == 0) {
            return false;
        }
        return true;
    }

    private static int[] findRatIndex() {
        for (int i = 0; i < mazeSize[0]; i++) {
            for (int j = 0; j < mazeSize[1]; j++) {
                if (maze.get(i).get(j).getStatus() == 2) {
                    return new int[]{i, j, -1};
                }
            }
        }
        throw new RuntimeException("Rat not found");
    }

    public void setSize(int[] size) {
        mazeSize = size;
    }

    public static void showData() {
        int row = mazeSize[0];
        int col = mazeSize[1];

        // Print column head
        System.out.printf("%12s", " ");
        for (int i = 0; i < col; i++) {
            String colHead = "col_" + (i);
            System.out.printf("%-12s", colHead);
        }
        System.out.println();

        // Print row head and maze data
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (j == 0) {
                    String rowHead = "row_" + (i);
                    System.out.printf("%-14s", rowHead);
                }
                String status;
                if (maze.get(i).get(j).getStatus() == 2) {
                    status = "\033[0;31m" + "R" + "\033[0m";
                    System.out.printf("%-23s ", status);
                } else if (maze.get(i).get(j).getStatus() == 3) {
                    status = "\033[0;32m" + "F" + "\033[0m";
                    System.out.printf("%-23s ", status);
                } else {
                    status = String.valueOf(maze.get(i).get(j).getStatus());
                    System.out.printf("%-12s ", status);
                }
                if (j == col - 1) {
                    System.out.println();
                }
            }
        }
        System.out.println();
    }
}
