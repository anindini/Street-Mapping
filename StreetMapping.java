/*
 * Anindini Singh, asingh37, lab MW 3:25, MEL 210
 * Rebecca Sarin, rsarin, lab TR 2:00, MEL 210
 * Project 3
 */

// Dijkstra implementation adapted from code found at: https://www.baeldung.com/java-dijkstra
// Haversine implementation adapted from code found at: http://rosettacode.org/wiki/Haversine_formula#Java

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javafx.util.Pair;

public class StreetMapping extends JComponent {
	private static final long serialVersionUID = 1L;
	static Graph map;
	static boolean showDirections = false;
	static LinkedList<Pair<Node, Node>> drawEdges = new LinkedList<Pair<Node, Node>>();
	static Node point = null;
	static double longMin;
	static double longMax;
	static double latMin;
	static double latMax;
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		Graphics2D g2D = (Graphics2D) (g.create());
		double longDiff = longMax - longMin;
		double latDiff = latMax - latMin;
		double a = width/longDiff; // these numbers are used for scaling
		double b = height/latDiff; // these numbers are used for scaling
		for (Entry<Node, LinkedList<Edge>> entry : map.adjList.entrySet()) {	//	iterates through the the adjacency list and draws all roads
			for (Edge edge : entry.getValue()) {
				Node start = entry.getKey();
				Node end = edge.vertex();
				g2D.draw(new Line2D.Double(a*(start.longitude - longMin), b*(start.latitude - latMin), a*(end.longitude - longMin), b*(end.latitude - latMin)));	// appropriate calculations for translating map to make visible 		
			}
		}
		if (showDirections) {	//	path corresponding to the source and destination intersections is displayed in red
			g2D.setColor(Color.RED);
			for (Pair<Node, Node> pair : drawEdges) {
				Node start = pair.getKey();
				Node end = pair.getValue();
				g2D.draw(new Line2D.Double(a*(start.longitude - longMin), b*(start.latitude - latMin), a*(end.longitude - longMin), b*(end.latitude - latMin)));
			}
		}
		//	Extra Credit
		if (point != null) {	//	highlights an intersection in magenta when user inputs intersection id to identify its locations
			g2D.setColor(Color.MAGENTA);
			double x = a*(point.longitude - longMin);
			double y = b*(point.latitude - latMin);
			g2D.fill(new Ellipse2D.Double(x - 5, y - 5, 10, 10));
		}
	}
	public static Graph makeGraph(File input) { // puts all intersections and roads into the graph
		Graph toReturn = new Graph();
		try {
			FileReader fReader = new FileReader(input);
			BufferedReader bReader = new BufferedReader(fReader);
			String str = bReader.readLine();
			while (str != null) {
				String [] item = str.split("\\s+");
				if (item[0].equals("i")) {	//	used to read the intersection information from the text file
					String intersectionID = item[1];
					double latitude = -Double.parseDouble(item[2]);
					if (latitude < latMin) {	//	used to determine the minimum latitude for a given map
						latMin = latitude;
					}
					if (latitude > latMax) {	//	used to determine the maximum latitude for a given map
						latMax = latitude;
					}
					double longitude = Double.parseDouble(item[3]);
					if (longitude < longMin) {	//	used to determine the minimum longitude for a given map
						longMin = longitude;
					}
					if (longitude > longMax) {	//	used to determine the maximum longitude for a given map
						longMax = longitude;
					}
					Node intersection = new Node(intersectionID, latitude, longitude);
					toReturn.intersections.put(intersectionID, intersection);
					toReturn.adjList.put(intersection, new LinkedList<Edge>());
				} else if (item[0].equals("r")) {	// used to read the road information
					String roadID = item[1];
					Node intersection1 = toReturn.intersections.get(item[2]);	//	returns intersections corresponding to the given intersection id 
					Node intersection2 = toReturn.intersections.get(item[3]);
					double distance = haversine(intersection1.latitude, intersection1.longitude, intersection2.latitude, intersection2.longitude);
					toReturn.setEdge(intersection1, intersection2, distance, roadID);
					toReturn.setEdge(intersection2, intersection1, distance, roadID);
				}
				str = bReader.readLine();
			}
			fReader.close();
			bReader.close();
			return toReturn;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return toReturn;
	}
	public static double haversine(double lat1, double lon1, double lat2, double lon2) {	//	 haversine formula, used to calculate (in miles) the distance between two intersections on the map
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return 3958.756 * c;
    }
	private static void calculateMinDist(Node evaluationNode, double edgeWeight, Node sourceNode) {	// adjusts the distance of the evaluation node, and its predecessor, when appropriate
	    double sourceDistance = sourceNode.distance;
	    if (sourceDistance + edgeWeight < evaluationNode.distance) {
	        evaluationNode.setDistance(sourceDistance + edgeWeight);
	        evaluationNode.setPredecessor(sourceNode);
	    }
	}
	public static Node dijkstra(Node start, Node end) {	//	used to compute the shortest path between the source and destination nodes
		PriorityQueue<Node> unsettledNodes = new PriorityQueue<Node>();	//	priority queue of nodes not visited
		Set<Node> settledNodes = new HashSet<>();	//	set of visited nodes
		start.setDistance(0);	//	distance of source is set to 0
		unsettledNodes.add(start);	//	the start node is first added to the priority queue
	    while (unsettledNodes.size() != 0) {
	        Node currentNode = unsettledNodes.poll();	//	the current node is the node returned and removed from the priority queue	
	        settledNodes.add(currentNode);	//	 since the current node had been visited is is added to the set of settled nodes
	        if (currentNode.intersectionID.equals(end.intersectionID)) {	//	if the destination node has been reached, it is returned
	        	return end;
	        }
	        for (Edge e : map.adjList.get(currentNode)) {	//	iterate through the edges of the adjacency list
	        	Node adjacentNode = e.vertex();
	        	double edgeWeight = e.weight();
	        	if (!settledNodes.contains(adjacentNode)) {
	        		calculateMinDist(adjacentNode, edgeWeight, currentNode);	//	calculate the minimum distance between the current and adjacent nodes
	                unsettledNodes.add(adjacentNode);
	        	}
	        }
	    }
	    if (end.predecessor == null) {	//	to determine whether a destination node is unreachable
	    	return new Node("DISCONNECTED", 0, 0);
	    }
	    return end;
	}
	public static double giveDirections(Node start, Node end) {	// navigate from the source to the destination node
		String directions = "";
		double totalDistance = 0;
		//	Extra Credit
		if (start.intersectionID.equals(end.intersectionID)) {	//	 if user input the same source and destination intersections
			return 0;
		}
		Node destination = dijkstra(start, end);	//	if the destination node can't be reached
		if (destination.intersectionID.equals("DISCONNECTED")) {
			return -1;
		}
		Node current = destination;	//	gives directions for path from the source to the destination intersections when they are both unique
		while (!current.intersectionID.equals(start.intersectionID)) {
			Node previous = current.predecessor;
			drawEdges.add(new Pair<Node, Node>(previous, current)); // adds the edge to the list of edges to be shown in red
			Edge xyEdge = null;
			for (Edge e : map.adjList.get(previous)) {
	    		if (e.vertex().intersectionID.equals(current.intersectionID)) {
	    			xyEdge = e;
	    		}
			}
			if (xyEdge != null) {
				totalDistance += xyEdge.weight();
				directions = "Take road " + xyEdge.id() + " from " + previous.intersectionID + " to " + current.intersectionID + ".\n" + directions;
			}
			current = previous;
		}
		System.out.println(directions);
		return totalDistance;
	}
	public static void main(String [] args) {
		File file = new File(args[0]);
		longMin = 180;
		longMax = -180;
		latMin = 90;
		latMax = -90;
		map = makeGraph(file);
		Node start;
		Node end;
		boolean show = false;
		if (args[1].equals("--show")) {	//	when user enters "show" to merely show the map, or to show directions
			show = true;
			if (args.length > 2 && args[2] != null) {	
				if (args[2].equals("--directions")) {	//	when user enters "directions" for the path between two intersections on a map
					showDirections = true;
					start = map.intersections.get(args[3]);
					end = map.intersections.get(args[4]);
					double distance = giveDirections(start, end);
					if (distance == 0) {
						System.out.println(args[3] + " and " + args[4] + " are the same intersection.");
					} else if (distance < 0) {
						System.out.println(args[3] + " and " + args[4] + " are not connected.");
					} else {
						System.out.println("Total distance traveled: " + distance + " miles");
					}
					// Extra credit
					if (args.length > 5 && args[5] != null) {	//	provide the time taken to reach a destination from the source when the user enter the speed of travel in miles per hour
						int mph = Integer.parseInt(args[5]);
						double time = distance/mph;
						System.out.println("Travel time: " + time + " hours");
					}
				} else { // show a point in magenta on the map
					Node intersection = map.intersections.get(args[2]);
					point = intersection;
				}
			}
		} else if (args[1].equals("--directions")) {	//	if show and directions are entered in reverse order
			if (args[2].equals("--show")) {
				show = true;
				showDirections = true;
				start = map.intersections.get(args[3]);
				end = map.intersections.get(args[4]);
				double distance = giveDirections(start, end);
				if (distance == 0) {
					System.out.println(args[3] + " and " + args[4] + " are the same intersection.");
				} else if (distance < 0) {
					System.out.println(args[3] + " and " + args[4] + " are not connected.");
				} else {
					System.out.println("Total distance traveled: " + distance + " miles");
				}
				// Extra credit
				if (args.length > 5 && args[5] != null) {
					int mph = Integer.parseInt(args[5]);
					double time = distance/mph;
					System.out.println("Travel time: " + time);
				}
			} else { // if the user only wants directions and no map
				start = map.intersections.get(args[2]);
				end = map.intersections.get(args[3]);
				double distance = giveDirections(start, end);
				if (distance == 0) {
					System.out.println(args[2] + " and " + args[3] + " are the same intersection.");
				} else if (distance < 0) {
					System.out.println(args[2] + " and " + args[3] + " are not connected.");
				} else {
					System.out.println("Total distance traveled: " + distance + " miles");
				}
				// Extra credit
				if (args.length > 4 && args[4] != null) {
					int mph = Integer.parseInt(args[4]);
					double time = distance/mph;
					System.out.println("Travel time: " + time + " hours");
				}
			}
		}
		if (show) {
			StreetMapping canvas = new StreetMapping();
			JFrame frame = new JFrame("Map");
			frame.add(canvas);
			frame.setSize(400, 400);	//	default frame size
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
	}
}
