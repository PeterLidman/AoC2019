package AoC_18;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private String id;
	private int bestDistance= Integer.MAX_VALUE;
	private boolean done = false;
	private List<Edge> neighbours = new ArrayList<Edge>();

	public String getNodeId() {
		return this.id;
	}

	public int getBestDistance() {
		return bestDistance;
	}

	public void setBestDistance(int bestDistance) {
		this.bestDistance = bestDistance;
	}

	public boolean isDone() {
		return done;		
	}
	
	public void setDone() {
		this.done=true;		
	}
	
	public void addNeighbour(Edge e) {
		if (this.neighbours.contains(e)) {
			System.out.println("This edge has already been used for this node.");
		} else {
//			System.out.println("Successfully added " + e);
			this.neighbours.add(e);
		}
	}

	public String getNeighbours(boolean silent) {
		String ret = "";
		if (!silent) {
			System.out.println("List of all edges that node " + this.id + " has: ");
			System.out.println("=================================");
		}
		for (int i = 0; i < this.neighbours.size(); i++) {
			ret += neighbours.get(i).getIdOfEndNode();

			if (!silent) {
				System.out.println("ID of Edge: " + neighbours.get(i).getId() + " dist" + neighbours.get(i).getWeight()
						+ "\nID of the first node: " + neighbours.get(i).getIdOfStartNode()
						+ "\nID of the second node: " + neighbours.get(i).getIdOfEndNode());
				System.out.println();
			}
		}
		if (!silent) {
			System.out.println(neighbours);
		}
		return ret;
	}
	
	public String getUnvisitedNeighbours(String VisitedNodes) {
		String ret = "";
		for (int i = 0; i < this.neighbours.size(); i++) {
			String candidate= neighbours.get(i).getIdOfEndNode();
			if(!VisitedNodes.contains(candidate)) {
				ret += candidate;
			}
		}
		return ret;
	}

	public Node(String string) {
		this.id = string;
	}

}
