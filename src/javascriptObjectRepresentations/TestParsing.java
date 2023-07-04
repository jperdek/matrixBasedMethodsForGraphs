package javascriptObjectRepresentations;
import parser.AngularDetectionRegexes;
import parser.FileLoader;

import functionalityNodes.NodeAggregator;
import functionalityNodes.NodeMapper;
import java.util.List;


public class TestParsing {
	public TestParsing() {}
	
	public void testParse(String filePath) {
		try {
			JavascriptRepresentation javascriptRepresentation = FileLoader.getJSONObjectFromFile(filePath, "@NgModule");
			javascriptRepresentation.print();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testGettingImports(String filePath) {
		 FileLoader.getRegexDataFromFile(
				filePath, AngularDetectionRegexes.getImportRegexp());	
	}
	
	public void testMapping(String parseFilePath, String importsFilePath) {
		JavascriptRepresentation javascriptRepresentation = FileLoader.getJSONObjectFromFile(parseFilePath, "@NgModule");
		List<String> imports = FileLoader.getRegexDataFromFile(
				importsFilePath, AngularDetectionRegexes.getImportRegexp());
		NodeAggregator nodeAggregator = new NodeAggregator();
		NodeMapper nodeMapper = new NodeMapper(nodeAggregator, javascriptRepresentation);
		
		int lastFileNameIndex = parseFilePath.lastIndexOf("/");
		if (lastFileNameIndex == -1) {
			lastFileNameIndex = parseFilePath.lastIndexOf("\\");
		}
		String basePart = parseFilePath.substring(0, lastFileNameIndex);
		basePart = basePart.replace('\\', '/');
		nodeMapper.mapImports(imports, basePart);
		javascriptRepresentation.print();
	}
	
	public static void main(String args[]) {
		TestParsing test = new TestParsing();
		//test.testParse("D:\\aspects\\puzzle\\src\\app\\app.module.ts");
		//test.testGettingImports("D:\\aspects\\puzzle\\src\\app\\app.module.ts");
		test.testMapping("D:\\aspects\\puzzle\\src\\app\\app.module.ts", 
				"D:\\aspects\\puzzle\\src\\app\\app.module.ts");
	}
}
