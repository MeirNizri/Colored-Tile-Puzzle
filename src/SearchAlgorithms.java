import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * The Class SearchAlgorithms represents different search algorithms. 
 * The algorithms are implemented so that they can adapt to any problem that implemented the "state" interface.
 * So this class can be useful for many problems that require searching.
 */
public class SearchAlgorithms {

	/**
	 * BFS algorithm. To improve the search we used Hashtable to the open and closed list to avoid unnecessary cycles.
	 * will not necessarily find the cheapest path but the shortest path.
	 * @param initialState the initial state
	 * @param withOpen decide whether to print the open list to screen or not
	 * @return the search information
	 */
	public static SearchInfo BFS (State initialState, boolean withOpen) {
		int numNodesCreated = 1;
		// Start to measure the running time
		long startTime = System.nanoTime();
		// Create all the data structure and insert initial state to them
		Queue<State> queue = new LinkedList<State>();
		Hashtable<String, State> openList = new Hashtable<String, State>();
		queue.add(initialState);
		openList.put(initialState.toString(), initialState);

		while(!queue.isEmpty()) {
			if(withOpen) printOpenList(queue);
			// Remove from the open list and enter to the closed list
			State state = queue.remove();
			// Traverse all allowed operators
			for(State operator : state.operators()) {
				numNodesCreated++;
				String operatorStr = operator.toString();
				if(!openList.containsKey(operatorStr)) {
					if(operator.IsGoal()) {
						// stop time and return all the search information
						double time = (double) (System.nanoTime() - startTime) / 1_000_000_000;
						return new SearchInfo(findPath(operator), operator.getCost(), numNodesCreated, time);
					}
					queue.add(operator);
					openList.put(operatorStr, operator);
				}
			}
		}
		// If there is no solution to the problem, return null path
		return new SearchInfo(null, 0, numNodesCreated, 0);
	}

	//----------------------------------------------------------------------------------------------------------------------------	

	/**
	 * DFID algorithm. This is the recursive version of the algorithm.
	 * will not necessarily find the cheapest path but the shortest path.
	 * @param initialState the initial state
	 * @param withOpen decide whether to print with open
	 * @return the search information
	 */
	public static SearchInfo DFID (State initialState, boolean withOpen) {
		int numNodesCreated = 1;
		// Start to measure the running time
		long startTime = System.nanoTime();
		// Set max depth and activate DFS that will scan to that depth
		for(int depth=1; depth<=initialState.maxThreshold(); depth++) {
			Hashtable<String, State> openList = new Hashtable<String, State>();
			SearchInfo result = LimitedDFS(initialState, depth, openList, numNodesCreated, withOpen);
			// Sum all nodes created in each recursive session
			numNodesCreated = result.getNumNodesCreated();
			if(!result.getCutoff()) {
				double time = (double) (System.nanoTime() - startTime) / 1_000_000_000;
				result.setTime(time);
				return result;
			}
		}
		// will never reach here
		return new SearchInfo(null, 0, numNodesCreated, 0);
	}

	/**
	 * Limited DFS. Operates the DFS only to a certain depth. 
	 * Returns a solution if found. If no goal node is found can return cutoff if cut because of depth block, 
	 * or fail if scanned entire search tree
	 */
	private static SearchInfo LimitedDFS(State state, int depth, Hashtable<String, State> openList, int numNodesCreated, boolean withOpen){
		if(state.IsGoal())  return new SearchInfo(findPath(state), state.getCost(), numNodesCreated, 0);
		else if(depth == 0) return new SearchInfo(true, numNodesCreated);
		else {
			String stateStr = state.toString();
			openList.put(stateStr, state);
			boolean isCutoff = false;
			// Traverse all allowed operators
			for(State operator : state.operators()) {
				numNodesCreated++;
				if(openList.containsKey(operator.toString())) 
					continue;
				// Recursive operation
				SearchInfo result = LimitedDFS(operator, depth-1, openList, numNodesCreated, withOpen);
				numNodesCreated = result.getNumNodesCreated();
				// Check if result contain solution path
				if(result.getCutoff()) isCutoff = true;
				else if(result.getsolutionPath() != null) return result; 
			}
			if(withOpen) printOpenList(openList.values());
			// No solution path found
			openList.remove(stateStr, state);
			state = null;
			if(isCutoff) 
				return new SearchInfo(true, numNodesCreated);
			else 
				return new SearchInfo(null, 0, numNodesCreated, 0);
		}
	}

	//----------------------------------------------------------------------------------------------------------------------------	

