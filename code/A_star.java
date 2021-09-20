import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * A* algorithm. implemented eith closed list. The priority queue is arranged by StateComparator. 
 * In case there are two states with equal evaluation, the one created first will be first in the queue.
 */
public class A_star implements SearchAlgorithms {

	/**
	 * Instantiates a new A* algorithm.
	 */
	public A_star() {}

	/**
	 * Executes the A* algorithm
	 * @param initialState the initial state
	 * @param withOpen decide whether to print with open
	 * @return the search information
	 */
	public SearchInfo execute(State initialState, boolean withOpen) {
		// Start to measure the running time
		long startTime = System.currentTimeMillis();
		// Create all the data structure and insert initial state to them
		PriorityQueue<State> queue = new PriorityQueue<State>(new StateComparator());
		Hashtable<String, State> openList = new Hashtable<String, State>();
		Hashtable<String, State> closedList = new Hashtable<String, State>();
		queue.add(initialState);
		openList.put(initialState.toString(), initialState);
		int numOperators = initialState.getNumOperators();

		while(!queue.isEmpty()) {
			if(withOpen) printOpenList(queue);
			// Take the first node from queue
			State state = queue.remove();
			String stateStr = state.toString();
			openList.remove(stateStr);
			if(state.IsGoal()) {
				// stop time and return all the search information
				double time = (double) (System.currentTimeMillis() - startTime) / 1000;
				return new SearchInfo(findPath(state), state.getCost(), time);
			}
			closedList.put(stateStr, state);
			// Traverse all allowed operators
			
			for(int i=1; i<=numOperators; i++) {
				State operator = state.getOperator(i);
				if(operator == null) continue;
				String operatorStr = operator.toString();
				// if its not in the open and closed lists insert to queue
				if(!closedList.containsKey(operatorStr) && !openList.containsKey(operatorStr)) {
					queue.add(operator);
					openList.put(operatorStr, operator);
				}
				// If its in the the open list but with lower evaluation, switch between them
				else if(openList.containsKey(operatorStr) && f(openList.get(operatorStr))>f(operator)) {
					queue.remove(openList.get(operatorStr));
					queue.add(operator);
					openList.replace(operatorStr, operator);
				}
			}
		}
		// If there is no solution to the problem, return null path
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