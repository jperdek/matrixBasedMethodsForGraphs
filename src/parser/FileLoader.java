package parser;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.List;
import java.util.ArrayList;
import javascriptObjectRepresentations.JavascriptRepresentation;


public class FileLoader {
	
	public FileLoader() {}
	
	public static List<String> getRegexDataFromFile(String pathToFile, String regularExpression) {
		FileReader fr = null;
		Scanner scanner = null;
		try {
			fr = new FileReader(pathToFile);
			scanner = new Scanner(fr);
			Iterator<MatchResult> iterator = scanner.findAll(regularExpression).iterator();
			
			List<String> results = new ArrayList<String>();
			while(iterator.hasNext()) {
				String result = iterator.next().group(0);
				results.add(result);
			}
			return results;
		} catch(FileNotFoundException fnf) {
			fnf.printStackTrace();
		} finally {
			if(scanner != null) { scanner.close(); };
			if(fr != null) { 
				try {
					fr.close();
				} catch(IOException e) {
				
				};
			}
		}
		return null;
	}
	
	public static JavascriptRepresentation getJSONObjectFromFile(String pathToFile, String recognitionObjectName) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(pathToFile);
			br = new BufferedReader(fr);
			
			return ObjectParser.parse(br, recognitionObjectName);
		} catch(FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) { 
				try {
					br.close();
				} catch(IOException e) {
					
				};
			}
			if(fr != null) { 
				try {
					fr.close();
				} catch(IOException e) {
				
				};
			}
		}
		return null;
	}
	
	public static String getMethodArguments(String pathToFile, String recognitionObjectName) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(pathToFile);
			br = new BufferedReader(fr);
			
			return ObjectParser.parseMethodArguments(br, recognitionObjectName);
		} catch(FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) { 
				try {
					br.close();
				} catch(IOException e) {
					
				};
			}
			if(fr != null) { 
				try {
					fr.close();
				} catch(IOException e) {
				
				};
			}
		}
		return null;
	}
	
	public static String loadWholeFile(String pathToFile) {
		FileReader fr = null;
		Scanner scanner = null;
		try {
			fr = new FileReader(pathToFile);
			scanner = new java.util.Scanner(fr).useDelimiter("\\A");
			return scanner.hasNext() ? scanner.next() : "";
		} catch(FileNotFoundException fnf) {
			fnf.printStackTrace();
		} finally {
			if(scanner != null) { scanner.close(); };
			if(fr != null) { 
				try {
					fr.close();
				} catch(IOException e) {
				
				};
			}
		}
		return null;
	}
}
