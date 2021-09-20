import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * DFID algorithm. This is the recursive version of the algorithm.
 * will not necessarily find the cheapest path but the shortest path.
 */
public class DFID implements SearchAlgorithms {

	/**
	 * Instantiates a new DFID algorithm.
	 */
	public DFID() {}

	/**
	 * Executes the DFID algorithm
	 * @param initialState the initial state
	 * @param withOpen decide whether to print with open
	 * @return the search information
	 */
	public SearchInfo execute(State initialState, boolean withOpen) {
		// Start to measure the running time
		long startTime = System.currentTimeMillis();
		// Set max depth and activate DFS that will scan to that depth
		for(int depth=1; depth<=initialState.maxThreshold(); depth++) {
			Hashtable<String, State> openList = new Hashtable<String, State>();
			SearchInfo result = LimitedDFS(initialState, depth, openList, withOpen);
			// Sum all nodes created in each recursive session
			if(!result.getCutoff()) {
				double time = (double) (System.currentTimeMillis() - startTime) / 1000;
				result.setTime(time);
				return result;
			}
		}
		// will never reach here
		return new SearchInfo(null, 0, 0);
	}

	/**
	 * Limited DFS. Operates the DFS only to a certain depth. 
	 * Returns a solution if found. If no goal node is found can return cutoff if cut because of depth block, 
	 * or fail if scanned entire search tree
	 */
	private static SearchInfo LimitedDFS(State state, int depth, Hashtable<String, State> openList, boolean withOpen){
		if(state.IsGoal())  return new SearchInfo(findPath(state), state.getCost(), 0);
		else if(depth == 0) return new SearchInfo(true);
		else {
			String stateStr = state.toString();
			openList.put(stateStr, state);
			boolean isCutoff = false;
			// Traverse all allowed operators
			int numOperators = state.getNumOperators();
			for(int i=1; i<=numOperators; i++) {
				State operator = state.getOperator(i);
				if(operator == null) continue;
				if(openList.containsKey(operator.toString())) 
					continue;
				// Recursive operation
				SearchInfo result = LimitedDFS(operator, depth-1, openList, withOpen);
				// Check if result contain solution path
				if(result.getCutoff()) isCutoff = true;
				else if(result.getsolutionPath() != null) return result; 
			}
			if(withOpen) printOpenList(openList.values());
			// No solution path found
			openList.remove(stateStr);
			if(isCutoff) 
				return new SearchInfo(true);
			else 
				return new SearchInfo(null, 0, 0);
		}
	}

	//----------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Find path from initial state to goal using the goal state
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