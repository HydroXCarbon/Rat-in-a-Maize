package Project1_135.util;

import Project1_135.model.Cell;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private static ArrayList<ArrayList<Cell>> maze;

    // index 0 = row, index 1 = column
    private static int[] mazeSize = new int[2];

    public Command(ArrayList<ArrayList<Cell>> maze) {
        this.maze = maze;
    }

    public void setSize(int[] size){
        this.mazeSize = size;
    }

    // direction: 0 = top, 1 = right, 2 = bottom, 3 = left
    public static void tryMove(int direction) {
        int[] ratIndex = findRatIndex();
        int[] nextCellIndex = calculateNextCellIndex(ratIndex, direction);
        System.out.printf("Rat found at %d %d\n", ratIndex[0], ratIndex[1]);
        System.out.printf("move to cell %d %d\n", nextCellIndex[0], nextCellIndex[1]);
        boolean movable = checkMovable(ratIndex, nextCellIndex, direction);
        if(movable){
            move(ratIndex, nextCellIndex);
        }
    }

    public static void autoMode() {
        System.out.println("Moving automatically");
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
    private static void move(int[] ratIndex, int[] nextCellIndex){
        // status: 0 = wall, 1 = ground,  2 = ground with rat, 3 = ground with Food
        maze.get(ratIndex[0]).get(ratIndex[1]).setStatus(1);
        maze.get(nextCellIndex[0]).get(nextCellIndex[1]).setStatus(2);
    }

    // direction: 0 = top, 1 = right, 2 = bottom, 3 = left
    private static boolean checkMovable(int[] ratIndex, int[] nextCellIndex, int direction){

        // Check if the rat can move to the direction (In current cell)
        List<Integer> ratCellEdges = maze.get(ratIndex[0]).get(ratIndex[1]).getEdges();
        System.out.println(ratCellEdges);
        if(ratCellEdges.contains(direction)){
            System.out.println("Can't Move (edge)");
            return false;
        }

        // Check if the rat can move to the direction (In direction cell)
        int nextCellStatus = maze.get(nextCellIndex[0]).get(nextCellIndex[1]).getStatus();

        if(nextCellStatus == 0){
            System.out.println("Can't Move (wall)");
            return false;
        }
        System.out.println("Can Move");
        return true;
    }

    private static int[] findRatIndex(){
        for (int i = 0; i < mazeSize[0]; i++){
            for(int j = 0; j < mazeSize[1]; j++){
                if (maze.get(i).get(j).getStatus() == 2){
                    return new int[]{i,j};
                }
            }
        }
        throw new RuntimeException("Rat not found");
    }
}
