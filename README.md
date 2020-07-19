# Street-Mapping
The Road Not Taken

This program implements Dijkstra's algorithm (from one specific Node to another specific Node) to find the shortest path between intersections.
If Dijkstra does not find the destination Node, it returns a Node called DISCONNECTED, and then a statement is printed saying the two Nodes are not connected.
The Haversine algorithm is used to find the distance between intersections.

The run configurations for this program differ based on what you'd like to do. But the skeleton is as follows:-
"java StreetMapping map.txt --show startIntersection endIntersection" to only show the path, while
"java StreetMapping map.txt --show --directions startIntersection endIntersection" to also give directions.

The features, "--show intersection" highlight a given intersection on the map as a magenta dot, while "--directions startIntersection endIntersection mph" 
display the time it taken to travel from startIntersection to endIntersection at the given mph. If the same intersection is entered as a start and end point,
a statement is printed saying that they are the same.
