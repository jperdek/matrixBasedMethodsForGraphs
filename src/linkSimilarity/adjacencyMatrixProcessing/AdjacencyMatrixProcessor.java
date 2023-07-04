package linkSimilarity.adjacencyMatrixProcessing;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import semanticAttributesAggregation.SemanticMetricsManager;
import linkSimilarity.AdjacencyToWeight;
import linkSimilarity.CorrelationDegreeMatrix;
import linkSimilarity.CorrelationToSimilarityMatrix;
import linkSimilarity.clusteringAlgorithms.MatrixOverlappingPartition;
import linkSimilarity.clusteringAlgorithms.MatrixPartition;
import linkSimilarity.clusteringAlgorithms.NodeHierarchicCluster;


public class AdjacencyMatrixProcessor {

	public AdjacencyMatrixProcessor() {
		
	}
	
	public void processAdjacencyMatrix(long symmetricMatrix[][], String matrixColumnNames[]) {
		AdjacencyToWeight adjacencyToWeightSymmetric = new AdjacencyToWeight(symmetricMatrix);
		double[][] weigthSymmetricMatrix = adjacencyToWeightSymmetric.convertToWeightMatrix(symmetricMatrix);

		
		int matrixSize = weigthSymmetricMatrix.length;
	    long shortestPathMatrix[][] = new long[matrixSize][matrixSize];
	    int distance[][] = new int[matrixSize][matrixSize];
	    double shortestWeightsPathMatrix[][] = new double[matrixSize][matrixSize];

	    CorrelationDegreeMatrix.findMinimumWeightsAndDistance(weigthSymmetricMatrix, symmetricMatrix, CorrelationDegreeMatrix.CORRELATION_FACTOR, shortestPathMatrix, distance, shortestWeightsPathMatrix);

		double[][] correlationMatrix = CorrelationDegreeMatrix.constructCorrelationDegreeMatrix(shortestWeightsPathMatrix, distance, CorrelationDegreeMatrix.CORRELATION_FACTOR);
		
		CorrelationToSimilarityMatrix correlationToSimilarityMatrix = new CorrelationToSimilarityMatrix(correlationMatrix);
		double[][] similarityMatrix = correlationToSimilarityMatrix.constructSimilarityMatrix();
		AdjacencyMatrixProcessor.saveMatrixToFile("similarity.txt", similarityMatrix);
		//System.out.println("SIMILARITY MATRIX");
		//correlationToSimilarityMatrix.printSimilarityMatrix(similarityMatrix);
		
		MatrixPartition matrixPartition = new MatrixPartition();
		NodeHierarchicCluster nodeHierarchicCluster = new NodeHierarchicCluster();
		int nodesMapping[] = new int[matrixSize];
		for (int index = 0; index < matrixSize; index++) {
			nodesMapping[index] = index;
		}
		matrixPartition.hierarchicalMatrixPartitioning(nodeHierarchicCluster, similarityMatrix, 2, 0, true, nodesMapping);
		nodeHierarchicCluster.printHierarchy(0);
		nodeHierarchicCluster.saveHierarchyToCSV("oneClusterPuzzleToPlay.csv", matrixColumnNames, ";");
		
		NodeHierarchicCluster nodeHierarchicCluster2 = new NodeHierarchicCluster();
		int nodesMapping2[] = new int[matrixSize];
		for (int index = 0; index < matrixSize; index++) {
			nodesMapping2[index] = index;
		}
		MatrixOverlappingPartition matrixOverlappingPartition2 = new MatrixOverlappingPartition();
		matrixOverlappingPartition2.hierarchicalMatrixPartitioningWithOverlapping(nodeHierarchicCluster2, similarityMatrix, 5, 0, true, nodesMapping2, 1000, 0);
		nodeHierarchicCluster2.printHierarchy(0);
		nodeHierarchicCluster2.saveHierarchyToCSV("manyClusterPuzzleToPlay.csv", matrixColumnNames, ";");
	}
	
