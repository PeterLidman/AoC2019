package AoC_18;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	private List<Node> nodes = new ArrayList<Node>();
	private int numberOfNodes = 0;

	public boolean checkForAvailability() {
		return this.numberOfNodes > 1;
	}

	public Node getNode(String id) {
		Node ret = null;
		for (Node n : nodes) {
			if (n.getNodeId().equals(id)) {
				return n;
			}
		}
		return ret;
	}

	public void createNode(Node node) {
		this.nodes.add(node);
		this.numberOfNodes++;
	}

	public int getNumberOfNodes() {
		return this.numberOfNodes;
	}

}
