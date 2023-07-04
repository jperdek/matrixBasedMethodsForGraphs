package parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import functionalityNodes.ComponentNode;
import functionalityNodes.FunctionalNode;
import functionalityNodes.ModuleNode;
import functionalityNodes.NodeAggregator;
import functionalityNodes.SelectorAggregator;
import javascriptObjectRepresentations.JavascriptRepresentation;
import javascriptObjectRepresentations.JavascriptString;
import java.util.Map;


public class ComponentsParserManager {

	private ServiceParserManager serviceParserManager;
	
	public ComponentsParserManager() {
		this.serviceParserManager = new ServiceParserManager();
	}
	
	public FunctionalNode componentLookup(ModuleNode processedModule, NodeAggregator nodeAggregator, 
			SelectorAggregator selectorAggregator, boolean includeExternal) {
		Map<String, FunctionalNode> functionalNodes = processedModule.getFunctionalNodes();
		Iterator<FunctionalNode> functionalNodesIterator = new ArrayList<FunctionalNode>(
				functionalNodes.values()).iterator();
		
		parseComponents(processedModule, nodeAggregator, selectorAggregator);

		// continue lookup to other components
		while(functionalNodesIterator.hasNext()) {
			FunctionalNode processedNode = functionalNodesIterator.next();
			
			if (processedNode instanceof ModuleNode) {
				ModuleNode childModuleNode = (ModuleNode) processedNode;
				componentLookup(childModuleNode, nodeAggregator, selectorAggregator, includeExternal);
			}
		}
		return processedModule;
	}
	
	private void parseComponents(ModuleNode processedModule, NodeAggregator nodeAggregator, SelectorAggregator selectorAggregator) {
		// get components from module
		JavascriptRepresentation processedComponentsJavascript = processedModule.getJavascriptRepresention();
		JavascriptRepresentation modulesObjectJavascript = processedComponentsJavascript.getAssociatedJavascriptRepresentation("declarations");
		
		// if declarations are not available in module
		if (modulesObjectJavascript == null) { return; }
		
		Iterator<JavascriptRepresentation> associatedComponentsJavascript = modulesObjectJavascript.getAllAssociatedJavascriptRepresentations().iterator();
		
		while(associatedComponentsJavascript.hasNext()) {
			JavascriptRepresentation actualComponentJavascript = associatedComponentsJavascript.next();
			
			FunctionalNode associatedNode = ((JavascriptString) actualComponentJavascript).getAssociatedNode();
			if (associatedNode == null) { continue; }

			String identifier = associatedNode.getImportName();

			processedModule.insertChildNode(identifier, associatedNode);
			//nodeAggregator.addNode(identifier, associatedNode);
			
			String componentFilePath = associatedNode.getPathInProject();
			if (componentFilePath.endsWith("component")) {
				componentFilePath = componentFilePath.replaceAll("component$", "component.ts");
				JavascriptRepresentation componentRepresentation = FileLoader.getJSONObjectFromFile(componentFilePath, "@Component");
				associatedNode.associateJavascriptRepresentation(componentRepresentation);
				parseComponentDetails(componentRepresentation, (ComponentNode) associatedNode, selectorAggregator, nodeAggregator);
			} else if (componentFilePath.endsWith("directive")) {
				componentFilePath = componentFilePath.replaceAll("directive$", "directive.ts");
				JavascriptRepresentation componentRepresentation = FileLoader.getJSONObjectFromFile(componentFilePath, "@Directive");
				associatedNode.associateJavascriptRepresentation(componentRepresentation);
			} else {
				System.out.println("Unknown declaration type! Example: " + componentFilePath);
			}
			//associatedNode.associateJavascriptRepresentation(componentRepresentation);
		}
	}
	
