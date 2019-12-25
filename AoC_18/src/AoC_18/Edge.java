package AoC_18;

public class Edge {
	private Node start;
	private Node end;
	private int weight;
	private int id;

	public int getId() {
		return this.id;
	}

	public Node getStart() {
		return this.start;
	}

	public String getIdOfStartNode() {
		return this.start.getNodeId();
	}

	public Node getEnd() {
		return this.end;
	}

	public String getIdOfEndNode() {
		return this.end.getNodeId();
	}

	public int getWeight() {
		return this.weight;
	}

	public Edge(Node s, Node e, int w, int id) {
		this.start = s;
		this.end = e;
		this.weight = w;
		this.id = id;
	}

}
