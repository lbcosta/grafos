package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node implements Comparable<Node>, Cloneable{
	
	private String id;
	
	private Float weight;
	
	private String label;
	
	private Node parent;
	
	private Float g;
	
	private List<Edge> edges = new ArrayList<Edge>();
	
	private HashMap<String, Object> otherAttributes = new HashMap<String, Object>();
	
	public Node(){
		
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Node(String id, Float weight) {
		super();
		this.id = id;
		this.weight = weight;
		this.g = Float.valueOf(0);
	}
	
	public Node(String id, Float weight, String label) {
		super();
		this.id = id;
		this.weight = weight;
		this.label = label;
		this.g = Float.valueOf(0);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<Edge> getEdges() {
		return edges;
	}
	
	public List<Edge> getEdgesOut() {
		
		List<Edge> edgesOut = new ArrayList<Edge>();
		
		for (Edge edge : edges) {
			if(edge.getSource().getId().equals(this.getId())){
				edgesOut.add(edge);
			}
		}
		
		return edgesOut;
	}
	
	public List<Edge> getEdgesIn() {
		List<Edge> edgesIn = new ArrayList<Edge>();
		
		for (Edge edge : edges) {
			if(edge.getTarget().getId().equals(this.getId())){
				edgesIn.add(edge);
			}
		}
		
		return edgesIn;
	}

	public HashMap<String, Object> getOtherAttributes() {
		return otherAttributes;
	}

	public void setOtherAttributes(HashMap<String, Object> otherAttributes) {
		this.otherAttributes = otherAttributes;
	}

	public List<Node> getNeighbors() {
		
		List<Node> neighbors = new ArrayList<Node>();
		
		for (Edge edge : edges) {
			if(edge.getSource().getId().equals(this.getId())){
				neighbors.add(edge.getTarget());
			}else{
				neighbors.add(edge.getSource());
			}
		}
		
		return neighbors;
	}
	
	public List<Node> getNeighborsOut() {
		
		List<Node> neighbors = new ArrayList<Node>();
		
		for (Edge edge : edges) {
			if(edge.getSource().equals(this))
				neighbors.add(edge.getTarget());
			
		}
		
		return neighbors;
	}
	
	public List<Node> getNeighborsIn() {
		
		List<Node> neighbors = new ArrayList<Node>();
		
		for (Edge edge : edges) {
			if(edge.getTarget().equals(this))
				neighbors.add(edge.getSource());
		}
		
		return neighbors;
	}
	
	public Integer getDegree(){
		return edges.size();
	}

	public void setNeighbors(List<Edge> neighbors) {
		this.edges = neighbors;
	}
	
	public boolean isNeighbor(Node neighbor){
		
		for (Edge a : edges) {
			if(a.getSource().getId().equals(neighbor.getId()) || a.getTarget().getId().equals(neighbor.getId())){
				return true;
			}
		}
		
		return false;
	}
	
	public Edge isSourceOf(Node neighbor){
		for (Edge a : edges) {
			if(a.getSource().getId().equals(this.getId()) && a.getTarget().getId().equals(neighbor.getId())){
				return a;
			}
		}
		
		return null;
	}
	
	public Edge isTargetOf(Node neighbor){
		for (Edge a : edges) {
			if(a.getSource().getId().equals(neighbor.getId()) && a.getTarget().getId().equals(this.getId())){
				return a;
			}
		}
		
		return null;
	}
	
	public Edge getEdgeWith(Node neighbor){
		for (Edge a : edges) {
			if(a.getSource().getId().equals(neighbor.getId()) || a.getTarget().getId().equals(neighbor.getId())){
				return a;
			}
		}
		
		return null;
	}
	
	public void addNeighbor(Edge a){
		this.edges.add(a);
	}
	
	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Float getG() {
		return g;
	}

	public void setG(Float g) {
		this.g = g;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null) {
			return this.id.equals(((Node)obj).getId());
		} else {
			return false;
		}

	}
	
	@Override
	public String toString() {		
		return id;
	}

	@Override
	public int compareTo(Node o) {		
		return getG().compareTo(o.getG());
	}
	
}
