Rubik's Cube Solver
==========

[Kaushik Bhattacharjee](https://github.com/kaushikBh) |
Colorado State University - CS 440 - Artificial Intelligence Project

Problem
-----------
Given any Rubik's Cube state, return a list of moves to solve the Rubik's RubikCube using [Korf's IDEA* Algorithm](http://en.wikipedia.org/wiki/Optimal_solutions_for_Rubik%27s_Cube#Korf.27s_Algorithm).

Solution Steps
-----------
- Represent a 3D rubikCube in a 2D state
- Generate 3 pattern databases: corner cubies, edge cubies set 1, edge cubies set 2
- Perform Iterative Deepening astar (IDA*) search on the possible moves using the 3 above pattern databases as the heuristic look up
- Return an optimal solution in the form of the face to turn and how many clockwise turns to make

Relaxed Constraints
---------------------------
- Handled only clockwise turns of a face

Running the Solver
------------------
Compile the Java source files in src directory:
- `cd src`
- `javac *.java`

To generate the heuristic lookup tables, run below commands in src directory
- `chmod 755 createHeuristics.sh`
-	`./createHeuristics.sh`

To solve a rubikCube from a file run following command:
- `java -Xmx2g RubikCube "input file path"`
