package graphConstruction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import graphConstruction.scoringEvaluation.PageRank;
import semanticAttributesAggregation.JackardSimilarity;
import semanticAttributesAggregation.SemanticMetricsManager;
import webSimilarity.adjacencyMatrixProcessing.AdjacencyMatrixProcessor;
import static org.neo4j.driver.Values.parameters;
import org.neo4j.driver.Record;


// inspired by https://www.markhneedham.com/blog/2014/05/20/neo4j-2-0-creating-adjacency-matrices/
@SuppressWarnings("unchecked")
public class GraphToMatrixProcessor extends GraphCreator {

	public static final String DELIMITER = "|&&&|";
	

	public GraphToMatrixProcessor(String uri, String user, String password) {
		super(uri, user, password);
	}
	
	@SuppressWarnings("unused")
	private static void processUntaggedGraph(GraphToMatrixProcessor graphToMatrixProcessor) {
		MatrixRepresentation adjacency = graphToMatrixProcessor.getAdjacencyMatrixFromGraph();
		//adjacency.printMatrix(); //PRINTS ADJACENCY MATRIX
		//adjacency.saveToFile("adjacency.txt");
		
		//HyperlinkMatrix hyperlinkMatrix = HyperlinkMatrix.convertAdjacencyMatrixToHyperlink(adjacency);
		//PageRank p = new PageRank(adjacency.getMatrix(), adjacency.getMatrixNames(), hyperlinkMatrix.getSize());
		//p.calc(hyperlinkMatrix.getSize());
		SemanticMetricsManager semanticMetricsManager = new SemanticMetricsManager();
		Map<String, Double> classMetricsValuesMap = graphToMatrixProcessor.getMetricValuesFromRelation("classSimilarity", adjacency.getMatrixNames());
		JackardSimilarity classSimilarity = new JackardSimilarity("classSimilarity", 0.5, classMetricsValuesMap);
		semanticMetricsManager.addMetric(classSimilarity);

		Map<String, Double> attributesMetricsValuesMap = graphToMatrixProcessor.getMetricValuesFromRelation("attributesSimilarity", adjacency.getMatrixNames());
		JackardSimilarity attributesSimilarity = new JackardSimilarity("attributesSimilarity", 0.85, attributesMetricsValuesMap);
		semanticMetricsManager.addMetric(attributesSimilarity);

		clusterAdjacencyMatrixSemanticAnalysis(adjacency, semanticMetricsManager);
		
		//clusterAdjacencyMatrix(adjacency);
	}
	
	private static void processGraph(GraphToMatrixProcessor graphToMatrixProcessor, String tag) {
		MatrixRepresentation adjacency = graphToMatrixProcessor.getAdjacencyMatrixFromGraph(tag);

		SemanticMetricsManager semanticMetricsManager = new SemanticMetricsManager();
		Map<String, Double> classMetricsValuesMap = graphToMatrixProcessor.getMetricValuesFromRelation("classSimilarity", adjacency.getMatrixNames(), tag);
		JackardSimilarity classSimilarity = new JackardSimilarity("classSimilarity", 0.5, classMetricsValuesMap);
		semanticMetricsManager.addMetric(classSimilarity);

		Map<String, Double> attributesMetricsValuesMap = graphToMatrixProcessor.getMetricValuesFromRelation("attributesSimilarity", adjacency.getMatrixNames(), tag);
		JackardSimilarity attributesSimilarity = new JackardSimilarity("attributesSimilarity", 0.85, attributesMetricsValuesMap);
		semanticMetricsManager.addMetric(attributesSimilarity);

		clusterAdjacencyMatrixSemanticAnalysis(adjacency, semanticMetricsManager);
		
		//clusterAdjacencyMatrix(adjacency);
	}
	
