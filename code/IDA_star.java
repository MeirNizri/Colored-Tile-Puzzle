import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * IDA* algorithm. This is implementation with stack. We didn't use a closed list but just an open list. 
 * Each node removed from stack will mark in the open list as "out".
 */
public class IDA_star implements SearchAlgorithms {

	/**
	 * Instantiates a new IDA* algorithm.
	 */
	public IDA_star() {}

	/**
	 * Executes the IDA* algorithm
	 * @param initialState the initial state
	 * @param withOpen decide whether to print with open
	 * @return the search information
	 */
	public SearchInfo execute(State initialState, boolean withOpen) {
		// Start to measure the running time
		long startTime = System.currentTimeMillis();
		Stack<State> stack = new Stack<State>();
		Hashtable<String, State> openList = new Hashtable<String, State>();
		// The initial threshold for the iterative depth search
		int threshold = initialState.h();
		int numOperators = initialState.getNumOperators();

		while(threshold != Integer.MAX_VALUE) {
			int minF = Integer.MAX_VALUE;
			// insert initial state to stack
			initialState.setOut(false);
			stack.add(initialState);
			openList.put(initialState.toString(), initialState);

			while(!stack.isEmpty()) {
				if(withOpen) printOpenList(stack);
				State state = stack.pop();
				// If this state in the current path, remove it from stack
				if(state.isOut()) openList.remove(state.toString());
				else {
					state.setOut(true);
					stack.add(state);
					// Traverse all allowed operators
					for(int i=1; i<=numOperators; i++) {
						State operator = state.getOperator(i);
						if(operator == null) continue;
						String operatorStr = operator.toString();
						// If the node is larger than the threshold do not scan it
						if(f(operator) > threshold) {
							minF = Math.min(minF, f(operator));
							continue;
						}
						if(openList.containsKey(operatorStr) && openList.get(operatorStr).isOut())
							continue; 
						if(openList.containsKey(operatorStr) && !openList.get(operatorStr).isOut()) {
							// If its not marked as "out" and with greater evaluation, remove from stack and add later
							if(f(openList.get(operatorStr)) > f(operator)) {
								stack.remove(openList.get(operatorStr));
								openList.remove(operatorStr);
							}
							else continue;
						}
						if(operator.IsGoal()) {
							// Stop time and return all the search information
							double time = (double) (System.currentTimeMillis() - startTime) / 1000;
							return new SearchInfo(findPath(operator), operator.getCost(), time);
						}
						// It is new state. add to stack
						stack.add(operator);
						openList.put(operatorStr, operator);
					}
				}
			}
			threshold = minF;
		}
		// If there is no solution to the problem, return null paths
		return new SearchInfo(null, 0, 0);
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
	 * Evaluation function. Returns the cost of the path up to this state 
	 * and the heuristic estimation from that state to the goal state.
	 */
	private static int f(State state) {
		return (state.getCost() + state.h());
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