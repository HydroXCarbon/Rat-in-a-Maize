package Project1_135;

import Project1_135.model.Cell;
import Project1_135.util.Command;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private final static Scanner inputScanner = new Scanner(System.in);
    private final static ArrayList<Cell> maze = new ArrayList<Cell>();
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
        showData(maze, mazeSize[0], mazeSize[1]);

        // Wait for user input
        while (true) {
            String input = "";
            try {
                input = receiveInput("Enter move (U = up, D = down, L = left, R = right, A = auto)");
            } catch (Exception e) {
                System.out.println("Invalid input");
            }

            switch (input) {
                case "u":
                    Command.moveUp();
                    break;
                case "d":
                    Command.moveDown();
                    break;
                case "l":
                    Command.moveLeft();
                    break;
                case "r":
                    Command.moveRight();
                    break;
                case "a":
                    Command.autoMode();
                    break;
            }

        }
        inputScanner.close();
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

    public static int[] storeData(Scanner fileScanner, ArrayList<Cell> maze) throws Exception {
        int mazeRow = 1;
        int mazeCol = 1;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] col = line.split(",");
            mazeCol = col.length;
            int edge;

            // Check row edge
            // edges 0 = top, 1 = right, 2 = bottom, 3 = left
            if (mazeRow == 1) {
                edge = 0;
            } else if (!fileScanner.hasNextLine()) {
                edge = 2;
            } else {
                edge = 3;
            }

            // Loop through each data
            for (int i = 0; i < mazeCol; i++) {
                int status;
                List<Integer> edges = new ArrayList<Integer>();

                // Check row edge and add to the list
                // edges: 0 = top, 1 = right, 2 = bottom, 3 = left
                if (!(edge == 3)) {
                    edges.add(edge);
                }

                // Check col edge and add to the list
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
                maze.add(new Cell(status, edges));
            }
            mazeRow++;
        }
        return new int[]{mazeRow, mazeCol};
    }

    public static void showData(ArrayList<Cell> maze, int row, int col) {
        // Print column head
        System.out.printf("%12s", " ");
        for (int i = 0; i < col; i++) {
            String colHead = "col_" + (i + 1);
            System.out.printf("%-12s", colHead);
        }
        System.out.println();

        // Print row head and maze data
        for (int i = 0; i < maze.size(); i++) {
            if (i % col == 0) {
                String rowHead = "row_" + (i / col);
                System.out.printf("%-14s", rowHead);
            }
            String status;
            if (maze.get(i).getStatus() == 2) {
                status = "R";
            } else if (maze.get(i).getStatus() == 3) {
                status = "F";
            } else {
                status = String.valueOf(maze.get(i).getStatus());
            }
            System.out.printf("%-12s", status);
            if ((i + 1) % col == 0) {
                System.out.println();
            }
        }
    }
}