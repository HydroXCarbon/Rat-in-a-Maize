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
        boolean movable = checkMovable(ratIndex, nextCellIndex, direction);
        if(movable){
            moveAndCheckFood(ratIndex, nextCellIndex);
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
    private static void moveAndCheckFood(int[] ratIndex, int[] nextCellIndex){
        // status: 0 = wall, 1 = ground,  2 = ground with rat, 3 = ground with Food

        // Check if the rat found food
        if(maze.get(nextCellIndex[0]).get(nextCellIndex[1]).getStatus() == 3){
            System.out.println("+++++ Found food +++++");
        }
        // Move
        maze.get(ratIndex[0]).get(ratIndex[1]).setStatus(1);
        maze.get(nextCellIndex[0]).get(nextCellIndex[1]).setStatus(2);
    }

    // direction: 0 = top, 1 = right, 2 = bottom, 3 = left
    private static boolean checkMovable(int[] ratIndex, int[] nextCellIndex, int direction){

        // Check if the rat can move to the direction (In current cell)
        if(nextCellIndex[0] < 0 || nextCellIndex[0] >= mazeSize[0] || nextCellIndex[1] < 0 || nextCellIndex[1] >= mazeSize[1]){
            System.out.println("Cannot Move (out of maze)");
            return false;
        }

        // Check if the rat can move to the direction (In direction cell)
        int nextCellStatus = maze.get(nextCellIndex[0]).get(nextCellIndex[1]).getStatus();

        if(nextCellStatus == 0){
            System.out.println("Cannot Move (wall)");
            return false;
        }
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
