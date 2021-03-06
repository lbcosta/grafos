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

	public void addEdge(Edge edge) {
		this.edges.add(edge);
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

	public boolean isStronglyConnected() throws CloneNotSupportedException {
		for(Node src : this.getNodeList()) {
			for(Node tgt : this.getNodeList()) {
				if(!src.equals(tgt)) {
					Node dijkstraTarget = this.dijkstra(src,tgt);
					if(tgt.equals(dijkstraTarget)) {
						return false;
					}
				}
			}
		}
		return true;
	}


	public List<Edge> prim(){

		List<Edge> MSTEdges = new ArrayList<>(); //Conjunto de arestas da MST
		List<Node> MSTNodes = new ArrayList<>(); //Conjunto de nós da MST

		List<Node> graphNodes = this.getNodeList(); //Todos os nós do grafo

		MSTNodes.add(graphNodes.get(0)); //Começa a MST de qualquer nó do grafo


		while(MSTEdges.size() < graphNodes.size() - 1) { //Algoritmo para quando Nº de arestas do MST for igual ao Nº de nós do grafo menos 1

		    //Pega todas as arestas que não fazem parte da MST
		    List<Edge> subsetNeighborsEdges = getPossibleEdges(MSTEdges, MSTNodes);

            //Pega aresta de menor peso
            Edge menor = Collections.min(subsetNeighborsEdges);

			//Se os dois nós ligados pela aresta não fizerem parte do subconjunto, ela é segura
			if(! (MSTNodes.contains(menor.getSource()) && MSTNodes.contains(menor.getTarget())) ) {
				MSTEdges.add(menor);
			}

			//Se o subconjunto tiver o source, adiciona o target a ele e vice e versa.
			if(MSTNodes.contains(menor.getSource())) {
				MSTNodes.add(menor.getTarget());
			} else {
				MSTNodes.add(menor.getSource());
			}
		}


		return MSTEdges;
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

	    List<Edge> MSTEdges = new ArrayList<>();
	    List<Node> graphNodes = this.getNodeList();
	    List<Edge> graphEdges = this.getEdges();

	    List<List<Node>> subsets = new ArrayList<>();
        fillSubsets(graphNodes, subsets); //Um subset para cada nó

        Collections.sort(graphEdges); //Organiza arestas do menor peso para maior peso

		for(Edge e : graphEdges) {
			Node src = e.getSource();
			Node tgt = e.getTarget();
			if(! find(subsets,src).equals(find(subsets,tgt))) { //Se não pertecem ao mesmo subset, a aresa é válida
				MSTEdges.add(e);
				union(subsets,src,tgt);
			}
		}

		System.out.println("Número de Componentes conexos: " + subsets.size());
	    return MSTEdges;
	}

    public List<Edge> boruvka() {
	    List<Node> graphNodes = this.getNodeList(); //Nós do grafo
	    List<Edge> MSTEdges = new ArrayList<>();  //Arestas da MST

        List<List<Node>> subsets = new ArrayList<>();
        fillSubsets(graphNodes, subsets); //Um subset para cada nó

        for(Node v : graphNodes) { //Fase 1 - Para cada vertice:
            List<Edge> edges = v.getEdges(); //Pega todas as arestas do vertice
            Edge menor = Collections.min(edges); //Pega a menor de menor peso
            if(! MSTEdges.contains(menor)) { //Se ainda não está na MST, adiciona e une os subsets
                MSTEdges.add(menor);
                union(subsets,v,menor.opposite(v));
            }
        }

        while(MSTEdges.size() < graphNodes.size() - 1) { //Fase 2 - Para cada subset:
            Edge menor = getSafeEdge(subsets,MSTEdges); //Pega a menor aresta válida (Que não pertence a MST e não une nós de um mesmo subset)

            union(subsets, menor.getSource(), menor.getTarget()); //Une os grupos das duas pontas da menor aresta válida
            MSTEdges.add(menor); //Adiciona a aresta a MST
        }


        return MSTEdges;
    }

    public float fordFulkerson(Node source, Node target, boolean showSteps) throws CloneNotSupportedException {
		float maxFlow = 0;
		this.setResidualGraph();
		ArrayList<Edge> path = this.getPath(source,target);
		return fordFulkersonRecursion(source, target, showSteps, maxFlow, path);
	}

	public float fordFulkersonRecursion(Node source, Node target, boolean showSteps, float maxFlow, ArrayList<Edge> path) throws CloneNotSupportedException{
		if(path == null) {
			return maxFlow;
		} else {
			float minResidualCapacity = this.minResidualCapacity(path); 	//Pega a menor capacidade residual daquele caminho
			for(Edge edge : path) {											//Muda o fluxo de cada uma das arestas
				if(edge.isBackward) {
					edge.increaseWeight(minResidualCapacity);				//Se for aresta de retorno, ela ganha delta (diminui fluxo)
				} else {
					edge.decreaseWeight(minResidualCapacity);				//Se for aresta normal, ela perde delta (aumenta fluxo)
					edge.setFlow(minResidualCapacity + edge.getFlow());		//Ajustando o atributo de fluxo
					boolean edgeHaveBackward = edge.getSource().isTargetOf(edge.getTarget()); //Checa se a aresta possui um par de retorno
					if(edgeHaveBackward) {
						Edge backward = edge.getTarget().getEdgeWith(edge.getSource()); //Se possuir,ajusta o fluxo dele
						backward.increaseWeight(minResidualCapacity);
					} else {
						Edge backward = new Edge(edge.getTarget(),edge.getSource(),edge.getFlow()); //Se não, cria um.
						backward.isBackward = true;
						this.addEdge(backward);
					}
				}
			}
			maxFlow += minResidualCapacity;	//Aumenta o fluxo total pela capacidade residual

			if(showSteps) { //Mostra os fluxos nas arestas e os caminhos que o algoritmo andou
				for(Edge edge : path) {
					System.out.println(edge + " - " + edge.getOtherAttributes());
				}
				System.out.println("Total Flow: " + maxFlow);
				System.out.println("\n-----");
			}

			return fordFulkersonRecursion(source, target, showSteps, maxFlow, this.getPath(source,target)); //Procura proximo caminho
		}
	}

	private void setResidualGraph() {
		for(Edge edge : this.getEdges()) {
			edge.setWeight(
					Float.valueOf((String) edge.getOtherAttributes().get("capacity"))
							- Float.valueOf((String) edge.getOtherAttributes().get("flow"))
			);
		}
	}

	private ArrayList<Edge> getPath(Node source, Node target) throws CloneNotSupportedException {
		ArrayList<Edge> pathEdges = new ArrayList<>();

		//Faz uma busca em largura:
		Stack<Node> stack = new Stack<>();
		Node actual = null;
		stack.push(source);
		while(! stack.isEmpty()) {
			actual = stack.pop();
			if(actual.equals(target)) {
				break;
			} else {
				List<Node> children = actual.getNeighborsOut();
				for(Node child : children) {
					Edge edgeToChild = actual.getEdgeWith(child);
					boolean edgeIsValid = edgeToChild.isBackward ?
							edgeToChild.getWeight() > 0
							: edgeToChild.getResidualCapacity() > 0;
					if(!child.equals(actual.getParent()) && edgeIsValid) {
						Node childCopy = (Node) child.clone();
						childCopy.setParent(actual);
						stack.add(childCopy);
					}

				}
			}
		}
		//Coloca o caminho em uma lista
		while(! actual.equals(source)) {
			Node parent = actual.getParent();
			pathEdges.add(parent.getEdgeWith(actual));
			actual = actual.getParent();
		}
		Collections.reverse(pathEdges);

		//O caminho so eh valido se chegou no sumidouro
		return this.isTargetReached(pathEdges, target) ? pathEdges : null;
	}

	private boolean isTargetReached(ArrayList<Edge> pathEdges, Node target) {
		boolean isReached = false;

		for(Edge edge : pathEdges) {
			isReached = (edge.getTarget().equals(target));
		}

		return isReached;
	}

	private float minResidualCapacity(List<Edge> path) {
		float minResidualCapacity = Float.POSITIVE_INFINITY;
		for(Edge edge : path) {
			if(edge.getWeight() < minResidualCapacity) {
				minResidualCapacity = edge.getWeight();
			}
		}
		return minResidualCapacity;
	}

    private Edge getSafeEdge(List<List<Node>> subsets, List<Edge> A) {
        List<Node> subset = subsets.get(0);

        List<Edge> E = new ArrayList<>();

        for(Node v : subset) {
            List<Edge> possibleEdges = v.getEdges();
            for(Edge e : possibleEdges) {
                boolean pertecemAoMesmoGrupo =  find(subsets, e.getSource()).equals(find(subsets, e.getTarget()));
                if(! (A.contains(e) && pertecemAoMesmoGrupo)) {
                    E.add(e);
                }
            }
        }

        return Collections.min(E);
    }

    private List<Edge> getPossibleEdges(List<Edge> A, List<Node> subset) {
        List<Edge> possibleEdges = new ArrayList<>();
	    for(Node v : subset) {
            List<Edge> E = v.getEdges();
            for(Edge e : E) {
                if(! A.contains(e)) {
                    possibleEdges.add(e);
                }
            }
        }

        return possibleEdges;
    }

    private void fillSubsets(List<Node> graphNodes, List<List<Node>> subsets) {
        for (Node v : graphNodes) {
            List<Node> subset = new ArrayList<>();
            subset.add(v);
            subsets.add(subset);
        }
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

//Ford-Fulkerson sem recursão:
//    public float fordFulkerson(Node source, Node target) throws CloneNotSupportedException {
//		float maxFlow = 0;
//		this.setResidualGraph();
//		ArrayList<Edge> path = this.getPath(source,target);
//		while(path != null) {
//			float minResidualCapacity = this.minResidualCapacity(path);
//			maxFlow += minResidualCapacity;
//			for(Edge edge : path) {
//				if(edge.isBackward) {
//					edge.setWeight(edge.getWeight() + minResidualCapacity);
//				} else {
//					edge.setWeight(edge.getWeight() - minResidualCapacity);
//					edge.setFlow(String.valueOf(minResidualCapacity + edge.getFlow()));
//					if(edge.getSource().isTargetOf(edge.getTarget())) {
//						Edge backward = edge.getTarget().getEdgeWith(edge.getSource());
//						backward.setWeight(backward.getWeight() + minResidualCapacity);
//					} else {
//						Edge backward = new Edge(edge.getTarget(),edge.getSource(),edge.getFlow());
//						backward.isBackward = true;
//						this.addEdge(backward);
//					}
//				}
//			}
//
//			for(Edge edge : path) {
//				System.out.println(edge + " - " + edge.getOtherAttributes());
//			}
//			System.out.println("Total Flow: " + maxFlow);
//			System.out.println("\n-----");
//
//
//			path = this.getPath(source,target);
//		}
//
//
//		return maxFlow;
//	}
