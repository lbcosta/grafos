package graph;

import java.util.*;

public class Graph {
	
	protected String dataSetName;
	
	protected HashMap<String, Node> nodeHash = new HashMap<String, Node>();
	
	protected List<Edge> edges = new ArrayList<Edge>();

	public Graph(){

    }
	
	public Graph(String datasetName) {
		super();
		this.dataSetName = datasetName;
	}

	public Node addNode(Node node) {
        Node v = nodeHash.get(node.getId());

        if(v == null){
            this.nodeHash.put(node.getId(),node);
        }

        return v;
    }

	public Node addNode(String id, Float weight, String label){
		
		Node v = nodeHash.get(id);
		
		if(v == null){
			
			v = new Node(id,weight,label);
		
			this.nodeHash.put(id, v);
			
		}
		
		return v;
	}
	
	public Edge addEdge(Node source, Node target, Float weight){		
			
		Edge a = new Edge(source, target, weight);
		
		source.addNeighbor(a);
		
		target.addNeighbor(a);
		
		edges.add(a);		
		
		return a;
		
	}
	
	public Integer size(){
		return new Integer(getNodeList().size());
	}
	
	public Node getHub(){
		List<Node> nodes = this.getNodeList();
		
		Collections.sort(nodes);
		Collections.reverse(nodes);
		
		return nodes.get(0);		
	}

	public String getDatasetName() {
		return dataSetName;
	}

	public void setDatasetName(String datasetName) {
		this.dataSetName = datasetName;
	}
	
	public List<Node> getNodeList(){
		List<Node> lista = new ArrayList<Node>();
		
		Set<String> chaves =  nodeHash.keySet();
		
		for (String chave : chaves) {
			Node v = nodeHash.get(chave);
			
			lista.add(v);
		}
		
		return lista;
	}
	
	public Node getNode(String id){
		return nodeHash.get(id);
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public Node buscaEmLargura(Node source, Node target) throws CloneNotSupportedException {
		LinkedList<Node> queue = new LinkedList<>();
		queue.add(source);
		while(! queue.isEmpty()) {
			Node actual = queue.pop();
			if(actual.equals(target)) {
				return actual;
			} else {
				List<Node> children = actual.getNeighbors();
				for(Node child : children) {
					if(!child.equals(actual.getParent())) {
						Node childCopy = (Node) child.clone();

						childCopy.setParent(actual);
						queue.add(childCopy);
					}
				}
			}
		}

		return null;
	}

	public Node buscaEmProfundidade(Node source, Node target) throws CloneNotSupportedException {
		Stack<Node> stack = new Stack<>();
		stack.push(source);
		while(! stack.isEmpty()) {
			Node actual = stack.pop();
			if(actual.equals(target)) {
				return actual;
			} else {
				List<Node> children = actual.getNeighbors();
				for(Node child : children) {
					if(!child.equals(actual.getParent())) {
						Node childCopy = (Node) child.clone();

						childCopy.setParent(actual);
						stack.add(childCopy);
					}

				}
			}
		}

		return null;
	}

	public Node buscaDeCustoUniforme(Node source, Node target) throws CloneNotSupportedException {
		ArrayList<Node> list = new ArrayList<>();
		list.add(source);
		while(! list.isEmpty()) {
			Collections.sort(list);
			Node actual = list.remove(0);
			if(actual.equals(target)) {
				return actual;
			} else {
				List<Node> children = actual.getNeighbors();
				for(Node child : children) {
					if(! child.equals(actual.getParent())) {
						Node childCopy = (Node) child.clone();

						childCopy.setParent(actual);

						Edge edgeToParent = childCopy.getEdgeWith(actual);
						childCopy.setG(actual.getG() + edgeToParent.getWeight());

						list.add(childCopy);
					}
				}
			}
		}

		return null;
	}

	public Node dijkstra(Node source, Node target) throws CloneNotSupportedException {
		//1º PASSO: TODOS OS G's são INFINITOS, MENOS O DO NÓ INICIAL
		for(Node node : this.getNodeList()) {
			node.setG(Float.POSITIVE_INFINITY);
		}
		this.getNode(source.getId()).setG(Float.valueOf(0));

		//2º PASSO: SUBCONJUNTO DE NÓS QUE AINDA NÃO TIVERAM SUAS ARESTAS RELAXADAS
		List<Node> list = this.getNodeList();

		//3º PASSO:
		while(! list.isEmpty()) {
			Collections.sort(list);
			Node actual = list.remove(0);
			if(actual.equals(target)) {
				return actual;
			} else {
				List<Node> children = actual.getNeighbors();
				for(Node child : children) {
					Node childCopy = (Node) child.clone();
					float weightToFather = actual.getEdgeWith(childCopy).getWeight();

					if(childCopy.getG() + weightToFather > actual.getG()) {

						childCopy.setG(actual.getG() + weightToFather);

						childCopy.setParent(actual);

						list.add(childCopy);
					}
				}
			}
		}

		return null;
	}

	public List<Edge> kruskal() {
	    /*
	    KRUSKAL(G):
            1 A = ∅
            2 foreach v ∈ G.V:
            3    MAKE-SET(v)
            4 foreach (u, v) in G.E ordered by weight(u, v), increasing:
            5    if FIND-SET(u) ≠ FIND-SET(v):
            6       A = A ∪ {(u, v)}
            7       UNION(u, v)
            8 return A
	     */

        List<Edge> MST = new ArrayList<>();
        List<List<Node>> forest = new ArrayList<>();
        for(Node graphNode : this.getNodeList()) {
            List<Node> tree = new ArrayList<>();
            tree.add(graphNode);
            forest.add(tree);
        }

        //Lista de arestas do menor para maior peso.
        List<Edge> possibleEdges = this.getEdges();
        Collections.sort(possibleEdges);

        for(Edge possibleEdge : possibleEdges) {
            Node v1 = possibleEdge.getSource();
            Node v2 = possibleEdge.getTarget();

            for(List<Node> tree : forest) {
                if(tree.contains(v1) ^ tree.contains(v2)) {
                    MST.add(possibleEdge);

                }
            }


        }





	    return MST;
	}



	
}