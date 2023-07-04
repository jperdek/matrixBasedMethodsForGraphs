package functionalityNodes;

import javascriptObjectRepresentations.JavascriptRepresentation;
import java.util.Iterator;
import java.util.List;


public class NodeMapper {
	private NodeAggregator nodeAggregator;
	private JavascriptRepresentation mappedPart;
	
	public NodeMapper(NodeAggregator nodeAggregator, JavascriptRepresentation mappedPart) {
		this.nodeAggregator = nodeAggregator;
		this.mappedPart = mappedPart;
	}
	
	private boolean conditionToIgnoreNotMappedParts(FunctionalNode fnode, String identifier) {
		return !(fnode instanceof InternalNode) && !identifier.contains("odule");
	}
	
	public void mapImports(List<String> importLines, String basePath) {
		List<FunctionalNode> representants = this.nodeAggregator.insertImports(importLines, basePath);
		Iterator<FunctionalNode> iterator  = representants.iterator();
		
		while(iterator.hasNext()) {
			FunctionalNode fnode = iterator.next();
			String identifier = fnode.getImportName();
			if (!this.mappedPart.associateNode(identifier, fnode) 
					&& this.conditionToIgnoreNotMappedParts(fnode, identifier)) {
				System.out.println(identifier + " has not been associated!");
			}
		}
	}
}
