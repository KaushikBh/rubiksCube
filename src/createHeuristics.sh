# Compile all of the java files
javac RubikCube.java;
javac RubikCubeNode.java;
javac CornerHeuristics.java;
javac EdgeHeuristics.java;
# Run the Heuristics generation class and store the
# output into a csv file
echo "Generate CornerSide heuristics..."
java CornerHeuristics -Xmx2048M > corners.csv;
# Generate the first set of edge heuristics and store
# the output into a csv file
echo "Generate Edge Set One heuristics..."
java EdgeHeuristics 0 -Xmx2048M > edgesSetOne.csv;
# Generate the second set of edge heuristics and store
# the output into a csv file
echo "Generate Edge Set Two heuristics..."
java EdgeHeuristics 1 -Xmx2048M > edgesSetTwo.csv;