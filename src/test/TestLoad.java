package test;

import java.util.List;

import graph.Graph;
import graph.Node;
import util.Import;

public class TestLoad {
	
	public static void main(String[] args) throws Exception {
		
		Graph graph = Import.loadGraph("caxeiro");
		
		List<Node> nodes = graph.getNodeList();
		
		for (Node node : nodes) {
			if(nodes.indexOf(node) == nodes.size() - 1) {
				System.out.println(node.getId());
			} else {
				System.out.print(node.getId() + " - ");

			}
		}

		Node target = graph.buscaEmLargura(graph.getNode("arad"),graph.getNode("bucharest"));
		while(target != null) {
			System.out.println(target);
			target = target.getParent();
		}


		
	}

}