	private String fixRelativePaths(String templateUrl, String path) {
		String basePath = path.substring(0, path.lastIndexOf("/"));

		if (templateUrl.startsWith(".")) {
			if (templateUrl.startsWith("./")) {
				templateUrl = templateUrl.replaceAll("^\\.\\/", basePath + "/");
			} else if (templateUrl.startsWith("../")) {
				String basePath2 = basePath;
				while (templateUrl.startsWith("../")) {
					basePath2 = basePath2.substring(0, basePath2.lastIndexOf("/"));
					templateUrl = templateUrl.replaceAll("^\\.\\.\\/", "");
				}
				templateUrl = templateUrl.replaceAll("^", basePath2 + "/");
			}

		}
		return templateUrl;
	}
	
	private String extractFromJavascriptString(String wrappedString) {
		if (wrappedString.matches("^[\"'][^\"']+[\"']$")) {
			return wrappedString.substring(1, wrappedString.length() - 1);
		}
		return wrappedString;
	}

	private void parseComponentDetails(JavascriptRepresentation componentContent, ComponentNode componentNode, SelectorAggregator selectorAggregator, NodeAggregator nodeAggregator) {
		JavascriptString selectorJavascript = (JavascriptString) componentContent.getAssociatedJavascriptRepresentation("selector");
		String selector = selectorJavascript.getString();
		componentNode.setSelector(selector);
		// maps selector
		selectorAggregator.add(selector, componentNode);
		
		JavascriptString templateUrlJavascript = (JavascriptString) componentContent.getAssociatedJavascriptRepresentation("templateUrl");
		String templateUrl = templateUrlJavascript.getString();
		templateUrl = fixRelativePaths(templateUrl, componentNode.getPathInProject());
		componentNode.setTemplateUrlPath(templateUrl);
		
		JavascriptRepresentation styleUrlsJavascript = componentContent.getAssociatedJavascriptRepresentation("styleUrls");
		Iterator<JavascriptRepresentation> associatedStyleUrlsJavascript = styleUrlsJavascript.getAllAssociatedJavascriptRepresentations().iterator();
		List<String> styleUrls = new ArrayList<String>();
		while(associatedStyleUrlsJavascript.hasNext()) {
			JavascriptString actualStyleUrlJavascript = (JavascriptString) associatedStyleUrlsJavascript.next();
			String styleUrl = fixRelativePaths(extractFromJavascriptString(actualStyleUrlJavascript.getString()), componentNode.getPathInProject());
			styleUrls.add(styleUrl);
		}
		componentNode.setStyleUrls(styleUrls);
		
		this.serviceParserManager.associateServiceAssociations(componentNode, nodeAggregator);
	}
	
	
	public FunctionalNode additionalComponentLookup(ModuleNode processedModule, NodeAggregator nodeAggregator, 
			TemplateParser templateParser, boolean includeExternal) {
		Map<String, FunctionalNode> functionalNodes = processedModule.getFunctionalNodes();
		Iterator<FunctionalNode> functionalNodesIterator = new ArrayList<FunctionalNode>(
				functionalNodes.values()).iterator();
		
		// continue lookup to other components
		while(functionalNodesIterator.hasNext()) {
			FunctionalNode processedNode = functionalNodesIterator.next();
			
			if (processedNode instanceof ComponentNode) {
				ComponentNode componentNode = (ComponentNode) processedNode;
				String pathToComponent = componentNode.getTemplateUrlPath();
				String templateContent = FileLoader.loadWholeFile(pathToComponent);
				Map<String, Integer> selectorsOccurrences = templateParser.findAssociatedComponents(templateContent);
				templateParser.collectSemanticInformation(templateContent, componentNode);
				componentNode.setSelectorsOccurrences(selectorsOccurrences);
			} else if (processedNode instanceof ModuleNode) {
				ModuleNode childModuleNode = (ModuleNode) processedNode;
				additionalComponentLookup(childModuleNode, nodeAggregator, templateParser, includeExternal);
			}
		}
		return processedModule;
	}
}
