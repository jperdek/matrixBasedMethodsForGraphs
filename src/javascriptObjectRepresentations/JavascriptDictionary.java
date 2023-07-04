package javascriptObjectRepresentations;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import functionalityNodes.FunctionalNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class JavascriptDictionary extends JavascriptRepresentation {
	
	Map<String, JavascriptRepresentation> identifiedVariables = new HashMap<String, JavascriptRepresentation>();
	
	public JavascriptDictionary() {
		super();
	}
	
	public void print() {
		System.out.println("Dictionary");
		Iterator<String> keyIterator = this.identifiedVariables.keySet().iterator();
		while(keyIterator.hasNext()) {
			String key = keyIterator.next();
			JavascriptRepresentation javascriptRepresentation = this.identifiedVariables.get(key);
			System.out.println("Key: >" + key + "< ");
			javascriptRepresentation.print();
			System.out.println(" ]");
		}	
	}
	
	public boolean associateNode(String nodeIdentifier, FunctionalNode functionalNode) {
		Iterator<String> keyIterator = this.identifiedVariables.keySet().iterator();
		while(keyIterator.hasNext()) {
			String key = keyIterator.next();
			JavascriptRepresentation javascriptRepresentation = this.identifiedVariables.get(key);
			if (javascriptRepresentation.associateNode(nodeIdentifier, functionalNode)) {
				return true;
			}
		}
		return false;
	}
	
	public JavascriptRepresentation parse(BufferedReader bufferedReader) throws IOException {	
		while(bufferedReader.ready()) {
			char rodeChar = '}';
			StringBuilder variableNameBuilder = new StringBuilder();
			while(bufferedReader.ready() && (rodeChar = (char) bufferedReader.read()) != ':' && rodeChar != '}') {
				if (rodeChar != '\n' && rodeChar != '\t' && rodeChar != ' ' && rodeChar != ',' && rodeChar != ']') {
					variableNameBuilder.append(rodeChar);
				}
			}
			
			if (rodeChar == '}') { return this; }
			
			char structuralChar = '[';
			while(bufferedReader.ready()) {
				rodeChar = (char) bufferedReader.read();
				if (rodeChar != '\n' && rodeChar != '\t' && rodeChar != ' ') {
					structuralChar = rodeChar;
					break;
				}
			}
			
			if (structuralChar == '[') {
				String variableName = variableNameBuilder.toString().replaceAll("\\s", "");
				JavascriptRepresentation javascriptArray = new JavascriptArray();
				javascriptArray.parse(bufferedReader);
				identifiedVariables.put(variableName, javascriptArray);
			} else if (structuralChar == '{') {
				String variableName = variableNameBuilder.toString().replaceAll("\\s", "");
				JavascriptRepresentation javascriptDictionary = new JavascriptDictionary();
				javascriptDictionary.parse(bufferedReader);
				identifiedVariables.put(variableName, javascriptDictionary);
			} else if (structuralChar == '\'') {
				String variableName = variableNameBuilder.toString().replaceAll("\\s", "");
				JavascriptRepresentation javascriptString = new JavascriptString();
				javascriptString.parse(bufferedReader);
				identifiedVariables.put(variableName, javascriptString);
			} else {
				System.out.println("Unknown char: " + structuralChar);
				return this;
			}
		}
		return this;
	}
	
	public List<JavascriptRepresentation> getAllAssociatedJavascriptRepresentations() {
		return new ArrayList<JavascriptRepresentation>( identifiedVariables.values());
	}
	
	public JavascriptRepresentation getAssociatedJavascriptRepresentation(String identifier) {
		return identifiedVariables.get(identifier);
	}
}
