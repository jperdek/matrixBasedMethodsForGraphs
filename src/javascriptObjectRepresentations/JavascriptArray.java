package javascriptObjectRepresentations;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import functionalityNodes.FunctionalNode;


public class JavascriptArray extends JavascriptRepresentation {
	List<JavascriptRepresentation> arrayContent = new ArrayList<JavascriptRepresentation>();
	
	public JavascriptArray() {
		super();
	}
	
	public void addContentToArray(JavascriptRepresentation javascriptRepresentation) {
		this.arrayContent.add(javascriptRepresentation);
	}
	
	public JavascriptRepresentation parse(BufferedReader bufferedReader) throws IOException {	
		char rodeChar;
		StringBuilder arrayPart = new StringBuilder();
		JavascriptRepresentation additionalContent = null;
		while(bufferedReader.ready() && (rodeChar = (char) bufferedReader.read()) != ']') {
			if (rodeChar == ',' || rodeChar == ']') {
				this.arrayContent.add(new JavascriptString(arrayPart.toString().replaceAll("\\s", "").strip(), additionalContent));
				additionalContent = null;
				arrayPart = new StringBuilder();
				if (rodeChar == ']') {
					return this;
				}
			} else if (rodeChar == '[') {
				additionalContent = new JavascriptArray();
				additionalContent.parse(bufferedReader);
			}else {
				arrayPart.append(rodeChar);
			}
		}
		if (arrayPart.toString() != "") {
			this.arrayContent.add(new JavascriptString(arrayPart.toString().replaceAll("\\s", "").strip(), additionalContent));
		}
		return this;
	}
	
	public void print() {
		System.out.println("Array: [");
		Iterator<JavascriptRepresentation> iterator = arrayContent.iterator();
		while(iterator.hasNext()) {
			iterator.next().print();
		}
	}
	
	public boolean associateNode(String nodeIdentifier, FunctionalNode functionalNode) {
		Iterator<JavascriptRepresentation> iterator = arrayContent.iterator();
		while(iterator.hasNext()) {
			if (iterator.next().associateNode(nodeIdentifier, functionalNode)) {
				return true;
			}
		}
		return false;
	}
	
	public List<JavascriptRepresentation> getAllAssociatedJavascriptRepresentations() {
		return arrayContent;
	}
	
	public JavascriptRepresentation getAssociatedJavascriptRepresentation(String identifier) {
		return null;
	}
}
