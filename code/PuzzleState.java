import java.awt.Point;
import java.util.Arrays;

/**
 * This class represents a state of the Colored Tile Puzzle game. 
 * PuzzleState implements State interface, and therefore, using Polymorphism it
 * can be entered as a parameter for the search algorithms.
 * @author Meir Nizri
 */
public class PuzzleState implements State {

	static int redCost = 30, greenCost = 1, discovery = 0;
	private Tile[][] board;
	/** the (x,y) of the empty location on the board. */
	private Point empty;
	private State parent;
	private int cost, lastOperator, discoveryTime; 
	/** Remark if this state is out of the open list. */ 
	private boolean out; 

	/** Empty constructor */
	public PuzzleState() { }

	/**
	 * Instantiates a new puzzle state.
	 * @param board the board
	 */
	public PuzzleState(Tile[][] board) {
		this.board = board;	
		this.discoveryTime = discovery++;
		// search for the empty location
		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
				if(board[i][j].isEmpty()) {
					this.empty = new Point(i,j);
					break;
				}
			}
		}
	}

	/**
	 * Instantiates a new puzzle state.
	 * @param board the board
	 * @param empty the empty location
	 * @param lastOperator the last operator
	 * @param parent the parent of this state
	 * @param cost the cost from initial state to current state
	 */
	public PuzzleState(Tile[][] board, Point empty, State parent, int cost, int lastOperator) {
		this.board = board;
		this.empty = empty;
		this.parent = parent;
		this.cost = cost;
		this.discoveryTime = discovery++;
		this.out = false;
		this.lastOperator = lastOperator;
	}

	/**
	 * Activates an operator on the current state.
	 * @param operator the number of the operator to activate
	 * @return the received state or null if this operator cannot be enabled.
	 */
	@Override
	public State getOperator(int operator) {
		Point newEmpty = new Point(empty);
		// Build the new empty location
		if(operator==1 && lastOperator!=3 && empty.y<board[0].length-1)   newEmpty.y++;  // Left
		else if(operator==2 && lastOperator!=4 && empty.x<board.length-1) newEmpty.x++;  // Up
		else if(operator==3 && lastOperator!=1 && empty.y>0)		 	  newEmpty.y--;  // Right
		else if(operator==4 && lastOperator!=2 && empty.x>0)              newEmpty.x--;  // Down
		else return null;
		// If the tile to move is black return null
		if(board[newEmpty.x][newEmpty.y].getColor() == 'b') return null;
		// Calculate the cost of moving the tile from the new empty location
		int newCost = 0;
		if(board[newEmpty.x][newEmpty.y].getColor() == 'r') newCost = cost + redCost;
		else newCost = cost + greenCost;
		// return the new state
		return new PuzzleState(moveTile(newEmpty), newEmpty, this, newCost, operator);
	}

	// A private method designed to create a new board with sliding tile to the new empty location
	private Tile[][] moveTile(Point newEmpty) {
		int numRows = board.length, numCols = board[0].length;
		// Creates new board identical to the parent board
		Tile[][] newBoard = new Tile[numRows][numCols];
		for(int i=0; i<numRows; i++) {
			for(int j=0; j<numCols; j++) 
				newBoard[i][j] = new Tile(board[i][j].getNumber(), board[i][j].getColor());
		}
		// Switch between the empty place in the previous state and the empty place in the current state
		newBoard[empty.x][empty.y] = new Tile(board[newEmpty.x][newEmpty.y].getNumber(), board[newEmpty.x][newEmpty.y].getColor());
		newBoard[newEmpty.x][newEmpty.y] = new Tile();
		return newBoard;
	}

	/**
	 * Checks if this is goal state. That is, checks whether 
	 * all numbers are arranged and the empty location is at the end
	 * @return true if goal, false otherwise
	 */
	@Override
	public boolean IsGoal() {
		int numRows = board.length, numCols = board[0].length;
		for(int i=0; i<numRows; i++) {
			for(int j=0; j<numCols; j++) {
				// Checks that the tile is in its proper place and also that it is not the empty
				if(board[i][j].getNumber()!=(i*numCols+j+1) && board[i][j].getNumber()!=0) 
					return false;
			}
		}
		return true;
	}

	/**
	 * Heuristic function that returns an estimate of the cost from this state to the goal state.
	 * I have defined this function much like the Manhattan distance method. That is, to sum 
	 * the optimal number of steps for each tile, Which is not empty, from its current location to the 
	 * place it needs to reach. The cost of each such step is in accordance with the color of the tile.
	 * @return estimation of the cost from this state to the target state
	 */
	@Override
	public int h() {
		int numRows = board.length, numCols = board[0].length;
		int number = 0, distance = 0, value = 0;
		for(int i=0; i<numRows; i++) {
			for(int j=0; j<numCols; j++) {
				if(!board[i][j].isEmpty()) {
					number = board[i][j].getNumber() - 1;
					// Calculate distance
					distance = Math.abs(number/numCols - i) + Math.abs(number%numCols - j);
					if(board[i][j].getColor() == 'r') value += (distance*redCost);
					else value += (distance*greenCost);
				}
			}
		}
		return value;
	}

	/** @return the parent state that leads to this state */
	@Override
	public State getParent() {
		return parent;
	}

	/** @return the total cost from the initial state to this state */
	@Override
	public int getCost() {
		return cost;
	}

	/** @return the discovery time of this state */
	@Override
	public int getDiscoveryTime() {
		return discoveryTime;
	}
	
	/** @return the number of total operators of this state */
	@Override
	public int getNumOperators() {
		return 4;
	}

	/**
	 * Checks if this state is out of the open list.
	 * The main use is in the DFBnB algorithm.
	 * @return true if this state is out from the open list, false otherwise
	 */
	@Override
	public boolean isOut() {
		return out;
	}

	/**
	 * Sets this state to be out or in the open list.
	 * @param isOut the value to set
	 */
	@Override
	public void setOut(boolean out) {
		this.out = out;
	}

	/** @return the lastOperator that leads to this state */
	public int getLastOperator() {
		return lastOperator;
	}

	/** @return The last tile number moved to reach the current state */
	public int getTileMoved() {
		if(parent == null) return 0;
		PuzzleState puzzleState = (PuzzleState)parent;
		return puzzleState.board[empty.x][empty.y].getNumber();
	}

	/**
	 * Gets the tile in the location (x,y)
	 * @return the tile
	 */
	public Tile getTile(int x, int y) {
		return board[x][y];
	}

	/**
	 * Compares two states and returns true if equal
	 * @param obj the object to compare
	 * @return true if equals, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		return this.toString() == obj.toString();
	}

	/**
	 * @return String representation of this state
	 */
	@Override
	public String toString() {
		String stateString = "";
		for (Tile[] row : board) 
			stateString += Arrays.toString(row) + "\n";
		return stateString;
	}

	/** @return the max threshold */
	@Override
	public int maxThreshold() {
		return Math.min(factorial(board.length*board[0].length-1), Integer.MAX_VALUE);
	}

	public int factorial(int n) {
		if(n>12) return Integer.MAX_VALUE;
		int fact = 1;
		for (int i = 2; i <= n; i++) {
			fact = fact * i;
		}
		return fact;
	}
}