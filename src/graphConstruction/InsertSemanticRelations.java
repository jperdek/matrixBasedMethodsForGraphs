package graphConstruction;

import static org.neo4j.driver.Values.parameters;

import java.util.Map;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import functionalityNodes.ModuleNode;
import parser.FeatureParser;
import semanticAttributesAggregation.AttributeSetAggregator;
import semanticAttributesAggregation.TemplateSemanticManager;


public class InsertSemanticRelations extends GraphCreator {

	private TemplateSemanticManager templateSemanticManager;
	
	public InsertSemanticRelations(String uri, String user, String password) {
		super(uri, user, password);
		this.templateSemanticManager = new TemplateSemanticManager();
	}
	
	private static void processPuzzleToPlay() {
		FeatureParser featureParser = new FeatureParser();
		ModuleNode rootModule = featureParser.parse("D:\\aspects\\puzzle\\src\\app\\app.module.ts", 
				"D:\\aspects\\puzzle\\src\\app\\app.module.ts");
		try (InsertSemanticRelations featureDataInsertion = new InsertSemanticRelations("bolt://localhost:7687", "neo4j", "feature")) {
			featureDataInsertion.createGraph(rootModule, AppliedGraphNames.PUZZLE_TO_PLAY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void processDesign3D() {
		FeatureParser featureParser = new FeatureParser();
		ModuleNode rootModule = featureParser.parse("D:\\aspects\\design3D\\design3D\\src\\app\\app.module.ts", 
				"D:\\aspects\\design3D\\design3D\\src\\app\\app.module.ts");
		try (InsertSemanticRelations featureDataInsertion = new InsertSemanticRelations("bolt://localhost:7687", "neo4j", "feature")) {
			featureDataInsertion.createGraph(rootModule, AppliedGraphNames.DESIGN_3D);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void processUntaggedGraph() {
		FeatureParser featureParser = new FeatureParser();
		ModuleNode rootModule = featureParser.parse("D:\\aspects\\puzzle\\src\\app\\app.module.ts", 
				"D:\\aspects\\puzzle\\src\\app\\app.module.ts");
		try (InsertSemanticRelations featureDataInsertion = new InsertSemanticRelations("bolt://localhost:7687", "neo4j", "feature")) {
			featureDataInsertion.createGraph(rootModule, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		InsertSemanticRelations.processPuzzleToPlay();
		InsertSemanticRelations.processDesign3D();
	}

	public void createGraph(ModuleNode rootNode, String tag)
    {
        try (Session session = driver.session()){
        	this.templateSemanticManager.processComponents(rootNode);
        	this.insertTemplateClassesRelationIndexValues(session, this.templateSemanticManager, tag);
        	this.insertTemplateAttributesRelationIndexValues(session, this.templateSemanticManager, tag);
        }
    }
	
	private void insertTemplateClassesRelationIndexValues(
			Session session, TemplateSemanticManager templateSemanticManager, String tag) {
		Map<String, Double> templateClasses = templateSemanticManager.getTemplateClassesMap();
		String name1, name2;
		String key;
		double relationValue;
		
		for (var entry : templateClasses.entrySet()) {
			key = entry.getKey();
			int delimiterPosition = key.indexOf(AttributeSetAggregator.DELIMITER);
		    name1 = key.substring(0, delimiterPosition);
		    name2 = key.substring(delimiterPosition + AttributeSetAggregator.DELIMITER.length());
		    relationValue = entry.getValue();
		    
		    if (tag == null) {
		    	this.createAttributeConnectioIfNotExists(session, name1, name2, "classSimilarity", relationValue);
		    } else {
		    	this.createAttributeConnectioIfNotExists(session, name1, name2, "classSimilarity", relationValue, tag);
		    }
		}
	}
	
	private void insertTemplateAttributesRelationIndexValues(
			Session session, TemplateSemanticManager templateSemanticManager, String tag) {
		Map<String, Double> templateAttributes = templateSemanticManager.getTemplateAttributesMap();
		String name1, name2;
		String key;
		double relationValue;
		
		for (var entry : templateAttributes.entrySet()) {
			key = entry.getKey();
			int delimiterPosition = key.indexOf(AttributeSetAggregator.DELIMITER);
		    name1 = key.substring(0, delimiterPosition);
		    name2 = key.substring(delimiterPosition + AttributeSetAggregator.DELIMITER.length());
		    relationValue = entry.getValue();

		    if (tag == null) {
		    	this.createAttributeConnectioIfNotExists(session, name1, name2, "attributesSimilarity", relationValue);
		    } else {
		    	this.createAttributeConnectioIfNotExists(session, name1, name2, "attributesSimilarity", relationValue, tag);
		    }
		}
	}
	
	private void createAttributeConnectioIfNotExists(Session session, String componentName1, String componentName2, String relationName, double relationValue) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Component {name: $name1}), (b:Component {name: $name2}) MERGE (a)-[:" + relationName + " {value: $relationValue}]->(b)",
								parameters("name1", componentName1, "name2", componentName2, "relationValue", relationValue ) );
				return result;
        } );
	}

	private void createAttributeConnectioIfNotExists(Session session, String componentName1, String componentName2, String relationName, double relationValue, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Component {name: $name1, tag: $tag}), (b:Component {name: $name2, tag: $tag}) MERGE (a)-[:" + 
								relationName + " {value: $relationValue, tag: $tag}]->(b)",
								parameters("name1", componentName1, "name2", componentName2, "relationValue", relationValue, "tag", tag ) );
				return result;
        } );
	}
}
