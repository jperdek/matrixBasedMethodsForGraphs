package parser;
import java.io.BufferedReader;
import java.io.IOException;

import javascriptObjectRepresentations.JavascriptDictionary;
import javascriptObjectRepresentations.JavascriptRepresentation;


public class ObjectParser {

	ObjectParser() {}
	
	private static void parseToStartSequence(BufferedReader bufferedReader, String searchSequence) throws IOException {
		char rodeChar;
		StringBuilder stringBuilder = new StringBuilder();
		while(bufferedReader.ready()) {
			rodeChar = (char) bufferedReader.read();
			stringBuilder.append(rodeChar);
			if (stringBuilder.indexOf(searchSequence) >= 0) {
				break;
			}
		}
	}
	
	private static void parseToStartChar(BufferedReader bufferedReader, char startChar) throws IOException {
		while(bufferedReader.ready() && ((char) bufferedReader.read()) != startChar) {}
	}
	
	public static JavascriptRepresentation parse(BufferedReader bufferedReader, String recognitionName) throws IOException {
		parseToStartSequence(bufferedReader, recognitionName);
		parseToStartChar(bufferedReader, '{');

		JavascriptRepresentation javascriptRepresentation = new JavascriptDictionary();
		javascriptRepresentation.parse(bufferedReader);
		return javascriptRepresentation;
	}
	
	public static String parseMethodArguments(BufferedReader bufferedReader, String recognitionName) throws IOException {
		parseToStartSequence(bufferedReader, recognitionName);
		parseToStartChar(bufferedReader, '(');
		
		StringBuffer sb = new StringBuffer();
		char rodeChar = ' ';
		int occurrences = 1;
		while(bufferedReader.ready() && ((rodeChar = (char) bufferedReader.read()) != ')' || occurrences > 0)) {
			if (rodeChar == ')') { occurrences = occurrences - 1; if (occurrences == 0) { break; }}
			if (rodeChar == '(') { occurrences = occurrences + 1; }
			sb.append(rodeChar);
		}
		
		return sb.toString();
	}
}