	/**
	 * A* algorithm. implemented eith closed list. The priority queue is arranged by StateComparator. 
	 * In case there are two states with equal evaluation, the one created first will be first in the queue.
	 * @param initialState the initial state
	 * @param withOpen decide whether to print with open
	 * @return the search information
	 */
	public static SearchInfo A_star(State initialState, boolean withOpen) {
		int numNodesCreated = 1;
		// Start to measure the running time
		long startTime = System.nanoTime();
		// Create all the data structure and insert initial state to them
		PriorityQueue<State> queue = new PriorityQueue<State>(new StateComparator());
		Hashtable<String, State> openList = new Hashtable<String, State>();
		Hashtable<String, State> closedList = new Hashtable<String, State>();
		queue.add(initialState);
		openList.put(initialState.toString(), initialState);

		while(!queue.isEmpty()) {
			if(withOpen) printOpenList(queue);
			// Take the first node from queue
			State state = queue.remove();
			String stateStr = state.toString();
			openList.remove(stateStr);
			if(state.IsGoal()) {
				// stop time and return all the search information
				double time = (double) (System.nanoTime() - startTime) / 1_000_000_000;
				return new SearchInfo(findPath(state), state.getCost(), numNodesCreated, time);
			}
			closedList.put(stateStr, state);
			// Traverse all allowed operators
			for(State operator : state.operators()) {
				numNodesCreated++;
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
		return new SearchInfo(null, 0, numNodesCreated, 0);
	}

	//----------------------------------------------------------------------------------------------------------------------------	

	/**
	 * IDA* algorithm. This is implementation with stack. We didn't use a closed list but just an open list. 
	 * Each node removed from stack will mark in the open list as "out".
	 * @param initialState the initial state
	 * @param withOpen decide whether to print with open
	 * @return the search information
	 */
	public static SearchInfo IDA_star(State initialState, boolean withOpen) {
		int numNodesCreated = 1;
		// Start to measure the running time
		long startTime = System.nanoTime();
		Stack<State> stack = new Stack<State>();
		Hashtable<String, State> openList = new Hashtable<String, State>();
		// The initial threshold for the iterative depth search
		int threshold = initialState.h();

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
					for(State operator : state.operators()) {
						numNodesCreated++;
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
							double time = (double) (System.nanoTime() - startTime) / 1_000_000_000;
							return new SearchInfo(findPath(operator), operator.getCost(), numNodesCreated, time);
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
		return new SearchInfo(null, 0, numNodesCreated, 0);
	}

	//----------------------------------------------------------------------------------------------------------------------------	

	/**
	 * DFBnB algorithm. This is implementation with stack. We didn't use a closed list but just an open list. 
	 * Each node removed from stack will mark in the open list as "out". It sorts the list of operators of each state by StateComparator. 
	 * In case there are two states with equal evaluation, the one created first will be first in the list.
	 * @param initialState the initial state
	 * @param withOpen decide whether to print with open
	 * @return the search information
	 */
	public static SearchInfo DFBnB(State initialState, boolean withOpen) {
		int numNodesCreated = 1;
		// Start to measure the running time
		long startTime = System.nanoTime();
		// Create all the data structure and insert initial state to them
		Stack<State> stack = new Stack<State>();
		Hashtable<String, State> openList = new Hashtable<String, State>();
		stack.add(initialState);
		openList.put(initialState.toString(), initialState);
		// Saves the goal state and path to it, which have the lowest cost found during the search
		List<State> resultPath = null;
		State goal = null;
		int threshold = initialState.maxThreshold();

		while(!stack.isEmpty()) {
			if(withOpen) printOpenList(stack);
			State state = stack.pop();
			// If this state in the current path, remove it from stack
			if(state.isOut()) openList.remove(state.toString());
			else {
				state.setOut(true);
				stack.add(state);
				// Save all allowed operators in sorted list according to the evaluation function
				List<State> operators = state.operators();
				operators.sort(new StateComparator());
				numNodesCreated += operators.size();
				// Cuts all states from the list whose evaluation is greater than the threshold
				int i = 0;
				while (i < operators.size()) {
				//for(int i=0; i<operators.size(); i++) {
					State operator = operators.get(i);
					String operatorStr = operator.toString();
					if(f(operator) >= threshold) {
						while(i < operators.size()) 
							operators.remove(i);
					}
					else if(openList.containsKey(operatorStr)) {
						State existOp = openList.get(operatorStr);
						if(existOp.isOut())
							operators.remove(i);
						else {
							// If its not marked as "out" and with greater evaluation, remove from stack and add later
							if(f(existOp) <= f(operator))
								operators.remove(i);
							else {
								stack.remove(existOp);
								openList.remove(operatorStr);
							}
						}
					}
					// If we reached here, f(operator)<threshold
					else if(operator.IsGoal()) {
						threshold = f(operator);
						resultPath = findPath(operator);
						goal = operator;
						// Cuts all states from the list whose evaluation is greater than the goal
						while(i < operators.size()) 
							operators.remove(i);
					}
					else i++;
				}
				// Add the entire remaining list to the stack in reverse order
				for(int j=operators.size()-1; j>=0; j--) {
					stack.add(operators.get(j));
					openList.put(operators.get(j).toString(), operators.get(j));
				}
			}
		}
		// Stop time and return all best solution found
		double time = (double) (System.nanoTime() - startTime) / 1_000_000_000;
		return new SearchInfo(resultPath, goal.getCost(), numNodesCreated, time);
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