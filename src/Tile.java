/**
 * This class represents a tile in the Colored Tile Puzzle game. 
 * Each block has its own unique number, and a color that can be: green, red or black. 
 * The cost of moving a green tile is 1, the cost of moving a red tile is 30, 
 * and a black tile cannot be moved. The number of the empty location in the board will be 0.
 * @author Meir Nizri
 */
public class Tile {

	private int number;
	private char color;

	/**
	 * Instantiates empty location in the board
	 */
	public Tile() {
		number = 0;
	}

	/**
	 * Instantiates a new tile.
	 * @param number the number of the tile
	 * @param color the color of the tile
	 */
	public Tile(int number, char color) {
		this.number = number;
		this.color = color;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the number.
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the color
	 */
	public char getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 * @param color the color to set
	 */
	public void setColor(char color) {
		this.color = color;
	}

	/**
	 * Checks if the tile is empty.
	 * @return true if the tile empty, false otherwise
	 */
	public boolean isEmpty() {
		return number == 0;
	}

	/**
	 * @return string representation of the tile
	 */
	public String toString() {
		return ""+number;
	}
}