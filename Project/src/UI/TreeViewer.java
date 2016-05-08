package UI;

import java.net.URL;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.view.Viewer;

public class TreeViewer {
	public TreeViewer(){
		
	}
	
	public void run(){
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new MultiGraph("Tutorial 1");
		URL location = TreeViewer.class.getProtectionDomain().getCodeSource().getLocation();
		graph.addAttribute("ui.stylesheet", "url('file:///" + location.getFile() + "../styles/test.css')");
		Viewer viewer = graph.display();
		graph.addNode("A").addAttribute("ui.label", "A");
		graph.addNode("B").addAttribute("ui.label", "B");
		graph.addNode("C").addAttribute("ui.label", "C");
		graph.addNode("D").addAttribute("ui.label", "D");
		graph.addNode("E").addAttribute("ui.label", "E");
		graph.addNode("F").addAttribute("ui.label", "F");
		graph.addNode("G").addAttribute("ui.label", "G");
		graph.addNode("H").addAttribute("ui.label", "H");
		graph.addEdge("AB", "A", "B");
		graph.addEdge("AC", "A", "C");
		graph.addEdge("CB", "C", "B");
		graph.addEdge("AD", "A", "D");
		graph.addEdge("AE", "A", "E");
		graph.addEdge("FG", "F", "G");
		graph.addEdge("FE", "F", "E");
		graph.addEdge("GA", "G", "A");
		graph.addEdge("GC", "G", "C");
		graph.addEdge("BD", "B", "D");
		graph.addEdge("BG", "B", "G");
		graph.addEdge("HC", "H", "C");
		graph.addEdge("HD", "H", "D");
		graph.addEdge("HF", "H", "F");
		graph.addEdge("CF", "C", "F");
	}
}
