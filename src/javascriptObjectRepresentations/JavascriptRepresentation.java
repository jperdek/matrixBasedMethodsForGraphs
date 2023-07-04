package javascriptObjectRepresentations;
import java.io.BufferedReader;
import java.io.IOException;
import functionalityNodes.FunctionalNode;
import java.util.List;

public abstract class JavascriptRepresentation {
	public JavascriptRepresentation() {}
	
	public abstract JavascriptRepresentation parse(BufferedReader br)  throws IOException;
	public abstract void print();
	public abstract boolean associateNode(String nodeIdentifier, FunctionalNode functionalNode);
	
	public abstract JavascriptRepresentation getAssociatedJavascriptRepresentation(String identifier);
	public abstract List<JavascriptRepresentation> getAllAssociatedJavascriptRepresentations();
}
