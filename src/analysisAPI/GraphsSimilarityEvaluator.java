package analysisAPI;

import java.util.Map;

import analysisAPI.integration.MatricesMerger;
import graphSimilarities.EdgeToNodeMapping;
import graphSimilarities.GraphsVertexEdgeSimilaritiesLong;
import graphSimilarities.GraphsVertexSimilarities;
import graphSimilarities.GraphsVertexSimilaritiesLong;
import graphSimilarities.MaximizedHungarianMethodWrapper;
import semanticAttributesAggregation.SemanticMetricsManager;
import webSimilarity.AdjacencyToWeight;
import webSimilarity.CorrelationDegreeMatrix;
import webSimilarity.CorrelationToSimilarityMatrix;
import webSimilarity.adjacencyMatrixProcessing.AdjacencyMatrixProcessor;
import webSimilarity.clusteringAlgorithms.MatrixOverlappingPartition;
import webSimilarity.clusteringAlgorithms.MatrixPartition;
import webSimilarity.clusteringAlgorithms.NodeHierarchicCluster;


public class GraphsSimilarityEvaluator {
	
	public GraphsSimilarityEvaluator() {
		
	}
	
	public NodeHierarchicCluster process2Graphs(double graphAdjacency1[][], double graphAdjacency2[][], String namesMatrix1[], String namesMatrix2[]) {
		NodeHierarchicCluster clusteredResult = null;
		double mergedSimilarityMatrix[][];
		double mergedAdjacencyMatrix[][];
		GraphsVertexSimilarities graphsVertexSimilarities;
		MatricesMerger matricesMerger = new MatricesMerger();
		String mergedMatrixNames[];
		int resultMapping[];
		double help[][];
		String help2[];	
		
		if (graphAdjacency1.length > graphAdjacency2.length) {
			help = graphAdjacency1;
			graphAdjacency1 = graphAdjacency2;
			graphAdjacency2 = help;
			
			help2 = namesMatrix1;
			namesMatrix1 = namesMatrix2;
			namesMatrix2 = help2;
		}
		
		graphsVertexSimilarities = new GraphsVertexSimilarities(graphAdjacency1, graphAdjacency2);
		mergedSimilarityMatrix = graphsVertexSimilarities.computeSimilarityMatrices();
		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityMatrix);
		resultMapping = hungarianAlgorithm.execute();
		mergedAdjacencyMatrix = matricesMerger.mergeMatricesAccordingMappingFromSecondOneUnion(graphAdjacency1, graphAdjacency2, resultMapping);
		mergedMatrixNames = matricesMerger.transferNamesAccordingMapping(resultMapping, namesMatrix1, namesMatrix2);
		
		//STILL NEEDS CUSTOMIZATION
		//clusteredResult = this.processAdjacencyMatrixSemanticAnalysis(mergedAdjacencyMatrix, mergedMatrixNames);
		//clusteredResult.setMatrixNames(mergedMatrixNames);

