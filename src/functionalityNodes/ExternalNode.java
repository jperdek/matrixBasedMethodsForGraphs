package functionalityNodes;


public class ExternalNode extends FunctionalNode {
	public ExternalNode(String importName, String pathInProject) {
		super(importName, pathInProject);
	}
	
	public void print() {
		System.out.println("External | " + importName + " -> " + pathInProject);
	}
}
