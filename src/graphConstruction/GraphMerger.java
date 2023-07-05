package graphConstruction;

import analysisAPI.integration.MatricesMerger;
import graphSimilarities.GraphsVertexSimilaritiesLong;
import graphSimilarities.MaximizedHungarianMethodWrapper;
import main.MainConfiguration;


public class GraphMerger {

	public static void main(String args[]) {
		try (GraphToMatrixProcessor graphToMatrixProcessor = new GraphToMatrixProcessor(
				MainConfiguration.NEO4J_DB_BOLT_CONNECTION, MainConfiguration.NEO4J_DB_NAME, MainConfiguration.NEO4J_DB_PASSWORD)) {
			GraphMerger.mergeGraphs(graphToMatrixProcessor, AppliedGraphNames.PUZZLE_TO_PLAY, AppliedGraphNames.DESIGN_3D);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void mergeGraphs(GraphToMatrixProcessor graphToMatrixProcessor, String tagGraph1, String tagGraph2) {
		MatrixRepresentation adjacencyGraph1 = graphToMatrixProcessor.getAdjacencyMatrixFromGraph(tagGraph1);
		MatrixRepresentation adjacencyGraph2 = graphToMatrixProcessor.getAdjacencyMatrixFromGraph(tagGraph2);
		long[][] adjacencyMatrixGraph1 = adjacencyGraph1.getMatrix();
		long[][] adjacencyMatrixGraph2 = adjacencyGraph2.getMatrix();
		String graph1Names[] = adjacencyGraph1.getMatrixNames();
		String graph2Names[] = adjacencyGraph2.getMatrixNames();
		
		GraphsVertexSimilaritiesLong graphsVertexSimilarities = new GraphsVertexSimilaritiesLong(adjacencyMatrixGraph1, adjacencyMatrixGraph2);
		double mergedSimilarityMatrix[][] = graphsVertexSimilarities.computeSimilarityMatrices();
		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityMatrix);
		int resultMapping[] = hungarianAlgorithm.execute();
		
		MatricesMerger matrixMerger = new MatricesMerger();
		long[][] matrix = matrixMerger.mergeMatricesAccordingMappingFromSecondOneUnion(adjacencyMatrixGraph2, adjacencyMatrixGraph1, resultMapping);

		String mergedNames[] = matrixMerger.transferNamesAccordingMapping(resultMapping, graph2Names, graph1Names);
	}
}
