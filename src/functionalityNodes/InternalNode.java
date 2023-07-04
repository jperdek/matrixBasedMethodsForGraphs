package functionalityNodes;

import java.util.Map;

import parser.FeatureNamesExtractor;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class InternalNode extends FunctionalNode {

	public InternalNode(String importName, String pathInProject) {
		super(importName, pathInProject);
		this.functionalNodes = new HashMap<String, FunctionalNode>();
	}
	
	public void print() {
		System.out.println("Internal | " + importName + " -> " + pathInProject);
	}
	
	public void insertChildNode(String nodeIdentifier, FunctionalNode functionalNode) {
		this.functionalNodes.put(nodeIdentifier, functionalNode);
	}
	
	public FunctionalNode getChildNode(String nodeIdentifier) {
		return this.functionalNodes.get(nodeIdentifier);
	}
	
	public Map<String, FunctionalNode> getFunctionalNodes() {
		return this.functionalNodes;
	}

	private void tabularize(int depth) {
		for(int i = 0; i < depth; i++) {
			System.out.append(' ');
		}
	}
	
	public void printTree(int depth, boolean includeExternal) {
		this.tabularize(depth);
		System.out.println("-->Module: " + this.importName);
		Iterator<FunctionalNode> functionalNodesIterator = new ArrayList<FunctionalNode>(
				this.functionalNodes.values()).iterator();
		
		int newDepth = depth + 1;
		while(functionalNodesIterator.hasNext()) {
			FunctionalNode processedNode = functionalNodesIterator.next();
			if (processedNode instanceof ModuleNode) {
				ModuleNode internalProcessedNode = (ModuleNode) processedNode;
				internalProcessedNode.printTree(newDepth, includeExternal);
			} else if (processedNode instanceof ComponentNode) {
				this.tabularize(newDepth);
				System.out.println("|->Component: " + FeatureNamesExtractor.makeNameReadable(processedNode.getImportName()));
				ComponentNode componentNode = (ComponentNode) processedNode;
				componentNode.printSelectorOccurrences(newDepth + 1);
				componentNode.printServicesOccurrences(newDepth + 1);
			} else if (processedNode instanceof ServiceNode) {
				this.tabularize(newDepth);
				System.out.println("|>>Service: " + FeatureNamesExtractor.makeNameReadable(processedNode.getImportName()));
				ServiceNode serviceNode = (ServiceNode) processedNode;
				serviceNode.printServicesOccurrences(newDepth + 1);
				serviceNode.printTree(newDepth, includeExternal);
			} else if (includeExternal && processedNode instanceof ExternalNode) {
				this.tabularize(newDepth);
				System.out.println("-->_ext_Module: " + FeatureNamesExtractor.makeNameReadable(processedNode.getImportName()));
			}
		}
	}
	
	public void markServiceOccurrence(String serviceIdentifier, int occurrences) {
		System.out.println("Error: Unimplemented mark service occurences! No service or component is used!");
	}
	
	public void getAllComponents(int depth, List<ComponentNode> aggregatedComponents) {
		Iterator<FunctionalNode> functionalNodesIterator = new ArrayList<FunctionalNode>(
				this.functionalNodes.values()).iterator();
		
		int newDepth = depth + 1;
		while(functionalNodesIterator.hasNext()) {
			FunctionalNode processedNode = functionalNodesIterator.next();
			if (processedNode instanceof ModuleNode) {
				ModuleNode internalProcessedNode = (ModuleNode) processedNode;
				internalProcessedNode.getAllComponents(newDepth, aggregatedComponents);
			} else if (processedNode instanceof ComponentNode) {
				ComponentNode componentNode = (ComponentNode) processedNode;
				aggregatedComponents.add(componentNode);
			}
		}
	}
}
