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

	public List<Edge> prim(){

		List<Edge> A = new ArrayList<>(); //Conjunto de arestas da MST
		List<Node> U = new ArrayList<>(); //Conjunto de nós da MST

		List<Node> V = this.getNodeList(); //Conjunto de nós existentes no grafo

		U.add(V.get(0)); //Começa a MST de qualquer nó do graph


		while(A.size() < V.size() - 1) {
			List<Edge> subsetNeighborsEdges = new ArrayList<>(); //Caminhos possiveis da MST em formação

			//Lista todos os vizinhos possíveis do subconjunto:
			for(Node u : U) {
				List<Edge> edgesToNeighbor =  u.getEdges();
				for(Edge e : edgesToNeighbor) {
					if(! A.contains(e)) {			//Só são (quase) seguras, arestas externas ao subconjunto
						subsetNeighborsEdges.add(e);
					}
				}
			}

			Edge menor = Collections.min(subsetNeighborsEdges); //Pega aresta de menor peso
			//Se os dois nós ligados pela aresta já não fizerem parte do subconjunto, ela é segura
			if(! (U.contains(menor.getSource()) && U.contains(menor.getTarget())) ) {
				A.add(menor);
			}

			//Se o subconjunto tiver o src, adiciona o tgt e vice e versa.
			if(U.contains(menor.getSource())) {
				U.add(menor.getTarget());
			} else {
				U.add(menor.getSource());
			}
		}


		return A;
	}

	public List<Edge> boruvka() {
	    /*
	        BORUVKA(G):
	            A = ∅
	            foreach v ∈ G.V:
	                MAKE-SET(v)
	            foreach v ∈ G.V:
	                e = min(u, v)
	                if FIND-SET(u) ≠ FIND-SET(v):
	                    A = A ∪ e
	                    UNION(u, v)


	     */


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

	    List<Edge> A = new ArrayList<>();
	    List<Node> V = this.getNodeList();
	    List<Edge> E = this.getEdges();
	    List<List<Node>> subsets = new ArrayList<>();

	    for(Node v : V) {
	    	List<Node> subset = new ArrayList<>();
	    	subset.add(v);
	    	subsets.add(subset);
		}

		Collections.sort(E);

		for(Edge e : E) {
			Node src = e.getSource();
			Node tgt = e.getTarget();
			if(! find(subsets,src).equals(find(subsets,tgt))) {
				A.add(e);
				union(subsets,src,tgt);
			}
		}


	    return A;
	}

	private List<Node> find(List<List<Node>> subsets, Node v) {
		for(List<Node> subset : subsets) {
			for(Node node : subset) {
				if(node.equals(v)) {
					return subset;
				}
			}
		}

		return null;
	}

	private void union(List<List<Node>> subsets, Node v1, Node v2) {
		int first = 0;
		int second = 0;
		for(List<Node> subset : subsets) {
			for(Node node : subset) {
				if(node.equals(v1)) {
					first = subsets.indexOf(subset);
					break;
				}
				if(node.equals(v2)) {
					second = subsets.indexOf(subset);
				}
			}
		}

		subsets.get(first).addAll(subsets.get(second));
		subsets.remove(second);
	}



	
}