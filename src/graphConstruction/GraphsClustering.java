package graphConstruction;

import org.neo4j.driver.Session;

import analysisAPI.GraphsSimilarityEvaluator;
import webSimilarity.clusteringAlgorithms.NodeHierarchicCluster;


public class GraphsClustering {
	
	public static void main(String args[]) {
		NodeHierarchicCluster nodeHierarchicCluster;
		NodeHierarchicCluster nodeHierarchicClusterExtended;
		
		try (GraphToMatrixProcessor graphToMatrixProcessor = new GraphToMatrixProcessor("bolt://localhost:7687", "neo4j", "feature")) {
			nodeHierarchicCluster = GraphsClustering.mergeGraphs(graphToMatrixProcessor, AppliedGraphNames.PUZZLE_TO_PLAY, AppliedGraphNames.DESIGN_3D);
			nodeHierarchicClusterExtended =  GraphsClustering.mergeGraphsExtended(graphToMatrixProcessor, AppliedGraphNames.PUZZLE_TO_PLAY, AppliedGraphNames.DESIGN_3D);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		try (GraphHierarchicClustering graphHierarchicClusteringProcessor = new GraphHierarchicClustering("bolt://localhost:7687", "neo4j", "feature")) {
			try ( Session session = graphHierarchicClusteringProcessor.driver.session() ) {
				graphHierarchicClusteringProcessor.clearClusters(session, AppliedGraphNames.CLUSTERS_PUZZLE_DESIGN_MERGED);
				graphHierarchicClusteringProcessor.clearClusters(session, AppliedGraphNames.CLUSTERS_PUZZLE_DESIGN_MERGED_EXTENDED);
				nodeHierarchicCluster.insertClusters(graphHierarchicClusteringProcessor, session, AppliedGraphNames.CLUSTERS_PUZZLE_DESIGN_MERGED, AppliedGraphNames.CLUSTERS_PUZZLE_DESIGN_MERGED);
				nodeHierarchicClusterExtended.insertClusters(graphHierarchicClusteringProcessor, session, AppliedGraphNames.CLUSTERS_PUZZLE_DESIGN_MERGED_EXTENDED, AppliedGraphNames.CLUSTERS_PUZZLE_DESIGN_MERGED_EXTENDED);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static NodeHierarchicCluster mergeGraphs(GraphToMatrixProcessor graphToMatrixProcessor, String tagGraph1, String tagGraph2) {
		MatrixRepresentation adjacencyGraph1 = graphToMatrixProcessor.getAdjacencyMatrixFromGraph(tagGraph1);
		MatrixRepresentation adjacencyGraph2 = graphToMatrixProcessor.getAdjacencyMatrixFromGraph(tagGraph2);
		long[][] adjacencyMatrixGraph1 = adjacencyGraph1.getMatrix();
		long[][] adjacencyMatrixGraph2 = adjacencyGraph2.getMatrix();
		String graph1Names[] = adjacencyGraph1.getMatrixNames();
		String graph2Names[] = adjacencyGraph2.getMatrixNames();
		
		GraphsSimilarityEvaluator graphSimilarityEvaluator = new GraphsSimilarityEvaluator();
		NodeHierarchicCluster clusteredResult = graphSimilarityEvaluator.process2Graphs(
				adjacencyMatrixGraph1, adjacencyMatrixGraph2, graph1Names, graph2Names);
		// clusteredResult.printHierarchyWithNames(0);
		return clusteredResult;
		//clusteredResult.insertClusters(graphToMatrixProcessor, 0);
	}
	
	private static NodeHierarchicCluster mergeGraphsExtended(GraphToMatrixProcessor graphToMatrixProcessor, String tagGraph1, String tagGraph2) {
		MatrixRepresentation adjacencyGraph1 = graphToMatrixProcessor.getAdjacencyMatrixFromGraph(tagGraph1);
		MatrixRepresentation adjacencyGraph2 = graphToMatrixProcessor.getAdjacencyMatrixFromGraph(tagGraph2);
		long[][] adjacencyMatrixGraph1 = adjacencyGraph1.getMatrix();
		long[][] adjacencyMatrixGraph2 = adjacencyGraph2.getMatrix();
		String graph1Names[] = adjacencyGraph1.getMatrixNames();
		String graph2Names[] = adjacencyGraph2.getMatrixNames();
		
		GraphsSimilarityEvaluator graphSimilarityEvaluator = new GraphsSimilarityEvaluator();
		NodeHierarchicCluster clusteredResult = graphSimilarityEvaluator.process2GraphsExtended(
				adjacencyMatrixGraph1, adjacencyMatrixGraph2, graph1Names, graph2Names);
		// clusteredResult.printHierarchyWithNames(0);
		return clusteredResult;
		//clusteredResult.insertClusters(graphToMatrixProcessor, 0);
	}
}
