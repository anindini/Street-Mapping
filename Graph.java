/*
 * Anindini Singh, asingh37, lab MW 3:25, MEL 210
 * Rebecca Sarin, rsarin, lab TR 2:00, MEL 210
 * Project 3
 */

// Graph implementation adapted from code shown in class

import java.util.HashMap;
import java.util.LinkedList;

public class Graph {
	  private int numVert;
	  private int numEdge;                   // Number of edges
	  public HashMap<String, Node> intersections;
	  public HashMap<Node, LinkedList<Edge>> adjList;
	  public Graph() {
		  intersections = new HashMap<String, Node>();
		  adjList = new HashMap<Node, LinkedList<Edge>>();
		  numVert = 0;
		  numEdge = 0;
	  }
	  public Graph(int n)                   // Constructor
	    { Init(n); }

	  public void Init(int n) {
		intersections = new HashMap<String, Node>(n);
		adjList = new HashMap<Node, LinkedList<Edge>>(n);
	    numVert = 0;
	    numEdge = 0;
	  }

	  public int n() { return numVert; } // # of vertices
	  public int e() { return numEdge; }     // # of edges

	  /** @return v's first neighbor */
	  public Node first(Node v) {
	    if (adjList.get(v).size() == 0)
	      return null;   // No neighbor
	    Edge it = adjList.get(v).getFirst();
	    return it.vertex();
	  }

	 /** @return v's next neighbor after w */
	  public Node next(Node v, Node w) {
	    Edge it = null;
	    if (isEdge(v, w)) {
	    	int wIndex = 0;
	    	for (Edge e : adjList.get(v)) {
	    		if (e.vertex().intersectionID.equals(w.intersectionID)) {
	    			wIndex = adjList.get(v).indexOf(e);
	    		}
	    	}
	    	it = adjList.get(v).get(wIndex + 1);
	    }
	    if (it != null)
	      return it.vertex();
	    return null; // No neighbor
	  }
	  /** Set the weight for an edge */
	  public void setEdge(Node i, Node j, double weight, String roadID) {
	    assert weight != 0 : "May not set weight to 0";
	    Edge currEdge = new Edge(j, weight, roadID);
	    if (isEdge(i, j)) { // Edge already exists in graph
	    	int jIndex = 0;
	    	for (Edge e : adjList.get(i)) {
	    		if (e.vertex().intersectionID.equals(j.intersectionID)) {
	    			jIndex = adjList.get(i).indexOf(e);
	    		}
	    	}
	    	adjList.get(i).remove(jIndex);
	    	adjList.get(i).add(currEdge);
	    }
	    else { // Keep neighbors sorted by vertex index
	      numEdge++;
	      adjList.get(i).add(currEdge);
	    }
	  }

	  /** Delete an edge */
	  public void delEdge(Node i, Node j) {
		  if (isEdge(i, j)) {
			  int jIndex = 0;
			  for (Edge e : adjList.get(i)) {
		    		if (e.vertex().intersectionID.equals(j.intersectionID)) {
		    			jIndex = adjList.get(i).indexOf(e);
		    		}
			  }
			  adjList.get(i).remove(jIndex);
			  // vertex[i].remove();
			  numEdge--;
		  }
	  }

	  /** Determine if an edge is in the graph */
	  public boolean isEdge(Node v, Node w) {
		  if (!adjList.get(v).isEmpty()) {
			  for (Edge it : adjList.get(v)) {
				  if ((it != null) && (it.vertex().intersectionID.equals(w.intersectionID))) {
					  return true;
				  }
			  }
		  }
	    return false;
	  }

	  /** @return an edge's weight */
	  public double weight(Node i, Node j) {
	    if (isEdge(i, j)) {
	    	for (Edge e : adjList.get(i)) {
	    		if (e.vertex().intersectionID.equals(j.intersectionID)) {
	    			return e.weight();
	    		}
	    	}
	    }
	    return 0;
	  }
}
