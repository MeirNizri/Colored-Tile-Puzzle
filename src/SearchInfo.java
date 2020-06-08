import java.util.List;

/**
 * The Class SearchInfo represents important information about the solution returned 
 * from a search algorithms as well as information about the process required to find this solution. 
 * Information such as: the solution path, the cost to reach the solution, the total nodes created, 
 * the algorithm run time. It also contains useful information for a search algorithm like DFID, 
 * which determines whether the algorithm has stopped because there is no solution or cut off because of the threshold.
 * @author Meir Nizri
 */
public class SearchInfo {

	private List<State> solutionPath;
	private int cost, numNodesCreated;
	private double time;
	private boolean cutoff;

	/** Empty constructor*/
	public SearchInfo() { }

	/**
	 * Instantiates a new search info. 
	 * Created to build a searchInfo that indicates cutoff.
	 */
	public SearchInfo(boolean cutoff, int numNodeCreated) {
		this.cutoff = cutoff;
		this.numNodesCreated = numNodeCreated;
	}

	/**
	 * Instantiates a new search info.
	 * @param solutionPath the solution path
	 * @param cost the cost to goal
	 * @param numNodeCreated the total number of nodes created
	 * @param time the run time of the algorithm
	 */
	public SearchInfo(List<State> solutionPath, int cost, int numNodeCreated, double time) {
		this.solutionPath = solutionPath;
		this.time = time;
		this.numNodesCreated = numNodeCreated;
		this.cost = cost;
		this.cutoff = false;
	}

	/** @return the solution Path */
	public List<State> getsolutionPath() {
		return solutionPath;
	}

	/** @return the cost */
	public int getCost() {
		return cost;
	}
	/**
	 * Sets the cost */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**@return the number of nodes created */
	public int getNumNodesCreated() {
		return numNodesCreated;
	}
	/** Sets the num nodes created */
	public void setNumNodesCreated(int numNodeCreated) {
		this.numNodesCreated = numNodeCreated;
	}

	/**
	 * Gets the run time of the algorithm@return the time */
	public double getTime() {
		return time;
	}
	/** Sets the time */
	public void setTime(double time) {
		this.time = time;
	}

	/** @return true if cutoff, false otherwise */
	public boolean getCutoff() {
		return cutoff;
	}
	/** Sets the cutoff */
	public void setCutoff(boolean cutoff) {
		this.cutoff = cutoff;
	}
}