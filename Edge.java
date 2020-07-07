/*
 * Anindini Singh, asingh37, lab MW 3:25, MEL 210
 * Rebecca Sarin, rsarin, lab TR 2:00, MEL 210
 * Project 3
 */

// Edge implementation adapted from code shown in class

public class Edge {
	private double wt;
	private Node vert;
	private String roadID;

	public Edge(Node v, double w, String x) // Constructor
	    { vert = v;  wt = w; roadID = x; }

	public Node vertex() { return vert; }
	public double weight() { return wt; }
	public String id() { return roadID; }
}
