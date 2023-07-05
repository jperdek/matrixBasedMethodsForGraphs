package graphConstruction;

import static org.neo4j.driver.Values.parameters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import functionalityNodes.ComponentNode;
import functionalityNodes.ExternalNode;
import functionalityNodes.FunctionalNode;
import functionalityNodes.ModuleNode;
import functionalityNodes.ServiceNode;
import main.MainConfiguration;
import parser.FeatureParser;


public class FeatureDataInsertion extends GraphCreator {

	public FeatureDataInsertion(String uri, String user, String password) {
		super(uri, user, password);
	}
	
	public static void main(String args[]) {
		FeatureDataInsertion.insertPuzzleToPlay();
		FeatureDataInsertion.insertDesign3D();
	}

	private static void insertPuzzleToPlay() {
		FeatureParser featureParser = new FeatureParser();
		ModuleNode rootModule = featureParser.parse(MainConfiguration.APP1_PUZZLE_TO_PLAY_MAIN_MODULE_PATH, 
				MainConfiguration.APP1_PUZZLE_TO_PLAY_MAIN_MODULE_PATH);
		try (FeatureDataInsertion featureDataInsertion = new FeatureDataInsertion(
				MainConfiguration.NEO4J_DB_BOLT_CONNECTION, MainConfiguration.NEO4J_DB_NAME, MainConfiguration.NEO4J_DB_PASSWORD)) {
			featureDataInsertion.createGraph(rootModule, AppliedGraphNames.PUZZLE_TO_PLAY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void insertDesign3D() {
		FeatureParser featureParser = new FeatureParser();
		ModuleNode rootModule = featureParser.parse(MainConfiguration.APP2_DESIGN_3D_MAIN_MODULE_PATH, 
				MainConfiguration.APP2_DESIGN_3D_MAIN_MODULE_PATH);
		try (FeatureDataInsertion featureDataInsertion = new FeatureDataInsertion(
				MainConfiguration.NEO4J_DB_BOLT_CONNECTION, MainConfiguration.NEO4J_DB_NAME, MainConfiguration.NEO4J_DB_PASSWORD)) {
			featureDataInsertion.createGraph(rootModule, AppliedGraphNames.DESIGN_3D);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createGraph(ModuleNode rootNode, String tag)
    {
        try ( Session session = driver.session() )
        {
        	insertTree(rootNode, session, 0, tag, true);
        }
    }
	
	public void createModuleIfNotExists(Session session, String moduleName, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:Module {name: $name, tag: $tag})",
								parameters( "name", moduleName, "tag", tag ) );
				return result;
        } );
	}

	public void createComponentIfNotExists(Session session, String componentName, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:Component {name: $name, tag: $tag})",
								parameters( "name", componentName, "tag", tag ) );
				return result;
        } );
	}

	public void createServiceIfNotExists(Session session, String componentName, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:Service {name: $name, tag: $tag})",
								parameters( "name", componentName, "tag", tag ) );
				return result;
        } );
	}

	
	public void createExternalNodeIfNotExists(Session session, String componentName, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:External {name: $name, tag: $tag})",
								parameters( "name", componentName, "tag", tag ) );
				return result;
        } );
	}
	
	public void createUsesModuleDependency(Session session, String firstNodeName, String secondNodeName, int frequency, String tag) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Module {name: $firstNodeName, tag: $tag}), (b:Module {name: $secondNodeName, tag: $tag}) "
										+ "	MERGE (a)-[:Uses {frequency: $frequency, tag: $tag }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName, "tag", tag ) );
				return result;
        } );
	}
	
	public void createUsesModuleToComponentDependency(Session session, String firstNodeName, String secondNodeName, int frequency, String tag) {
		createComponentIfNotExists(session, secondNodeName, tag);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Module {name: $firstNodeName, tag: $tag}), (b:Component {name: $secondNodeName, tag: $tag}) "
										+ "	MERGE (a)-[:Uses {frequency: $frequency, tag: $tag }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName, "tag", tag ) );
				return result;
        } );
	}
	
	public void createUsesModuleToServiceDependency(Session session, String firstNodeName, String secondNodeName, int frequency, String tag) {
		createServiceIfNotExists(session, secondNodeName, tag);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Module {name: $firstNodeName, tag: $tag}), (b:Service {name: $secondNodeName, tag: $tag}) "
										+ "	MERGE (a)-[:Uses {frequency: $frequency, tag: $tag }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName, "tag", tag ) );
				return result;
        } );
	}

	public void createUsesModuleToExternalDependency(Session session, String firstNodeName, String secondNodeName, int frequency, String tag) {
		createExternalNodeIfNotExists(session, secondNodeName, tag);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Module {name: $firstNodeName, tag: $tag}), (b:External {name: $secondNodeName, tag: $tag}) "
										+ "	MERGE (a)-[:Uses {frequency: $frequency, tag: $tag }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName, "tag", tag ) );
				return result;
        } );
	}
	
	public void createUsesComponentToServiceDependency(Session session, String firstNodeName, String secondNodeName, int frequency, String tag) {
		createServiceIfNotExists(session, secondNodeName, tag);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Component {name: $firstNodeName, tag: $tag}), (b:Service {name: $secondNodeName, tag: $tag})"
										+ "	MERGE (a)-[:Uses {frequency: $frequency, tag: $tag }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName, "tag", tag ) );
				return result;
        } );
	}

	public void createUsesComponentToComponentDependency(Session session, String firstNodeName, String secondNodeName, int frequency, String tag) {
		createComponentIfNotExists(session, secondNodeName, tag);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Component {name: $firstNodeName, tag: $tag}), (b:Component {name: $secondNodeName, tag: $tag})"
										+ "	MERGE (a)-[:Uses {frequency: $frequency, tag: $tag }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName, "tag", tag ) );
				return result;
        } );
	}
	
	public void createUsesServiceToServiceDependency(Session session, String firstNodeName, String secondNodeName, int frequency, String tag) {
		createServiceIfNotExists(session, secondNodeName, tag);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Service {name: $firstNodeName, tag: $tag}), (b:Service {name: $secondNodeName, tag: $tag})"
						+ "	MERGE (a)-[:Uses {frequency: $frequency, tag: $tag }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName, "tag", tag ) );
				return result;
        } );
	}
	
	
	public void createModuleIfNotExists(Session session, String moduleName) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:Module {name: $name})",
								parameters( "name", moduleName ) );
				return result;
        } );
	}

	public void createComponentIfNotExists(Session session, String componentName) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:Component {name: $name})",
								parameters( "name", componentName ) );
				return result;
        } );
	}

	public void createServiceIfNotExists(Session session, String componentName) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:Service {name: $name})",
								parameters( "name", componentName ) );
				return result;
        } );
	}

	public void createExternalNodeIfNotExists(Session session, String componentName) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MERGE (a:External {name: $name})",
								parameters( "name", componentName ) );
				return result;
        } );
	}
	
	public void createUsesModuleDependency(Session session, String firstNodeName, String secondNodeName, int frequency) {
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Module {name: $firstNodeName}), (b:Module {name: $secondNodeName}) "
										+ "	MERGE (a)-[:Uses {frequency: $frequency }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName ) );
				return result;
        } );
	}
	
	public void createUsesModuleToComponentDependency(Session session, String firstNodeName, String secondNodeName, int frequency) {
		createComponentIfNotExists(session, secondNodeName);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Module {name: $firstNodeName}), (b:Component {name: $secondNodeName}) "
										+ "	MERGE (a)-[:Uses {frequency: $frequency }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName ) );
				return result;
        } );
	}
	
	public void createUsesModuleToServiceDependency(Session session, String firstNodeName, String secondNodeName, int frequency) {
		createServiceIfNotExists(session, secondNodeName);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Module {name: $firstNodeName}), (b:Service {name: $secondNodeName}) "
										+ "	MERGE (a)-[:Uses {frequency: $frequency }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName ) );
				return result;
        } );
	}

	public void createUsesModuleToExternalDependency(Session session, String firstNodeName, String secondNodeName, int frequency) {
		createExternalNodeIfNotExists(session, secondNodeName);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Module {name: $firstNodeName}), (b:External {name: $secondNodeName}) "
										+ "	MERGE (a)-[:Uses {frequency: $frequency }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName ) );
				return result;
        } );
	}
	
	public void createUsesComponentToServiceDependency(Session session, String firstNodeName, String secondNodeName, int frequency) {
		createServiceIfNotExists(session, secondNodeName);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Component {name: $firstNodeName}), (b:Service {name: $secondNodeName})"
										+ "	MERGE (a)-[:Uses {frequency: $frequency }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName ) );
				return result;
        } );
	}

	public void createUsesComponentToComponentDependency(Session session, String firstNodeName, String secondNodeName, int frequency) {
		createComponentIfNotExists(session, secondNodeName);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Component {name: $firstNodeName}), (b:Component {name: $secondNodeName})"
										+ "	MERGE (a)-[:Uses {frequency: $frequency }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName ) );
				return result;
        } );
	}
	
	public void createUsesServiceToServiceDependency(Session session, String firstNodeName, String secondNodeName, int frequency) {
		createServiceIfNotExists(session, secondNodeName);
		session.writeTransaction( tx -> {
				Result result = tx.run( "MATCH (a:Service {name: $firstNodeName}), (b:Service {name: $secondNodeName})"
						+ "	MERGE (a)-[:Uses {frequency: $frequency }]->(b)",
								parameters( "firstNodeName", firstNodeName, "frequency", frequency, "secondNodeName", secondNodeName ) );
				return result;
        } );
	}
	
	public void insertTree(FunctionalNode processedMainNode, Session session, int depth, String tag, boolean includeExternal) {
		//CREATES ROOT MODULE NODE
		if (tag == null) {
			createModuleIfNotExists(session, processedMainNode.getImportName());
		} else {
			createModuleIfNotExists(session, processedMainNode.getImportName(), tag);
		}
		insertTreeInner(processedMainNode, session, 0, tag, true);
	}
	
	public void insertSelectorOccurrencesForComponent(ComponentNode processedComponent, Session session, String tag) {
		Map<String, Integer> selectorsOccurrences = processedComponent.getSelectorsOccurrences();
		if (selectorsOccurrences == null) { return; }
		
		for (Map.Entry<String, Integer> entry : selectorsOccurrences.entrySet()) {
			String selectorName = entry.getKey();
			Integer occurrenceCount = entry.getValue();
			
			//CREATE COMPONENT TO GIVEN COMPONENT CONNECTION
			if (tag == null) {
				createUsesComponentToComponentDependency(
						session, processedComponent.getImportName(), selectorName, occurrenceCount);
			} else {
				createUsesComponentToComponentDependency(
						session, processedComponent.getImportName(), selectorName, occurrenceCount, tag);
			}
		}
	}
	
	public void	insertServicesOccurrencesForComponent(ComponentNode processedComponent, Session session, String tag) {
		Map<String, Integer> servicesOccurrences = processedComponent.getServicesOccurrences();
		if (servicesOccurrences == null) { return; }
		
		for (Map.Entry<String, Integer> entry : servicesOccurrences.entrySet()) {
			// String serviceName = FeatureNamesExtractor.makeNameReadable(entry.getKey());
			Integer occurrenceCount = entry.getValue();
			
			//CREATE COMPONENT TO GIVEN SERVICE CONNECTION
			if (tag == null) {
				createUsesComponentToServiceDependency(
						session, processedComponent.getImportName(), entry.getKey(), occurrenceCount.intValue());
			} else {
				createUsesComponentToServiceDependency(
						session, processedComponent.getImportName(), entry.getKey(), occurrenceCount.intValue(), tag);
			}
		}
	}

	public void insertServicesOccurrencesForService(ServiceNode serviceNode, Session session, String tag) {
		Map<String, Integer> servicesOccurrences = serviceNode.getServicesOccurrences();
		if (servicesOccurrences == null) { return; }
		
		for (Map.Entry<String, Integer> entry : servicesOccurrences.entrySet()) {
			// String serviceName = FeatureNamesExtractor.makeNameReadable(entry.getKey());
			Integer occurrenceCount = entry.getValue();
	
			//CREATE COMPONENT TO GIVEN SERVICE CONNECTION
			if (tag == null) {
				createUsesServiceToServiceDependency(
						session, serviceNode.getImportName(), entry.getKey(), occurrenceCount.intValue());
			} else {
				createUsesServiceToServiceDependency(
						session, serviceNode.getImportName(), entry.getKey(), occurrenceCount.intValue(), tag);
			}
		}
	}

	public void insertTreeInner(FunctionalNode processedMainNode, Session session, int depth, String tag, boolean includeExternal) {
		
		Iterator<FunctionalNode> functionalNodesIterator = new ArrayList<FunctionalNode>(
				processedMainNode.getFunctionalNodes().values()).iterator();
		
		int newDepth = depth + 1;
		while(functionalNodesIterator.hasNext()) {
			FunctionalNode processedNode = functionalNodesIterator.next();
			if (processedNode instanceof ModuleNode) {
				ModuleNode internalProcessedNode = (ModuleNode) processedNode;
				
				//CREATES MODULE NODE
				if (tag == null) {
					createModuleIfNotExists(session, internalProcessedNode.getImportName());
					
					//INSERTS CONNECTION BETWEEN MODULES
					createUsesModuleDependency(session, processedMainNode.getImportName(), internalProcessedNode.getImportName(), 1);
				} else {
					createModuleIfNotExists(session, internalProcessedNode.getImportName(), tag);
					
					//INSERTS CONNECTION BETWEEN MODULES
					createUsesModuleDependency(session, processedMainNode.getImportName(), internalProcessedNode.getImportName(), 1, tag);
				}
				
				insertTreeInner(internalProcessedNode, session, newDepth, tag, includeExternal);
			} else if (processedNode instanceof ComponentNode) {
				ComponentNode componentNode = (ComponentNode) processedNode;
				
				//CREATES COMPONENT NODE
				if (tag == null) {
					createComponentIfNotExists(session, componentNode.getImportName());
					
					//CREATES CONNECTION BETWEEN MODULE AND COMPONENT
					createUsesModuleToComponentDependency(session, processedMainNode.getImportName(), componentNode.getImportName(), 1);
				} else {
					createComponentIfNotExists(session, componentNode.getImportName(), tag);
					
					//CREATES CONNECTION BETWEEN MODULE AND COMPONENT
					createUsesModuleToComponentDependency(session, processedMainNode.getImportName(), componentNode.getImportName(), 1, tag);
				}
					
				insertSelectorOccurrencesForComponent(componentNode, session, tag);
				insertServicesOccurrencesForComponent(componentNode, session, tag);
			} else if (processedNode instanceof ServiceNode) {
			
				ServiceNode serviceNode = (ServiceNode) processedNode;
				
					//CREATES SERVICE NODE
				if (tag == null) {
					createServiceIfNotExists(session, serviceNode.getImportName());
					//CREATES CONNECTION BETWEEN MODULE AND SERVICE
					createUsesModuleToServiceDependency(session, processedMainNode.getImportName(), serviceNode.getImportName(), 1);
				} else {
					createServiceIfNotExists(session, serviceNode.getImportName(), tag);
					//CREATES CONNECTION BETWEEN MODULE AND SERVICE
					createUsesModuleToServiceDependency(session, processedMainNode.getImportName(), serviceNode.getImportName(), 1, tag);
				}
				
				
				insertServicesOccurrencesForService(serviceNode, session, tag);
				
				insertTreeInner(serviceNode, session, newDepth, tag, includeExternal);
			} else if (includeExternal && processedNode instanceof ExternalNode) {
				//CREATES EXTERNAL NODE
				if (tag == null) {
					createExternalNodeIfNotExists(session, processedNode.getImportName());
					
					//INSERTS CONNECTION BETWEEN NODES
					createUsesModuleToExternalDependency(session, processedMainNode.getImportName(), processedNode.getImportName(), 1);	
				} else {
					createExternalNodeIfNotExists(session, processedNode.getImportName(), tag);
					
					//INSERTS CONNECTION BETWEEN NODES
					createUsesModuleToExternalDependency(session, processedMainNode.getImportName(), processedNode.getImportName(), 1, tag);	
				}
			}
		}
	}
}
