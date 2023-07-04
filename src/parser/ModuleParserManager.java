package parser;

import functionalityNodes.FunctionalNode;
import functionalityNodes.InternalNode;
import functionalityNodes.ModuleNode;
import functionalityNodes.NodeAggregator;
import functionalityNodes.NodeMapper;
import javascriptObjectRepresentations.JavascriptRepresentation;
import javascriptObjectRepresentations.JavascriptString;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ModuleParserManager {
	
	public ModuleParserManager() {	
	}
	
	public FunctionalNode parseModules(String mainModuleFilePath, String importsFilePath, NodeAggregator nodeAggregator) {
		ModuleNode processedNode = new ModuleNode("Root module", importsFilePath);
		return this.parseModule(processedNode, mainModuleFilePath, nodeAggregator);
	}

	public FunctionalNode parseModule(InternalNode processedNode, String mainModuleFilePath, NodeAggregator nodeAggregator) {	
		JavascriptRepresentation processedModuleJavascript = parseGivenModule(mainModuleFilePath, mainModuleFilePath, nodeAggregator);
		
		//associates parsed module data
		processedNode.associateJavascriptRepresentation(processedModuleJavascript);
		
		// extracts all module information only from imports field
		JavascriptRepresentation modulesObjectJavascript = processedModuleJavascript.getAssociatedJavascriptRepresentation("imports");
		
		// if imports are not available in module
		if (modulesObjectJavascript == null) { return processedNode; }
		
		Iterator<JavascriptRepresentation> associatedModulesJavascript = modulesObjectJavascript.getAllAssociatedJavascriptRepresentations().iterator();
				
		// iterating through module information from imports field
		while(associatedModulesJavascript.hasNext()) {
			JavascriptRepresentation actualModuleJavascript = associatedModulesJavascript.next();
			FunctionalNode associatedNode = ((JavascriptString) actualModuleJavascript).getAssociatedNode();
			if (associatedNode == null) { continue; }

			String identifier = associatedNode.getImportName();
			if (identifier.endsWith("RoutingModule")) {
				//parsing of routing module go here!
				continue;
			}

			processedNode.insertChildNode(identifier, associatedNode);
			String fixedPathToModule = associatedNode.getPathInProject();
			fixedPathToModule = fixedPathToModule.replaceAll("module$", "module.ts");
			
			if (associatedNode instanceof ModuleNode) {  //instance of InternalNode //possibility
				parseModule(((InternalNode) associatedNode), fixedPathToModule, nodeAggregator);
			}
		}
		
		return processedNode;
	}
	
	private JavascriptRepresentation parseGivenModule(String mainModuleFilePath, String importsFilePath, NodeAggregator nodeAggregator) {
		JavascriptRepresentation mainModuleRepresentation = FileLoader.getJSONObjectFromFile(mainModuleFilePath, "@NgModule");
		List<String> imports = FileLoader.getRegexDataFromFile(
				importsFilePath, AngularDetectionRegexes.getImportRegexp());

		NodeMapper nodeMapper = new NodeMapper(nodeAggregator, mainModuleRepresentation);
		
		int lastFileNameIndex = mainModuleFilePath.lastIndexOf("/");
		if (lastFileNameIndex == -1) {
			lastFileNameIndex = mainModuleFilePath.lastIndexOf("\\");
		}
		
		String basePart = mainModuleFilePath.substring(0, lastFileNameIndex);
		basePart = basePart.replace('\\', '/');
		nodeMapper.mapImports(imports, basePart);
		return mainModuleRepresentation;
	}
	
	public FunctionalNode lookup(ModuleNode processedModule) {
		System.out.println("<-------------------------------------------------------------->");
		System.out.println(processedModule.getImportName());
		processedModule.getJavascriptRepresention().print();
		System.out.println("<->");
		Map<String, FunctionalNode> functionalNodes = processedModule.getFunctionalNodes();
		Iterator<FunctionalNode> functionalNodesIterator = new ArrayList<FunctionalNode>(
				functionalNodes.values()).iterator();

		// continue lookup to other components
		while(functionalNodesIterator.hasNext()) {
			FunctionalNode processedNode = functionalNodesIterator.next();
			
			if (processedNode instanceof ModuleNode) {
				ModuleNode childModuleNode = (ModuleNode) processedNode;
				lookup(childModuleNode);
			}
		}
		return processedModule;
	}
}
