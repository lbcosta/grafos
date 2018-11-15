package test;

import java.util.Collections;
import java.util.List;

import graph.Graph;
import graph.Node;
import util.Import;

public class TestLoad {
	
	public static void main(String[] args) throws Exception {
		
		Graph graph = Import.loadGraph("testeFF2");
		
		List<Node> nodes = graph.getNodeList();
		System.out.print("Nodes: ");
		for (Node node : nodes) {
			if(nodes.indexOf(node) == nodes.size() - 1) {
				System.out.println(node.getId());
			} else {
				System.out.print(node.getId() + " - ");

			}
		}
//		System.out.println("\nBusca em Largura:");
//		Node targetLargura = graph.buscaEmLargura(graph.getNode("s"),graph.getNode("t"));
//		while(targetLargura != null) {
//			System.out.println(targetLargura);
//			targetLargura = targetLargura.getParent();
//		}
//
//		System.out.println("\nBusca em Profundidade:");
//		Node targetProfundidade = graph.buscaEmProfundidade(graph.getNode("arad"),graph.getNode("bucharest"));
//		while(targetProfundidade != null) {
//			System.out.println(targetProfundidade);
//			targetProfundidade = targetProfundidade.getParent();
//		}
//
//		System.out.println("\nBCU:");
//		Node targetBCU = graph.buscaDeCustoUniforme(graph.getNode("sibiu"),graph.getNode("bucharest"));
//		while(targetBCU != null) {
//			System.out.print(targetBCU + " - ");
//			System.out.println(targetBCU.getG());
//			targetBCU = targetBCU.getParent();
//		}
//
//		System.out.println("\nDijkstra:");
//		Node targetDijkstra = graph.dijkstra(graph.getNode("arad"),graph.getNode("bucharest"));
//		while(targetDijkstra != null) {
//			System.out.print(targetDijkstra + " - ");
//			System.out.println(targetDijkstra.getG());
//			targetDijkstra = targetDijkstra.getParent();
//		}

//		System.out.println("\nKruskal:");
//		System.out.println(graph.kruskal());

//		System.out.println("\nPrim:");
//		System.out.println(graph.prim());

//		System.out.println("\nBoruvka:");
//		System.out.println(graph.boruvka());

		/*
		[lugoj->mehadia, zerind->oradea, arad->zerind, mehadia->dobreta, sibiu->rimmicu vilcea, rimmicu vilcea->pitest, sibiu->fagaras, pitest->bucharest, timisoara->lugoj, arad->timisoara, dobreta->craiova, craiova->pitest]
		 */


		System.out.println("\nFord-Fulkerson");
		System.out.println("Fluxo Máximo: " + graph.fordFulkerson(graph.getNode("S"), graph.getNode("T"), true));
	}

}
