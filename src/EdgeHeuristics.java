import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class EdgeHeuristics {
	public static void generateEdgeHeuristics(int set) {
		// Create a rubikCube and initialize it with a solved rubikCube state
		RubikCube rubikCube = new RubikCube(RubikCube.SOLVEDSTATE.toCharArray());

		// Create a new Queue to perform BFS
		Queue<RubikCubeNode> q = new LinkedList<RubikCubeNode>();

		// Put the solved/initial state of the rubikCube on the queue
		q.add(new RubikCubeNode(rubikCube.state, 0));
		int[] edgeHeuristics = new int[42577920];
		int[][] edges = new int[6][2];
		int base = 6 * set;
		int limit = 6 + base;

		// Select the proper set of edges to work with
		if (set == 0 || set == 1) {
			int[][] tempEdges = RubikCube.EDGES;
			for (int i = base; i < limit; i++) {
				edges[i - base] = tempEdges[i];
			}
		} else {
			System.err.println("Please specify a valid set of edges");
		}

		Set<Map.Entry<Character, int[]>> faces = RubikCube.FACES.entrySet();

		// Iterate until we can't anymore
		while (!q.isEmpty()) {
			RubikCubeNode current = q.poll();
			// For each rubikCube state, try all of possible
			// turns of each other other faces
			for (Map.Entry<Character, int[]> face : faces) {
				// Perform a clockwise turn
				char[] newState = RubikCube.rotate(current.state, face.getKey(), 1);
				int encodedEdge = Integer.parseInt(RubikCube.encodeEdges(newState).substring(base, limit));
				// Check to see if this combination has been made before
				if (edgeHeuristics[encodedEdge] == 0) {
					// for a new combination, add it to the queue
					q.add(new RubikCubeNode(newState, current.heuristic + 1));
				}
			}

			// Handle the current node. encode the edgeHeuristics, and check to
			// see if this permutation is seen before.
			String encodedEdge = RubikCube.encodeEdges(current.state).substring(base, limit);
			int encodedEdgeInt = Integer.parseInt(encodedEdge);
			if (edgeHeuristics[encodedEdgeInt] == 0) {
				edgeHeuristics[encodedEdgeInt] = current.heuristic;
				System.out.println(encodedEdgeInt + "," + current.heuristic);
			}
		}
	}

	public static void main(String[] args) {
		if (args.length <= 0) {
			EdgeHeuristics.generateEdgeHeuristics(1);
		} else {
			EdgeHeuristics.generateEdgeHeuristics(Integer.parseInt(args[0]));
		}
	}
}
