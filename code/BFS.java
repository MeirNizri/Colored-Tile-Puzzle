import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * BFS algorithm. To improve the search we used Hashtable to the open and closed list to avoid unnecessary cycles.
 * will not necessarily find the cheapest path but the shortest path.
 */
public class BFS implements SearchAlgorithms {

	/**
	 * Instantiates a new BFS algorithm.
	 */
	public BFS() {}

	/**
	 * Executes the BFS algorithm
	 * @param initialState the initial state
	 * @param withOpen decide whether to print the open list to screen or not
	 * @return the search information
	 */
	public SearchInfo execute (State initialState, boolean withOpen) {
		// Start to measure the running time
		long startTime = System.currentTimeMillis();
		// Create all the data structure and insert initial state to them
		Queue<State> queue = new LinkedList<State>();
		Hashtable<String, State> openList = new Hashtable<String, State>();
		queue.add(initialState);
		openList.put(initialState.toString(), initialState);
		int numOperators = initialState.getNumOperators();

		while(!queue.isEmpty()) {
			if(withOpen) printOpenList(queue);
			// Remove from the open list and enter to the closed list
			State state = queue.remove();
			// Traverse all allowed operators
			for(int i=1; i<=numOperators; i++) {
				State operator = state.getOperator(i);
				if(operator == null) continue;
				String operatorStr = operator.toString();
				if(!openList.containsKey(operatorStr)) {
					if(operator.IsGoal()) {
						// stop time and return all the search information
						double time = (double) (System.currentTimeMillis() - startTime) / 1000;
						return new SearchInfo(findPath(operator), operator.getCost(), time);
					}
					queue.add(operator);
					openList.put(operatorStr, operator);
				}
			}
		}
		// If there is no solution to the problem, return null path
		return new SearchInfo(null, 0, 0);
	}

	//----------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Find path from initial state to goal using the goal state.
	 * @param goal the goal state
	 * @return list of all states in the path to goal
	 */
	private static List<State> findPath(State goal) {
		List<State> solutionPath = new LinkedList<State>();
		State temp = goal;
		solutionPath.add(goal);
		// backtrack search
		while (temp.getCost() != 0) {
			temp = temp.getParent();
			// add to the start of the list
			solutionPath.add(0, temp);
		}
		return solutionPath;
	}

	/**
	 * Prints the open list.
	 * @param collection the open list
	 */
	private static void printOpenList(Collection<State> collection) {
		for(State n : collection) 
			System.out.println(n.toString());
		System.out.println("-----------------------------------");
	}
}