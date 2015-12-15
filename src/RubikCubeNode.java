import java.util.ArrayList;
import java.util.Map;

/**
 * class to keep track of Rubik cube states and their heuristic value.
 * Implements the Comparable interface to allow for easy comparison of
 * during IDA* to determine which node has the lower heuristic.
 */
public class RubikCubeNode implements Comparable<RubikCubeNode> {
	/**
	 * The state of the cube
	 */

	public char[] state;
	/**
	 * The heuristic value
	 */
	public int heuristic;

	/**
	 * The g value
	 */
	public int g;

	/**
	 * The path from the goal state to this node
	 */
	public String path;

	/**
	 *
	 * @param state the state of the cube
	 * @param heuristic the heuristic value
	 */
	public RubikCubeNode(char[] state, int heuristic) {
		this.state = state;
		this.heuristic = heuristic;
		this.g = 0;
		this.path = "";
	}

	/**
	 *
	 * @param state the state of the cube
	 * @param heuristic the heuristic value
	 * @param path the current path
	 */
	public RubikCubeNode(char[] state, int heuristic, String path) {
		this.state = state;
		this.heuristic = heuristic;
		this.g = 0;
		this.path = path;
	}

	/**
	 *
	 * @param state the state of the cube
	 * @param heuristic the heuristic value
	 * @param g the current g value
	 * @param path the current path
	 */
	public RubikCubeNode(char[] state, int heuristic, int g, String path) {
		this.state = state;
		this.heuristic = heuristic;
		this.g = g;
		this.path = path;
	}

	/**
	 * Generates all successors of the given node.
	 * @param node the node to find successors for
	 * @return an ArrayList<RubikCubeNode> of all successors for
	 * the param node
	 */
	public static ArrayList<RubikCubeNode> getSuccessors(RubikCubeNode node) {
		ArrayList<RubikCubeNode> successors = new ArrayList<RubikCubeNode>();
		for (Map.Entry<Character, int[]> face : RubikCube.FACES.entrySet()) {

			char[] newState = RubikCube.rotate(node.state, face.getKey(), 1);

			int encodedCorner = Integer.parseInt(RubikCube.encodeCorners(newState));

			String encodedEdges = RubikCube.encodeEdges(newState);

			int encodedEdgeSetOne = Integer.parseInt(encodedEdges.substring(0, 6));
			int encodedEdgeSetTwo = Integer.parseInt(encodedEdges.substring(6, 12));

			int[] possibleHeuristics = new int[3];
			possibleHeuristics[0] = IDEA_Star.corners[encodedCorner];
			possibleHeuristics[1] = IDEA_Star.edgesSetOne[encodedEdgeSetOne];
			possibleHeuristics[2] = IDEA_Star.edgesSetTwo[encodedEdgeSetTwo];
			int max = possibleHeuristics[0];
			for (int i = 1; i < possibleHeuristics.length; i++) {
				if (possibleHeuristics[i] > max) {
					max = possibleHeuristics[i];
				}
			}
			successors.add(new RubikCubeNode(newState, IDEA_Star.corners[encodedCorner], node.path + face.getKey() + "1")) ;
		}
		return successors;
	}

	@Override
	public int compareTo(RubikCubeNode b) {
		if (this.heuristic < b.heuristic) {
			return -1;
		} else if (this.heuristic > b.heuristic) {
			return 1;
		}
		return 0;
	}
}
