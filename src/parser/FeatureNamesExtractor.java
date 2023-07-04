package parser;

import functionalityNodes.ComponentNode;
import functionalityNodes.ExternalNode;
import functionalityNodes.FunctionalNode;
import functionalityNodes.NodeAggregator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;


public class FeatureNamesExtractor {

	private NodeAggregator nodeAggregator;
	
	public FeatureNamesExtractor(NodeAggregator nodeAggregator) {
		this.nodeAggregator = nodeAggregator;
	}
	
	public List<String> extractAllNodeNames() {
		List<String> extractedNodeNames = new ArrayList<String>();
		Iterator<String> nodeNames = this.nodeAggregator.getNodeNames().iterator();
		while(nodeNames.hasNext()) {
			String nodeName = nodeNames.next();
			String readableNodeName = FeatureNamesExtractor.makeNameReadable(nodeName);
			extractedNodeNames.add(readableNodeName);
		}
		return extractedNodeNames;
	}
	
	public static String makeNameReadable(String nodeName) {
		String readableNodeName = nodeName.replaceAll("([A-Z])", " $1").strip();
		readableNodeName = readableNodeName.replaceAll("(Module$|Service$|Directive$|Component$)", "");
		return readableNodeName;	
	}

	public List<String> extractNodeNames() {
		List<String> extractedNodeNames = new ArrayList<String>();

		for(Map.Entry<String, FunctionalNode> entry: this.nodeAggregator.getAllNodeNames()) {
			String nodeName = entry.getKey();
			FunctionalNode functionalNode = entry.getValue();

			if (!(functionalNode instanceof ExternalNode)) {
				String readableNodeName = FeatureNamesExtractor.makeNameReadable(nodeName);
				extractedNodeNames.add(readableNodeName);
			}
		}
		return extractedNodeNames;
	}
}
