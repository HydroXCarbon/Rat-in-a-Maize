package Project1_135.util;

import Project1_135.model.Cell;

import java.util.ArrayList;

public class Command {
    private final ArrayList<Cell> maze;

    private int[] mazeSize = new int[2];

    public Command(ArrayList<Cell> maze) {
        this.maze = maze;
    }

    public void setSize(int[] size){
        this.mazeSize = size;
    }

    public static void moveUp() {
        System.out.println("Moving up");
    }

    public static void moveDown() {
        System.out.println("Moving down");
    }

    public static void moveLeft() {
        System.out.println("Moving left");
    }

    public static void moveRight() {
        System.out.println("Moving right");
    }

    public static void autoMode() {
        System.out.println("Moving automatically");
    }
}
