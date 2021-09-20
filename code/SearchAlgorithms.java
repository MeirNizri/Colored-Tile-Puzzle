/**
 * The interface SearchAlgorithms represents different search algorithms. The implementation of this interface 
 * will be according to the "Strategy" design pattern. That is, each class that implements this interface will 
 * contain the "execute" method, which will run the appropriate search algorithm according to the constructor 
 * from which the SearchAlgorithm object is built.
 * The algorithms are implemented so that they can adapt to any problem that implemented the "state" interface.
 * So this class can be useful for many problems that require searching.
 */
public interface SearchAlgorithms {

	/**
	 * Executes the search algorithm.
	 * @param initialState the initial state
	 * @param withOpen decide whether to print the open list to screen or not
	 * @return the search information
	 */
	public SearchInfo execute (State initialState, boolean withOpen);
}