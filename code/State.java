/**
 * The Interface State represent a state of any problem. The search algorithms 
 * gets states from classes that implement this interface only. The methods defined 
 * in this interface are necessary methods for the search algorithms to find the goal state.
 * @author Meir Nizri
 */
public interface State {

	/**
	 * Activates an operator on the current state.
	 * @param operator the number of the operator to activate
	 * @return the received state or null if this operator cannot be enabled.
	 */
	public State getOperator(int operator);

	/**
	 * @return true if this state is goal, false otherwise
	 */
	public boolean IsGoal();

	/**
	 * Represents a heuristic function that returns an estimate of the 
	 * cost from this state to the target state.
	 * @return estimation of the cost from this state to the target state
	 */
	public int h();

	/**
	 * @return the total cost from the initial state to this state
	 */
	public int getCost();

	/**
	 * @return the parent state that leads to this state.
	 */
	public State getParent();

	/**
	 * @return the discovery time of this state
	 */
	public int getDiscoveryTime();

	/**
	 * @return the number of total operators of this state
	 */
	public int getNumOperators();
	
	/**
	 * Checks if this state is out of the open list.
	 * The main use is in the DFBnB algorithm.
	 * @return true if this state is out from the open list, false otherwise
	 */
	public boolean isOut();

	/**
	 * Sets this state to be out or in the open list.
	 * @param isOut the value to set
	 */
	public void setOut(boolean isOut);
	
	/**
	 * @return the max threshold
	 */
	public int maxThreshold();
}