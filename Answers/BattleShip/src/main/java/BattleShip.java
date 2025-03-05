import java.util.Scanner;
import java.util.Random;

/**
  The BattleShip class manages the gameplay of the Battleship game between two players.
  It includes methods to manage grids, turns, and check the game status.
 */
public class BattleShip {

    // Grid size for the game
    static final int GRID_SIZE = 10;

    // Player 1's main grid containing their ships
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's main grid containing their ships
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];

    // Player 1's tracking grid to show their hits and misses
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Player 2's tracking grid to show their hits and misses
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];

    // Scanner object for user input
    static Scanner strScanner = new Scanner(System.in);

    /**
      The main method that runs the game loop.
      It initializes the grids for both players, places ships randomly, and manages turns.
      The game continues until one player's ships are completely sunk.
     */
    public static void main(String[] args) {
        System.out.println("Welcome to BattleShip!");
        System.out.print("\nFirst player's name: ");
        String player1Name = strScanner.nextLine();
        System.out.print("\nSecond player's name: ");
        String player2Name = strScanner.nextLine();
        // Initialize grids for both players
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        // Place ships randomly on each player's grid
        placeShips(player1Grid);
        placeShips(player2Grid);

        // Variable to track whose turn it is
        boolean player1Turn = true;

        // Main game loop, runs until one player's ships are all sunk
        while (!isGameOver()) {
            if (player1Turn) {
                System.out.println(player1Name + "'s turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println(player2Name + "'s turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }
        System.out.println("Game Over!");
        if (!player1Turn) {
            System.out.println(player1Name + " wins!");
        }
        else  {
            System.out.println(player2Name + " wins!");
        }
    }

    /**
      Initializes the grid by filling it with water ('~').

      @param grid The grid to initialize.
     */
    static void initializeGrid(char[][] grid){
        for(int i = 0; i < GRID_SIZE; i++){
            for(int j = 0; j < GRID_SIZE; j++){
                grid[i][j] = '~';
            }
        }
    }

    /**
      Places ships randomly on the given grid.
      This method is called for both players to place their ships on their respective grids.

      @param grid The grid where ships need to be placed.
     */
    static void placeShips(char[][] grid) {
        for(int i = 2; i <= 5 ; i++){
            Random random = new Random();
            boolean isPlaced = false;
            while(!isPlaced){
                int row = random.nextInt(GRID_SIZE);
                int col = random.nextInt(GRID_SIZE);
                boolean horizontal = random.nextBoolean();
                if(canPlaceShip(grid, row, col, i , horizontal)){
                    for(int j = 0; j < i; j++){
                        if(!horizontal){
                            grid[row + j][col] = 'S';
                        }
                        else {
                            grid[row][col + j] = 'S';
                        }
                    }
                    isPlaced = true;
                }
            }
        }
    }

    /**
      Checks if a ship can be placed at the specified location on the grid.
      This includes checking the size of the ship, its direction (horizontal or vertical),
      and if there's enough space to place it.

      @param grid The grid where the ship is to be placed.
      @param row The starting row for the ship.
      @param col The starting column for the ship.
      @param size The size of the ship.
      @param horizontal The direction of the ship (horizontal or vertical).
      @return true if the ship can be placed at the specified location, false otherwise.
     */
    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (!horizontal) {
            if (row + (size - 1) >= GRID_SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] == 'S') {
                    return false;
                }
            }
        }
        else {
            if (col + (size - 1) >= GRID_SIZE) {
                return false;
            }
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] == 'S') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
      Manages a player's turn, allowing them to attack the opponent's grid
      and updates their tracking grid with hits or misses.

      @param opponentGrid The opponent's grid to attack.
      @param trackingGrid The player's tracking grid to update.
     */
    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        System.out.print("Enter target (For example A1): ");
        String target = strScanner.nextLine();
        if (!isValidInput(target)) {
            System.out.println("Invalid input!");
            System.out.println("Switching player...");
        }
        else {
            int row = target.charAt(1) - '0';
            int col = 0;
            if (target.charAt(0) >= 'A' && target.charAt(0) <= 'J') {
                col = target.charAt(0) - 'A';
            }
            if (target.charAt(0) >= 'a' && target.charAt(0) <= 'j') {
                col = target.charAt(0) - 'a';
            }
            if (opponentGrid[row][col] == 'S') {
                System.out.println("Hit!");
                trackingGrid[row][col] = 'X';
            }
            else {
                System.out.println("Miss!");
                trackingGrid[row][col] = '0';
            }
        }
    }

    /**
      Checks if the game is over by verifying if all ships are sunk.

      @return true if the game is over (all ships are sunk), false otherwise.
     */
    static boolean isGameOver() {
        if (!allShipsSunk(player1TrackingGrid) && !allShipsSunk(player2TrackingGrid)) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
      Checks if all ships have been destroyed on a given grid.

      @param grid The grid to check for destroyed ships.
      @return true if all ships are sunk, false otherwise.
     */
    static boolean allShipsSunk(char[][] grid) {
        int temp = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == 'X') {
                    temp++;
                }
            }
        }
        if (temp == 14) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
      Validates if the user input is in the correct format (e.g., A5).

      @param input The input string to validate.
      @return true if the input is in the correct format, false otherwise.
     */
    static boolean isValidInput(String input) {
        if (input.length() < 2 || input.length() > 3) {
            return false;
        }
        if ((input.charAt(0) < 'A' || (input.charAt(0) > 'J' && input.charAt(0) < 'a')) ||
                (input.charAt(0) > 'j')) {
            return false;
        }
        if ((input.charAt(1) < '0' || input.charAt(1) > '9')) {
            return false;
        }
        return true;
    }

    /**
      Prints the current state of the player's tracking grid.
      This method displays the grid, showing hits, misses, and untried locations.

      @param grid The tracking grid to print.
     */
    static void printGrid (char[][] grid) {
        System.out.print("   ");
        for(int i = 0; i < GRID_SIZE; i++){
            System.out.print((char)(i+65) + " ");
        }
        System.out.println();
        for(int i = 0; i < GRID_SIZE; i++){
            System.out.print(" " + i + " ");
            for(int j = 0; j < GRID_SIZE; j++){
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}
