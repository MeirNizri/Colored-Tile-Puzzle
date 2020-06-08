import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This class represents the Colored Tile Puzzle game. This game has a board that contains tiles with one empty cell. 
 * Each tile has a number and color. The goal is to arrange the board so that the tiles are arranged by their number 
 * and the empty cell at the end. This class will read the initial state from the input.txt file and run a search algorithm 
 * to find the solution. If there is a solution it will write to output.txt file all the steps that each tile did, 
 * the number of nodes created, and the cost of the solution. If there is no solution will write "no path". 
 * In the input file you can choose which search algorithm to use from the algorithms: BFS, DFID, A*, IDA*, DFBnB. 
 * You can also choose whether to write the algorithm's runtime to output.txt, and to print the open list to the screen in each iteration.
 * @author Meir Nizri
 */
public class Ex1 {

	static String searchAlgorithm;
	static boolean withTime, withOpen;
	static PuzzleState initialState;

	/**
	 * The main method.
	 */
	public static void main(String[] args) {
		readInput();
		// initialize the game and start game
		PuzzleGameManager manager = new PuzzleGameManager(searchAlgorithm, initialState, withOpen);
		manager.startGame();
		// Write result
		writeOutput(manager);
	}

	/**  Read input from "input.txt" */
	public static void readInput() {
		BufferedReader reader;	
		try {
			reader = new BufferedReader(new FileReader("input.txt"));
			// Read the search algorithm to run
			searchAlgorithm = reader.readLine();
			// Decide whether to write the run time and print the open list
			if(reader.readLine().matches("with time")) withTime = true;
			if(reader.readLine().matches("with open")) withOpen = true;
			// Obtain the number of rows and columns of the board
			String boardSize = reader.readLine();
			int indexOfX = boardSize.indexOf('x');
			int numRows = Integer.parseInt(boardSize.substring(0, indexOfX));
			int numCols = Integer.parseInt(boardSize.substring(indexOfX+1, boardSize.length()));
			// Create a matrix that contains the colors of each tile
			char[][] tilesColor = new char[numRows][numCols];
			String blackTiles = reader.readLine().substring(6).replaceAll("\\s", "");
			if(!blackTiles.isEmpty()) {
				for(String s : blackTiles.split(",")) {
					int i = Integer.parseInt(s);
					tilesColor[i/numCols][i%numCols] = 'b';
				}
			}
			String redTiles = reader.readLine().substring(4).replaceAll("\\s", "");
			if(!redTiles.isEmpty()) {
				for(String s : redTiles.split(",")) {
					int i = Integer.parseInt(s);
					tilesColor[i/numCols][i%numCols] = 'r';
				}
			}
			// Create the tile board
			Tile[][] board = new Tile[numRows][numCols];
			for(int i=0; i<numRows; i++) {
				String[] stateRow = reader.readLine().split(",");
				for(int j=0; j<numCols; j++) {
					if(stateRow[j].charAt(0) != '_') {
						int number = Integer.parseInt(stateRow[j]);
						char color = tilesColor[number/numCols][number%numCols];
						if(color != 'b' && color != 'r') color = 'g';
						board[i][j] = new Tile(number, color);
					}
					else board[i][j] = new Tile();
				}
			}
			// Create the initial state of the game
			initialState = new PuzzleState(board);

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write output to "output.txt"
	 * @param manager The game manager who ran the game
	 */
	public static void writeOutput(PuzzleGameManager manager) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter("output.txt"));
			// Write the moves of each tile to solution and the total number of nodes created
			writer.println(manager.getMoves());
			writer.println("Num: " + manager.getNumNodeCreated());
			// Write the cost of the solution if one exists
			if(manager.getsolutionPath() != null) {
				writer.println("Cost: " + manager.getCost());
				// Write the time if chooses to write it
				if(withTime)
					writer.println(manager.getTime() + " seconds");	
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}