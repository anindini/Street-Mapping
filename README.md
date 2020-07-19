# Street-Mapping
The Road Not Taken

This program implements Dijkstra's algorithm (from one specific Node to another specific Node) to find the shortest path between intersections.
If Dijkstra does not find the destination Node, it would return a Node called DISCONNECTED, and then a statement would be printed saying the two Nodes were not connected.
The Haversine algorithm is used to find the distance between intersections.

The run configurations for this program would differ based on what you'd like to do. But the skeleton would be as follows:-
"java StreetMapping map.txt --show startIntersection endIntersection" to only show the path, while
"java StreetMapping map.txt --show --directions startIntersection endIntersection" to also give directions.

The features, "--show intersection" would highlight a given intersection on the map as a magenta dot, while "--directions startIntersection endIntersection mph" 
would display the time it would take to travel from startIntersection to endIntersection at the given mph. If the same intersection is entered as a start and end point,
a statement would be printed saying that they were the same.
