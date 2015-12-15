import java.util.*;

public class CornerSideHeuristics {
	/**
	 * As per Korf's,generate all of the permutations with a solved
	 * cube and then perform a breadth-first search.
	 *
	 */
	public static void generateCornerSideHeuristics() {
		// Create a rubikCube and initialize it with a solved rubikCube state
		RubikCube rubikCube = new RubikCube(RubikCube.SOLVEDSTATE.toCharArray());

		// Create a new Queue to perform BFS
		Queue<RubikCubeNode> q = new LinkedList<RubikCubeNode>();

		// Put the solved/initial state of the corners on the queue
		q.add(new RubikCubeNode(rubikCube.state, 0));
		int[] corners = new int[88179840];
		Set<Map.Entry<Character, int[]>> faces = RubikCube.FACES.entrySet();

		// Iterate until we can't anymore
		while (!q.isEmpty()) {
			RubikCubeNode current = q.poll();
			// For each rubikCube state,try all of possible
			// turns of each other faces
			for (Map.Entry<Character, int[]> face : faces) {
				// Perform  a clockwise turn
				char[] newState = RubikCube.rotate(current.state, face.getKey(), 1);
				int encodedCorner = Integer.parseInt(RubikCube.encodeCorners(newState));
				// Check to see if this combination has been made before
				if (corners[encodedCorner] == 0) {
					// for a new combination, add it to the queue
					q.add(new RubikCubeNode(newState, current.heuristic + 1));
				}
			}

			// Handle the current node.encode the corners, and check to
			// see if we've seen this permutation before.
			String encodedCorner = RubikCube.encodeCorners(current.state);
			int encodedCornerInt = Integer.parseInt(encodedCorner);
			if (corners[encodedCornerInt] == 0) {
				corners[encodedCornerInt] = current.heuristic;
				// Print this out
				System.out.println(encodedCorner + "," + current.heuristic);
			}
		}
	}

	/**
	 * A main function to kick off the heuristic table generation
	 * @param args
	 */
	public static void main(String[] args) {
		CornerSideHeuristics.generateCornerSideHeuristics();
	}
}