		return clusteredResult;
	}
	
	public NodeHierarchicCluster process2Graphs(long graphAdjacency1[][], long graphAdjacency2[][], String namesMatrix1[], String namesMatrix2[]) {
		NodeHierarchicCluster clusteredResult = null;
		double mergedSimilarityMatrix[][];
		long mergedAdjacencyMatrix[][];
		GraphsVertexSimilaritiesLong graphsVertexSimilarities;
		MatricesMerger matricesMerger = new MatricesMerger();
		String mergedMatrixNames[];
		int resultMapping[];
		long help[][];
		String help2[];	
		
		if (graphAdjacency1.length > graphAdjacency2.length) {
			help = graphAdjacency1;
			graphAdjacency1 = graphAdjacency2;
			graphAdjacency2 = help;
			
			help2 = namesMatrix1;
			namesMatrix1 = namesMatrix2;
			namesMatrix2 = help2;
		}
		
		graphsVertexSimilarities = new GraphsVertexSimilaritiesLong(graphAdjacency1, graphAdjacency2);
		mergedSimilarityMatrix = graphsVertexSimilarities.computeSimilarityMatrices();
		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityMatrix);
		resultMapping = hungarianAlgorithm.execute();
		mergedAdjacencyMatrix = matricesMerger.mergeMatricesAccordingMappingFromSecondOneUnion(graphAdjacency1, graphAdjacency2, resultMapping);
		mergedMatrixNames = matricesMerger.transferNamesAccordingMapping(resultMapping, namesMatrix1, namesMatrix2);
		
		clusteredResult = this.processAdjacencyMatrixSemanticAnalysis(mergedAdjacencyMatrix, mergedMatrixNames);
		clusteredResult.setMatrixNames(mergedMatrixNames);

		return clusteredResult;
	}
	
	public NodeHierarchicCluster process2GraphsExtended(long graphAdjacency1[][], long graphAdjacency2[][], String namesMatrix1[], String namesMatrix2[]) {
		NodeHierarchicCluster clusteredResult = null;
		double mergedSimilarityVertexMatrix[][];
		double mergedSimilarityEdgeMatrix[][];
		long mergedAdjacencyMatrix[][];
		GraphsVertexEdgeSimilaritiesLong graphsVertexEdgeSimilarities;
		MatricesMerger matricesMerger = new MatricesMerger();
		String mergedMatrixNames[];
		int resultMapping[];
		long help[][];
		String help2[];	
		Map<String, EdgeToNodeMapping> mergedNodeMapping;
		
		if (graphAdjacency1.length > graphAdjacency2.length) {
			help = graphAdjacency1;
			graphAdjacency1 = graphAdjacency2;
			graphAdjacency2 = help;
			
			help2 = namesMatrix1;
			namesMatrix1 = namesMatrix2;
			namesMatrix2 = help2;
		}
		
		graphsVertexEdgeSimilarities = new GraphsVertexEdgeSimilaritiesLong(graphAdjacency1, graphAdjacency2, true);
		graphsVertexEdgeSimilarities.computeSimilarityMatrices();
		mergedSimilarityVertexMatrix = graphsVertexEdgeSimilarities.getConvergedMatrixY();
		mergedSimilarityEdgeMatrix = graphsVertexEdgeSimilarities.getConvergedMatrixX();
		MaximizedHungarianMethodWrapper hungarianAlgorithm = new MaximizedHungarianMethodWrapper(mergedSimilarityVertexMatrix);
		resultMapping = hungarianAlgorithm.execute();
		mergedAdjacencyMatrix = matricesMerger.mergeMatricesAccordingMappingFromSecondOneUnion(graphAdjacency1, graphAdjacency2, resultMapping);
		mergedMatrixNames = matricesMerger.transferNamesAccordingMapping(resultMapping, namesMatrix1, namesMatrix2);
		mergedNodeMapping = matricesMerger.transferNodeEdgesAccordingMappingUnion(resultMapping,
				graphsVertexEdgeSimilarities.getMappingEdgesToNodesForMatrix1(),
				graphsVertexEdgeSimilarities.getMappingEdgesToNodesForMatrix2(), namesMatrix1, namesMatrix2);
		
		clusteredResult = this.processAdjacencyMatrixSemanticAnalysis(mergedAdjacencyMatrix, mergedMatrixNames, mergedSimilarityEdgeMatrix, mergedNodeMapping);
		clusteredResult.setMatrixNames(mergedMatrixNames);

		return clusteredResult;
	}

	public NodeHierarchicCluster processAdjacencyMatrixSemanticAnalysis(
			long symmetricMatrix[][], String matrixColumnNames[], SemanticMetricsManager semanticMetricsManager) {
		AdjacencyToWeight adjacencyToWeightSymmetric = new AdjacencyToWeight(symmetricMatrix);
		double[][] weigthSymmetricMatrix = adjacencyToWeightSymmetric.convertToWeightMatrix(symmetricMatrix);

		NodeHierarchicCluster nodeHierarchicCluster;
		int matrixSize = weigthSymmetricMatrix.length;
	    long shortestPathMatrix[][] = new long[matrixSize][matrixSize];
	    int distance[][] = new int[matrixSize][matrixSize];
	    double shortestWeightsPathMatrix[][] = new double[matrixSize][matrixSize];
	    double[][] correlationMatrix;
	    double[][] similarityMatrix;
	    int nodesMapping[];
	    MatrixPartition matrixPartition;
	    
	    CorrelationDegreeMatrix.findMinimumWeightsAndDistance(weigthSymmetricMatrix, symmetricMatrix, CorrelationDegreeMatrix.CORRELATION_FACTOR, shortestPathMatrix, distance, shortestWeightsPathMatrix);

		correlationMatrix = CorrelationDegreeMatrix.constructCorrelationDegreeMatrix(shortestWeightsPathMatrix, distance, CorrelationDegreeMatrix.CORRELATION_FACTOR);
		
		CorrelationToSimilarityMatrix correlationToSimilarityMatrix = new CorrelationToSimilarityMatrix(correlationMatrix);
		similarityMatrix = correlationToSimilarityMatrix.constructSimilarityMatrix(semanticMetricsManager);
		AdjacencyMatrixProcessor.saveMatrixToFile("similarity.txt", similarityMatrix);

		matrixPartition = new MatrixPartition();
		nodeHierarchicCluster = new NodeHierarchicCluster();
		nodesMapping = new int[matrixSize];
		for (int index = 0; index < matrixSize; index++) {
			nodesMapping[index] = index;
		}
		matrixPartition.hierarchicalMatrixPartitioning(nodeHierarchicCluster, similarityMatrix, 4, 0, true, nodesMapping);
		return nodeHierarchicCluster;
	}
	
	
	public NodeHierarchicCluster processAdjacencyMatrixSemanticAnalysis(
			long symmetricMatrix[][], String matrixColumnNames[]) {
		AdjacencyToWeight adjacencyToWeightSymmetric = new AdjacencyToWeight(symmetricMatrix);
		double[][] weigthSymmetricMatrix = adjacencyToWeightSymmetric.convertToWeightMatrix(symmetricMatrix);

		NodeHierarchicCluster nodeHierarchicCluster;
		int matrixSize = weigthSymmetricMatrix.length;
	    long shortestPathMatrix[][] = new long[matrixSize][matrixSize];
	    int distance[][] = new int[matrixSize][matrixSize];
	    double shortestWeightsPathMatrix[][] = new double[matrixSize][matrixSize];
	    double[][] correlationMatrix;
	    double[][] similarityMatrix;
	    int nodesMapping[];
	    MatrixPartition matrixPartition;
	    
	    CorrelationDegreeMatrix.findMinimumWeightsAndDistance(weigthSymmetricMatrix, symmetricMatrix, CorrelationDegreeMatrix.CORRELATION_FACTOR, shortestPathMatrix, distance, shortestWeightsPathMatrix);

		correlationMatrix = CorrelationDegreeMatrix.constructCorrelationDegreeMatrix(shortestWeightsPathMatrix, distance, CorrelationDegreeMatrix.CORRELATION_FACTOR);
		
		CorrelationToSimilarityMatrix correlationToSimilarityMatrix = new CorrelationToSimilarityMatrix(correlationMatrix);
		similarityMatrix = correlationToSimilarityMatrix.constructSimilarityMatrix();
		AdjacencyMatrixProcessor.saveMatrixToFile("similarity.txt", similarityMatrix);

		matrixPartition = new MatrixPartition();
		nodeHierarchicCluster = new NodeHierarchicCluster();
		nodesMapping = new int[matrixSize];
		for (int index = 0; index < matrixSize; index++) {
			nodesMapping[index] = index;
		}
		matrixPartition.hierarchicalMatrixPartitioning(nodeHierarchicCluster, similarityMatrix, 4, 0, true, nodesMapping);

		return nodeHierarchicCluster;
	}

	
	public NodeHierarchicCluster processAdjacencyMatrixSemanticAnalysis(
			long symmetricMatrix[][], String matrixColumnNames[],
			double similarityEdgeMatrix[][], Map<String, EdgeToNodeMapping> mergedNodeMapping) {
		AdjacencyToWeight adjacencyToWeightSymmetric = new AdjacencyToWeight(symmetricMatrix, similarityEdgeMatrix, mergedNodeMapping);
		double[][] weigthSymmetricMatrix = adjacencyToWeightSymmetric.convertToWeightMatrix(symmetricMatrix);

		NodeHierarchicCluster nodeHierarchicCluster;
		int matrixSize = weigthSymmetricMatrix.length;
	    long shortestPathMatrix[][] = new long[matrixSize][matrixSize];
	    int distance[][] = new int[matrixSize][matrixSize];
	    double shortestWeightsPathMatrix[][] = new double[matrixSize][matrixSize];
	    double[][] correlationMatrix;
	    double[][] similarityMatrix;
	    int nodesMapping[];
	    MatrixPartition matrixPartition;
	    
	    CorrelationDegreeMatrix.findMinimumWeightsAndDistance(weigthSymmetricMatrix, symmetricMatrix, CorrelationDegreeMatrix.CORRELATION_FACTOR, shortestPathMatrix, distance, shortestWeightsPathMatrix);

		correlationMatrix = CorrelationDegreeMatrix.constructCorrelationDegreeMatrix(shortestWeightsPathMatrix, distance, CorrelationDegreeMatrix.CORRELATION_FACTOR);
		
		CorrelationToSimilarityMatrix correlationToSimilarityMatrix = new CorrelationToSimilarityMatrix(correlationMatrix);
		similarityMatrix = correlationToSimilarityMatrix.constructSimilarityMatrix();
		AdjacencyMatrixProcessor.saveMatrixToFile("similarity.txt", similarityMatrix);

		matrixPartition = new MatrixPartition();
		nodeHierarchicCluster = new NodeHierarchicCluster();
		nodesMapping = new int[matrixSize];
		for (int index = 0; index < matrixSize; index++) {
			nodesMapping[index] = index;
		}
		matrixPartition.hierarchicalMatrixPartitioning(nodeHierarchicCluster, similarityMatrix, 4, 0, true, nodesMapping);

		return nodeHierarchicCluster;
	}
}
