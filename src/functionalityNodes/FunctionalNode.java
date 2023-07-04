package functionalityNodes;
import java.util.Map;

import javascriptObjectRepresentations.JavascriptRepresentation;


public class FunctionalNode {
	protected Map<String, FunctionalNode> functionalNodes = null;
	protected String pathInProject;
	protected String importName;
	protected JavascriptRepresentation javascriptRepresentation;
	

	public FunctionalNode(String importName, String pathInProject) {
		this.importName = importName;
		this.pathInProject = pathInProject;
	}
	
	public String getPathInProject() {
		return this.pathInProject;
	}
	
	public String getImportName() {
		return this.importName;
	}
	
	public Map<String, FunctionalNode> getFunctionalNodes() {
		return this.functionalNodes;
	}

	public void print() {
		System.out.println("| " + importName + " -> " + pathInProject);
	}
	
	public void associateJavascriptRepresentation(JavascriptRepresentation javascriptRepresentation) {
		this.javascriptRepresentation = javascriptRepresentation;
	}
	
	public JavascriptRepresentation getJavascriptRepresention() {
		return this.javascriptRepresentation;
	}
}
