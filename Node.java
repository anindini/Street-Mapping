/*
 * Anindini Singh, asingh37, lab MW 3:25, MEL 210
 * Rebecca Sarin, rsarin, lab TR 2:00, MEL 210
 * Project 3
 */

import java.util.LinkedList;

public class Node implements Comparable<Node>{
	String intersectionID;
	double latitude;
	double longitude;
	double distance;
	Node predecessor;	//	parent of a node
	LinkedList<Node> shortestPath;
	public Node(String id, double a, double b) {
		intersectionID = id;
		latitude = a;
		longitude = b;
		distance = Integer.MAX_VALUE;	// distance of node initialised to infinity
		predecessor = null;
		shortestPath = new LinkedList<Node>();
		shortestPath.add(this);
	}
	public void setDistance(double d) {
		distance = d;
	}
	public void setPredecessor(Node n) {
		predecessor = n;
	}
	public void setShortestPath(LinkedList<Node> path) {
		shortestPath = path;
	}
	public LinkedList<Node> getShortestPath() {
		return shortestPath;
	}
	public int compareTo(Node other) {	
		if (distance < other.distance) {
			return -1;
		} else if (distance > other.distance) {
			return 1;
		}
		return 0;
	}
}
