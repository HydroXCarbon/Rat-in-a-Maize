package Project1_135;

import Project1_135.model.Cell;
import Project1_135.util.Command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final static Scanner inputScanner = new Scanner(System.in);
    private final static ArrayList<ArrayList<Cell>> maze = new ArrayList<>();
    private final static Command command = new Command(maze);
    private static int[] mazeSize = new int[2];

    public static void main(String[] args) {
        String path = "src/main/java/Project1_135/maze/";
        boolean fileFound = false;

        do {
            try {
                // Receive file name from user
                String filename = receiveInput("New file name = ");

                // Read file and store data
                Scanner fileScanner = new Scanner(new File(path + filename));
                mazeSize = storeData(fileScanner, maze);
                command.setSize(mazeSize);

                // Close file and input scanner
                fileFound = true;
                fileScanner.close();
                break;
            } catch (Exception e) {
            }
        } while (!fileFound);
        showData();

        // Wait for user input
        while (true) {
            String input = "";
            try {
                input = receiveInput("Enter move (U = up, D = down, L = left, R = right, A = auto)");
            } catch (Exception e) {
                System.out.println("Invalid input");
            }

            // direction: 0 = top, 1 = right, 2 = bottom, 3 = left
            switch (input) {
                case "u":
                    Command.tryMove(0);
                    break;
                case "d":
                    Command.tryMove(2);
                    break;
                case "l":
                    Command.tryMove(3);
                    break;
                case "r":
                    Command.tryMove(1);
                    break;
                case "a":
                    Command.autoMode();
                    break;
            }
            showData();
        }
        //inputScanner.close();
    }

    public static String receiveInput(String message) {
        System.out.println(message);
        String input = "";
        try {
            System.out.flush();
            input = inputScanner.nextLine();
            input = input.toLowerCase();
        } catch (Exception e) {
        }
        return input;
    }

    public static int[] storeData(Scanner fileScanner, ArrayList<ArrayList<Cell>> maze) throws Exception {
        int mazeRow = 0;
        int mazeCol = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] col = line.split(",");
            mazeCol = col.length;
            int hEdge;

            // Check row edge
            // edges 0 = top, 1 = right, 2 = bottom, 3 = left
            if (mazeRow == 0) {
                hEdge = 0;
            } else if (!fileScanner.hasNextLine()) {
                hEdge = 2;
            } else {
                hEdge = 3;
            }

            ArrayList<Cell> row = new ArrayList<>();

            // Loop through each data
            for (int i = 0; i < mazeCol; i++) {
                int status;
                List<Integer> edges = new ArrayList<Integer>();

                // Check row edge and add to the list
                // edges: 0 = top, 1 = right, 2 = bottom, 3 = left
                // edge == 3 means it's in middle of the maze (no horizontal edge)
                if (!(hEdge == 3)) {
                    edges.add(hEdge);
                }

                // Check col edge and add vertical edge to the list
                if (i == 0) {
                    edges.add(3);
                } else if (i == mazeCol - 1) {
                    edges.add(1);
                }

                // Check status
                // status: 0 = wall, 1 = ground,  2 = ground with rat, 3 = ground with Food
                switch (col[i].trim()) {
                    case "0":
                        status = 0;
                        break;
                    case "1":
                        status = 1;
                        break;
                    case "R":
                        status = 2;
                        break;
                    case "F":
                        status = 3;
                        break;
                    default:
                        throw new Exception("Invalid character in file");
                }

                // Store data
                row.add(new Cell(status, edges));
            }
            maze.add(row);
            mazeRow++;
        }

        // Check maze size
        if(mazeRow < 3 || mazeCol < 3){
            throw new Exception("Invalid maze size");
        }

        return new int[]{mazeRow, mazeCol};
    }

    public static void showData() {
        int row = mazeSize[0];
        int col = mazeSize[1];

        // Print column head
        System.out.printf("%12s", " ");
        for (int i = 0; i < col; i++) {
            String colHead = "col_" + (i + 1);
            System.out.printf("%-12s", colHead);
        }
        System.out.println();

        // Print row head and maze data
        for (int i = 0; i < row; i++) {
            for( int j = 0; j < col; j++) {
                if (j == 0) {
                    String rowHead = "row_" + (i + 1);
                    System.out.printf("%-14s", rowHead);
                }
                String status;
                if (maze.get(i).get(j).getStatus() == 2) {
                    status = "R";
                } else if (maze.get(i).get(j).getStatus() == 3) {
                    status = "F";
                } else {
                    status = String.valueOf(maze.get(i).get(j).getStatus());
                }
                System.out.printf("%-12s ", status);
                if (j  == col - 1) {
                    System.out.println();
                }
            }
        }
    }
}