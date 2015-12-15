import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IDEA_Star {

	public static final int[] corners = getHeuristics(88179840, "cornerSide.csv");
	public static final int[] edgesSetOne = getHeuristics(42577920, "edgesSetOne.csv");
	public static final int[] edgesSetTwo = getHeuristics(42577920, "edgesSetTwo.csv");
	public static int nextHeuristicBound;
	public static int numberOfNodesVisited;
	public static PriorityQueue<RubikCubeNode> frontier = new PriorityQueue<RubikCubeNode>();
	public static HashSet<RubikCubeNode> explored = new HashSet<RubikCubeNode>();

	/**
	 * Performs the IDA* search for our Rubik's cube.
	 * @param startState the starting state of the cube
	 * @param verbose true,to print out more details about the IDA* algorithm
	 * @return the string that represents the optimal solution
	 */
	public static String executeIDAStar(char[] startState, boolean verbose) {
		if (Arrays.equals(startState, RubikCube.SOLVEDSTATE.toCharArray())) {
			return "The given cube is already in a solved state";
		}
		// Initialize the root node with the start state
		RubikCubeNode start = new RubikCubeNode(startState, corners[Integer.parseInt(RubikCube.encodeCorners(startState))]);
		// And put the start node on the openSet
		if (verbose) {
			System.out.println("Initial heuristic value: " + start.heuristic);
		}
		// Initialize nextHeuristicBound with our starting heuristic value
		nextHeuristicBound = start.heuristic;
		// Initialize numberOfNodesVisited
		numberOfNodesVisited = 0;

		RubikCubeNode end = null;

		// Loop until a solution is found
		while (end == null) {
			if (verbose) {
				System.out.println("Current bound is: " + nextHeuristicBound);
				System.out.println("# of Nodes visited: " + numberOfNodesVisited);
			}
			frontier.add(start);
			end = search(nextHeuristicBound);

			nextHeuristicBound++;

			frontier.clear();
			explored.clear();
		}
		if (verbose) {
			System.out.println("Solved!");
			System.out.println("Total # of nodes visited: " + numberOfNodesVisited);
		}
		return formatSolution(end.path);
	}

	/**
	 * The recursive expanding function that will expand nodes as per
	 * the rules of IDA*
	 * @param bound the current bound - used to determine if we should
	 *              expand nodes or not
	 * @return the node representation of the goal state
	 */
	private static RubikCubeNode search(int bound) {
		numberOfNodesVisited++;

		while (!frontier.isEmpty()) {
			numberOfNodesVisited++;
			RubikCubeNode current = frontier.poll();
			// If goal node is found, return the goal node
			if (Arrays.equals(current.state, RubikCube.SOLVEDSTATE.toCharArray())) {
				return current;
			}
			// Add this current node to explored set
			explored.add(current);
			// Get all of the possible successors from the given node
			ArrayList<RubikCubeNode> successors = RubikCubeNode.getSuccessors(current);
			// Iterate over each of the successors
			for (RubikCubeNode successor : successors) {
				int f = current.g + successor.heuristic;
				successor.g = current.g + 1;
				if (f <= bound && !explored.contains(successor)) {
					// Add it to frontier
					frontier.add(successor);
				}
			}
		}
		// if solution is not found at this bound
		return null;
	}

	/**
	 * Formats the solution. Without this function,a solution would duplicate
	 * turns of the same face eg: O1R1R1R1.
	 * This function will turn that solution into a prettier, O1R3.
	 * @param solution the raw solution
	 * @return a properly formatted optimal solution
	 */
	private static String formatSolution(String solution) {
		try {
			char[] s = solution.toCharArray();
			// Initialize the solution with the beginning 2 characters
			String optimalSolution = solution.substring(0, 2);
			for (int i = 2; i < s.length; i ++) {
				// Add each character to the optimal solution
				optimalSolution += s[i];
				// If the current character is equal to the last character in the string
				// and if i % 2 == 0 we are only comparing characters
				if (s[i] == s[i - 2] && i % 2 == 0) {
					// Get the number to be incremented
					Integer oldNumber = Integer.parseInt(optimalSolution.substring(
							optimalSolution.length() - 2, optimalSolution.length() - 1));
					// Trim the optimal solution to remove the old number
					optimalSolution = optimalSolution.substring(0, optimalSolution.length() - 2);
					// Add the incremented value
					optimalSolution += (oldNumber + 1);
					// increment i so that we skip over the values we just handled
					i++;
				}
			}
			return optimalSolution;
		} catch (Exception e) {
			// If anything abnormal happens while trying to format the string,
			// just return the non-pretty version of it. This is a fail-safe.
			return solution;
		}
	}

	/**
	 * Reads the CSV files and returns an int[]
	 * to load heuristic values from values
	 * @param h the size of the array
	 * @param fileName the name of the CSV file to read from
	 * @return an int[]
	 */
	private static int[] getHeuristics(int h, String fileName) {
		// cornerside heuristics array has 88179840
		// elements, but not all of them will have a value
		// as heuristics are calculated for valid corner
		// positions from goal state rather than all possible
		// permutations of corners.
		int[] heuristics = new int[h];
		FileReader file = null;
		String line;
		try {
			file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			while ((line = reader.readLine()) != null) {
				// For each line, split by the comma
				String[] lineData = line.split(",");
				if (!(lineData[0].equals("") || lineData[1].equals(""))) {
					heuristics[Integer.parseInt(lineData[0])] = Integer.parseInt(lineData[1]);
				}
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found");
		} catch (IOException e) {
			throw new RuntimeException("IO error occurred");
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return heuristics;
	}

	/**
	 * A quick tester for IDA*
	 * @param args
	 */
	public static void main(String[] args) {
		RubikCube rubikCube = new RubikCube("input/cube04");
		System.out.println(rubikCube);
		String result = IDEA_Star.executeIDAStar(rubikCube.state, true);
		System.out.println(result);
	}
}
