import java.util.Collection;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * DFBnB algorithm. This is implementation with stack. We didn't use a closed list but just an open list. 
 * Each node removed from stack will mark in the open list as "out". It sorts the list of operators of each state by StateComparator. 
 * In case there are two states with equal evaluation, the one created first will be first in the list.
 */
public class DFBnB implements SearchAlgorithms {

	/**
	 * Instantiates a new DFBnB algorithm.
	 */
	public DFBnB() {}

	/**
	 * Executes the DFBnB algorithm
	 * @param initialState the initial state
	 * @param withOpen decide whether to print with open
	 * @return the search information
	 */
	public SearchInfo execute(State initialState, boolean withOpen) {
		// Start to measure the running time
		long startTime = System.currentTimeMillis();
		// Create all the data structure and insert initial state to them
		Stack<State> stack = new Stack<State>();
		Hashtable<String, State> openList = new Hashtable<String, State>();
		stack.add(initialState);
		openList.put(initialState.toString(), initialState);
		// Saves the goal state and path to it, which have the lowest cost found during the search
		List<State> resultPath = null;
		State goal = null;
		int threshold = initialState.maxThreshold();
		int numOperators = initialState.getNumOperators();

		while(!stack.isEmpty()) {
			if(withOpen) printOpenList(stack);
			State state = stack.pop();
			// If this state in the current path, remove it from stack
			if(state.isOut()) openList.remove(state.toString());
			else {
				state.setOut(true);
				stack.add(state);
				LinkedList<State> operators = new LinkedList<State>();
				// Save all allowed operators in sorted list according to the evaluation function
				for(int i=1; i<=numOperators; i++) {
					State operator = state.getOperator(i);
					if(operator != null) operators.add(operator);
				}
				operators.sort(new StateComparator());
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
		double time = (double) (System.currentTimeMillis() - startTime) / 1000;
		return new SearchInfo(resultPath, goal.getCost(), time);
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