	public void processAdjacencyMatrixSemanticAnalysis(
			long symmetricMatrix[][], String matrixColumnNames[], SemanticMetricsManager semanticMetricsManager) {
		AdjacencyToWeight adjacencyToWeightSymmetric = new AdjacencyToWeight(symmetricMatrix);
		double[][] weigthSymmetricMatrix = adjacencyToWeightSymmetric.convertToWeightMatrix(symmetricMatrix);

		
		int matrixSize = weigthSymmetricMatrix.length;
	    long shortestPathMatrix[][] = new long[matrixSize][matrixSize];
	    int distance[][] = new int[matrixSize][matrixSize];
	    double shortestWeightsPathMatrix[][] = new double[matrixSize][matrixSize];

	    CorrelationDegreeMatrix.findMinimumWeightsAndDistance(weigthSymmetricMatrix, symmetricMatrix, CorrelationDegreeMatrix.CORRELATION_FACTOR, shortestPathMatrix, distance, shortestWeightsPathMatrix);

		double[][] correlationMatrix = CorrelationDegreeMatrix.constructCorrelationDegreeMatrix(shortestWeightsPathMatrix, distance, CorrelationDegreeMatrix.CORRELATION_FACTOR);
		
		CorrelationToSimilarityMatrix correlationToSimilarityMatrix = new CorrelationToSimilarityMatrix(correlationMatrix);
		double[][] similarityMatrix = correlationToSimilarityMatrix.constructSimilarityMatrix(semanticMetricsManager);
		AdjacencyMatrixProcessor.saveMatrixToFile("similarity.txt", similarityMatrix);
		//System.out.println("SIMILARITY MATRIX");
		//correlationToSimilarityMatrix.printSimilarityMatrix(similarityMatrix);
		
		MatrixPartition matrixPartition = new MatrixPartition();
		NodeHierarchicCluster nodeHierarchicCluster = new NodeHierarchicCluster();
		int nodesMapping[] = new int[matrixSize];
		for (int index = 0; index < matrixSize; index++) {
			nodesMapping[index] = index;
		}
		matrixPartition.hierarchicalMatrixPartitioning(nodeHierarchicCluster, similarityMatrix, 2, 0, true, nodesMapping);
		nodeHierarchicCluster.printHierarchy(0);
		nodeHierarchicCluster.saveHierarchyToCSV("oneClusterPuzzleToPlay.csv", matrixColumnNames, ";");
		
		NodeHierarchicCluster nodeHierarchicCluster2 = new NodeHierarchicCluster();
		int nodesMapping2[] = new int[matrixSize];
		for (int index = 0; index < matrixSize; index++) {
			nodesMapping2[index] = index;
		}
		MatrixOverlappingPartition matrixOverlappingPartition2 = new MatrixOverlappingPartition();
		matrixOverlappingPartition2.hierarchicalMatrixPartitioningWithOverlapping(nodeHierarchicCluster2, similarityMatrix, 5, 0, true, nodesMapping2, 1000, 0);
		nodeHierarchicCluster2.printHierarchy(0);
		nodeHierarchicCluster2.saveHierarchyToCSV("manyClusterPuzzleToPlay.csv", matrixColumnNames, ";");
	}
	
	public static void saveMatrixToFile(String pathToFile, double matrix[][]) {
		FileWriter fw = null;
		BufferedWriter bufferedWriter = null;
		try {
			fw = new FileWriter(pathToFile);
			bufferedWriter = new BufferedWriter(fw);
			
			//bufferedWriter.append("[");
			for (int i = 0; i < matrix.length; i++) {
				//bufferedWriter.append("[");
				for (int j = 0; j < matrix[i].length; j++) {
					bufferedWriter.append(Double.toString(matrix[i][j]));
					if (j != matrix[i].length - 1) {
						bufferedWriter.append(", ");
					}
				}
				//bufferedWriter.append("]");
				bufferedWriter.append("\n");
			}
			//bufferedWriter.append("]\n");
		} catch(FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			if(bufferedWriter != null) { 
				try { bufferedWriter.close(); }
				catch(IOException e) {
					e.printStackTrace();
				};
			}
			if(fw != null) { 
				try {
					fw.close();
				} catch(IOException e) {
				
				};
			}
		}
	}
}