	public static void main(String args[]) {

		try (GraphToMatrixProcessor graphToMatrixProcessor = new GraphToMatrixProcessor("bolt://localhost:7687", "neo4j", "feature")) {
			// GraphToMatrixProcessor.processUntaggedGraph(graphToMatrixProcessor);
			GraphToMatrixProcessor.processGraph(graphToMatrixProcessor, AppliedGraphNames.PUZZLE_TO_PLAY);
			GraphToMatrixProcessor.processGraph(graphToMatrixProcessor, AppliedGraphNames.DESIGN_3D);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String prepareRelationQuery(String relationName, String names[]) {
		String relationQuery = "";
		for (String name: names) {
			if (relationQuery.equals("")) {
				relationQuery = "MATCH (n1 {name: '" + name + "' })-[r:" + relationName +"]->(n2) RETURN n1.name AS firstNode, n2.name AS secondNode, r.value AS metricsValue";
			} else {
				relationQuery = relationQuery + " UNION MATCH (n1 {name: '" + name + "' })-[r:" + relationName + "]->(n2) RETURN n1.name AS firstNode, n2.name AS secondNode, r.value AS metricsValue";
			}
		}
		return relationQuery;
	}
	
	private String prepareRelationQuery(String relationName, String names[], String tag) {
		String relationQuery = "";
		for (String name: names) {
			if (relationQuery.equals("")) {
				relationQuery = "MATCH (n1 {name: '" + name + "', tag: '" + tag + "' })-[r:" + 
			relationName +" { tag: '" + tag + "', tag: '" + tag + "' }]->(n2 { tag: '" + tag + "' }) RETURN n1.name AS firstNode, n2.name AS secondNode, r.value AS metricsValue";
			} else {
				relationQuery = relationQuery + " UNION MATCH (n1 {name: '" + name + "' })-[r:" + 
						relationName + " { tag: '" + tag + "' }]->(n2 { tag: '" + tag + "' }) RETURN n1.name AS firstNode, n2.name AS secondNode, r.value AS metricsValue";
			}
		}
		return relationQuery;
	}

	private Map<String, Integer> createMapFromNames(String names[]) {
		Map<String, Integer> namesMap = new HashMap<String, Integer>();
		for(int index = 0; index < names.length; index++) {
			namesMap.put(names[index], index);
		}
		return namesMap;
	}
	
	public Map<String, Double> getMetricValuesFromRelation(String relationName, String matrixColumnNames[]) {
		Map<String, Double> metricsFromRelations = new HashMap<String, Double>();
		String query = this.prepareRelationQuery(relationName, matrixColumnNames);
		Map<String, Integer> namesMap = this.createMapFromNames(matrixColumnNames);
		
		try (Session session = driver.session()) {
        	List<Record> metricsRelations = (List<Record>) session.readTransaction(tx -> {
				Result result = tx.run(query);
				return result.list();
        	});
        	this.prepareMapFromRelations(metricsRelations, metricsFromRelations, namesMap);
    		return metricsFromRelations;
        }
	}
	
	public Map<String, Double> getMetricValuesFromRelation(String relationName, String matrixColumnNames[], String tag) {
		Map<String, Double> metricsFromRelations = new HashMap<String, Double>();
		String query = this.prepareRelationQuery(relationName, matrixColumnNames, tag);
		Map<String, Integer> namesMap = this.createMapFromNames(matrixColumnNames);
		
		try (Session session = driver.session()) {
        	List<Record> metricsRelations = (List<Record>) session.readTransaction(tx -> {
				Result result = tx.run(query);
				return result.list();
        	});
        	this.prepareMapFromRelations(metricsRelations, metricsFromRelations, namesMap);
    		return metricsFromRelations;
        }
	}
	
	public void prepareMapFromRelations(List<Record> relationRecords, 
			Map<String, Double> metricsFromRelations, Map<String, Integer> namesMap) {
		Iterator<Record> iterator = relationRecords.iterator();
		String name1, name2;
		String combinedName;
		double metricValue;
		int matrixIndexNode1, matrixIndexNode2;
		
		while(iterator.hasNext()) {
			Record rowRecord = iterator.next();
			name1 = rowRecord.get("firstNode").asString();
			name2 = rowRecord.get("secondNode").asString();
			metricValue = rowRecord.get("metricsValue").asDouble();
			
			matrixIndexNode1 = namesMap.get(name1);
			matrixIndexNode2 = namesMap.get(name2);
			combinedName = Integer.toString(matrixIndexNode1) + GraphToMatrixProcessor.DELIMITER + Integer.toString(matrixIndexNode2);
			//System.out.println(combinedName);
			metricsFromRelations.put(combinedName, metricValue);
		}
	}

	public static void clusterAdjacencyMatrixSemanticAnalysis(
			MatrixRepresentation adjacency, SemanticMetricsManager semanticMetricsManager) {
		//PROCESS ADJACENCY MATRIX
		AdjacencyMatrixProcessor adjacencyMatrixProcessor = new AdjacencyMatrixProcessor();
		String matrixNames[] = adjacency.getMatrixNames();
		adjacencyMatrixProcessor.processAdjacencyMatrixSemanticAnalysis(
				adjacency.getMatrix(), matrixNames, semanticMetricsManager);	
	}
	
	public static void clusterAdjacencyMatrix(MatrixRepresentation adjacency) {
		//PROCESS ADJACENCY MATRIX
		AdjacencyMatrixProcessor adjacencyMatrixProcessor = new AdjacencyMatrixProcessor();
		String matrixNames[] = adjacency.getMatrixNames();
		adjacencyMatrixProcessor.processAdjacencyMatrix(adjacency.getMatrix(), matrixNames);	
	}
	
	public MatrixRepresentation getAdjacencyMatrixFromGraph() {
        try ( Session session = driver.session() ) {
        	List<Record> adjacencyMatrix = (List<Record>) session.readTransaction( tx -> {
				Result result = tx.run( "MATCH (g) "
						+ "WITH g "
						+ "ORDER BY g.name "
						+ " "
						+ "WITH COLLECT(g) AS groups "
						+ "UNWIND groups AS g1 "
						+ "UNWIND groups AS g2 "
						+ "OPTIONAL MATCH path = (g1)<-[:Uses]-()-[:Uses]->(g2) "
						+ " "
						+ "WITH g1, g2, CASE WHEN path is null THEN 0 ELSE COUNT(path) END AS overlap "
						+ "ORDER BY g1.name, g2.name "
						+ "RETURN g1.name AS name, COLLECT(overlap) AS row "
						+ "ORDER BY g1.name");
				return result.list();
        	} );

        	// printAdjacencyMatrix(adjacencyMatrix);
        	long matrix[][] = adjacencyMatrixToDoubleArray(adjacencyMatrix);
        	String names[] = getAdjacencyMatrixNames(adjacencyMatrix);
        	return new MatrixRepresentation(matrix, names);
        }
    }
	
	public MatrixRepresentation getAdjacencyMatrixFromGraph(String tag) {
        try ( Session session = driver.session() ) {
        	List<Record> adjacencyMatrix = (List<Record>) session.readTransaction( tx -> {
				Result result = tx.run( "MATCH (g { tag: $tag }) "
						+ "WITH g "
						+ "ORDER BY g.name "
						+ " "
						+ "WITH COLLECT(g) AS groups "
						+ "UNWIND groups AS g1 "
						+ "UNWIND groups AS g2 "
						+ "OPTIONAL MATCH path = (g1 { tag: $tag })<-[:Uses { tag: $tag }]-({tag: $tag })-[:Uses { tag: $tag }]->(g2 { tag: $tag }) "
						+ " "
						+ "WITH g1, g2, CASE WHEN path is null THEN 0 ELSE COUNT(path) END AS overlap "
						+ "ORDER BY g1.name, g2.name "
						+ "RETURN g1.name AS name, COLLECT(overlap) AS row "
						+ "ORDER BY g1.name", parameters("tag", tag ));
				return result.list();
        	} );

        	// printAdjacencyMatrix(adjacencyMatrix);
        	long matrix[][] = adjacencyMatrixToDoubleArray(adjacencyMatrix);
        	String names[] = getAdjacencyMatrixNames(adjacencyMatrix);
        	return new MatrixRepresentation(matrix, names);
        }
    }
	
	public void printAdjacencyMatrix(List<Record> adjacencyMatrix) {
		Iterator<Record> iterator = adjacencyMatrix.iterator();
		
		while(iterator.hasNext()) {
			Record rowRecord = iterator.next();
			System.out.println(rowRecord.get("row"));
		}
	}
	
	public String[] getAdjacencyMatrixNames(List<Record> adjacencyMatrix) {
		Iterator<Record> iterator = adjacencyMatrix.iterator();
		int size = adjacencyMatrix.size();
		
		String names[] = new String[size];
		int rowNumber = 0;
		while(iterator.hasNext()) {
			Record rowRecord = iterator.next();
			names[rowNumber] = rowRecord.get("name").asString(); 
			rowNumber++;
		}
		return names;
	}
	
	public long[][] adjacencyMatrixToDoubleArray(List<Record> adjacencyMatrix) {
		Iterator<Record> iterator = adjacencyMatrix.iterator();
		int size = adjacencyMatrix.size();
		
		long matrix[][] = new long[size][];
		int rowNumber = 0;
		while(iterator.hasNext()) {
			Record rowRecord = iterator.next();
			matrix[rowNumber] = ((List<Long>) rowRecord.get("row").asObject()).stream().mapToLong(x->x).toArray(); 
			rowNumber++;
		}
		return matrix;
	}
}
