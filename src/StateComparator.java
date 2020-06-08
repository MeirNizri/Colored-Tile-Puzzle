import java.util.Comparator;

/**
 * The Class StateComparator represents a comparator for comparing two states. 
 * The current implementation is according to the evaluation function of A* and IDA*, 
 * which means the cost of the route up to the current state plus the value of the 
 * heuristic function on this state. The smaller ones will appear first. 
 * In case both are equal, the first discovered will appear first.
 * @author Meir Nizri
 */
public class StateComparator implements Comparator<State> {

	/**
	 * Compare between to states
	 * @return the the difference between them.
	 */
	@Override
	public int compare(State o1, State o2) {
		int difference = (o1.getCost() + o1.h())  -  (o2.getCost() + o2.h());
		if (difference == 0) difference = o1.getDiscoveryTime() - o2.getDiscoveryTime();
		return difference;
	}
}