package Project1_135;

//นายธนกฤต     ชุติวงศ์ธนะพัฒน์       6513112
//นายภูรินท์	   พงษ์พานิช 	        6513135
//นายจารุภัทร	   โชติสิตานันท์	    6513161
//นางสาวชลิษา	   บัวทอง		    6513163

import Project1_135.model.Cell;
import Project1_135.util.Command;

import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    private final static Scanner inputScanner = new Scanner(System.in);
    private final static ArrayList<ArrayList<Cell>> maze = new ArrayList<>();
    private final static Command command = new Command(maze);
    private static int[] mazeSize = new int[2];

    public static void main(String[] args) {
        String path = "src/main/java/Project1_135/maze/";
        while(true) {
            boolean fileFound = false;
            boolean noFoodLeft = false;
            do {
                try {
                    // Receive file name from user
                    String filename = receiveInput("New file name = ");
                    if(filename.equalsIgnoreCase("exit")) {
                        System.exit(0);
                    }

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
            command.showTable();

            // Wait for user input
            while (!noFoodLeft) {
                String input = "";
                try {
                    input = receiveInput("Enter move (U = up, D = down, L = left, R = right, A = auto)");
                } catch (Exception e) {
                }

                // direction: 0 = top, 1 = right, 2 = bottom, 3 = left
                switch (input) {
                    case "u":
                        noFoodLeft = Command.tryMove(0);
                        break;
                    case "d":
                        noFoodLeft = Command.tryMove(2);
                        break;
                    case "l":
                        noFoodLeft = Command.tryMove(3);
                        break;
                    case "r":
                        noFoodLeft = Command.tryMove(1);
                        break;
                    case "a":
                        noFoodLeft = Command.autoMode(1);
                        break;
                }
            }
            System.out.println("-------------------------------\n");
            maze.clear();
        }
        //inputScanner.close();
    }

    public static String receiveInput(String message)  {
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
        int amountFood = 0;
        int mazeRow = 0;
        int mazeCol = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] col = line.split(",");
            mazeCol = col.length;
            ArrayList<Cell> row = new ArrayList<>();

            // Loop through each data
            for (int i = 0; i < mazeCol; i++) {
                int status;

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
                        amountFood++;
                        status = 3;
                        break;
                    default:
                        throw new Exception("Invalid character in file");
                }

                // Store data
                row.add(new Cell(status));
            }
            maze.add(row);
            mazeRow++;
        }

        // Check maze size
        if (mazeRow < 3 || mazeCol < 3) {
            throw new Exception("Invalid maze size");
        }

        // Set amount of food
        command.setsFood(amountFood);

        return new int[]{mazeRow, mazeCol};
    }
}