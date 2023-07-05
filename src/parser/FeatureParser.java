package parser;

import functionalityNodes.FunctionalNode;
import functionalityNodes.ModuleNode;
import functionalityNodes.NodeAggregator;
import functionalityNodes.SelectorAggregator;
import main.MainConfiguration;
import semanticAttributesAggregation.TemplateSemanticManager;


public class FeatureParser {

	public FeatureParser() {
	}
	
	public ModuleNode parse(String mainModuleFilePath, String importsFilePath) {
		ModuleParserManager moduleParser = new ModuleParserManager();
		NodeAggregator nodeAggregator = new NodeAggregator();
		FunctionalNode rootModule = moduleParser.parseModules(
				mainModuleFilePath, importsFilePath, nodeAggregator);
		ComponentsParserManager componentsParserManager = new ComponentsParserManager();
		SelectorAggregator selectorAggregator = new SelectorAggregator();
		componentsParserManager.componentLookup((ModuleNode) rootModule, nodeAggregator, selectorAggregator, false);
		TemplateParser templateParser = new TemplateParser(selectorAggregator);
		componentsParserManager.additionalComponentLookup((ModuleNode) rootModule, nodeAggregator, templateParser, false);
		((ModuleNode) rootModule).printTree(0, false);
		
		FeatureNamesExtractor featureNamesExtractor = new FeatureNamesExtractor(nodeAggregator);
		featureNamesExtractor.extractNodeNames();
		
		//moduleParser.lookup((ModuleNode) rootModule);7
		return ((ModuleNode) rootModule); 
	}
	
	public static void main(String args[]) {
		FeatureParser featureParser = new FeatureParser();
		ModuleNode rootModule = featureParser.parse(MainConfiguration.APP1_PUZZLE_TO_PLAY_MAIN_MODULE_PATH, 
				MainConfiguration.APP1_PUZZLE_TO_PLAY_MAIN_MODULE_PATH);
		TemplateSemanticManager templateSemanticManager = new TemplateSemanticManager();
		templateSemanticManager.processComponents(rootModule);
		
	}
}